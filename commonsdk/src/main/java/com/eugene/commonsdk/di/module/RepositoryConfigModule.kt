package com.eugene.commonsdk.di.module

import android.app.Application
import com.eugene.commonsdk.integration.cache.Cache
import com.eugene.commonsdk.integration.cache.CacheType
import com.eugene.commonsdk.integration.cache.IntelligentCache
import com.eugene.commonsdk.integration.cache.LruCache
import com.eugene.commonsdk.module.db.RoomConfiguration
import com.eugene.commonsdk.module.imageloader.BaseImageLoaderStrategy
import com.eugene.commonsdk.module.imageloader.ImageConfig
import com.eugene.commonsdk.module.network.BaseUrl
import com.eugene.commonsdk.module.network.GlobalHttpHandler
import com.eugene.commonsdk.module.network.log.DefaultFormatPrinter
import com.eugene.commonsdk.module.network.log.FormatPrinter
import com.eugene.commonsdk.module.network.log.RequestInterceptor
import com.eugene.commonsdk.utils.DataHelper
import dagger.Module
import dagger.Provides
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.internal.Util
import java.io.File
import java.util.concurrent.*
import javax.inject.Singleton

/**
 *可向框架中注入外部配置的自定义参数
 */
@Module
class RepositoryConfigModule(private val builder: Builder) {

    companion object {
        fun build(init: Builder.() -> Unit) = Builder(init).build()
    }

    private var mApiUrl: HttpUrl? = null
    private var mBaseUrl: BaseUrl? = null
    private var mLoaderStrategy: BaseImageLoaderStrategy<ImageConfig>? = null
    private var mHandler: GlobalHttpHandler? = null
    private var mInterceptors: ArrayList<Interceptor>? = null
    private var mErrorListener: ResponseErrorListener? = null
    private var mCacheFile: File? = null
    private var mRetrofitConfiguration: ClientModule.RetrofitConfiguration? = null
    private var mOkhttpConfiguration: ClientModule.OkhttpConfiguration? = null
    private var mRxCacheConfiguration: ClientModule.RxCacheConfiguration? = null
    private var mGsonConfiguration: AppModule.GsonConfiguration? = null
    private var mRoomConfiguration: RoomConfiguration<*>? = null
    private var mPrintHttpLogLevel: RequestInterceptor.Level? = null
    private var mFormatPrinter: FormatPrinter? = null
    private var mExecutorService: ExecutorService? = null
    private var mCacheFactory: Cache.Factory? = null

    init {
        this.mApiUrl = builder.mApiUrl
        this.mBaseUrl = builder.mBaseUrl
        this.mLoaderStrategy = builder.mLoaderStrategy
        this.mHandler = builder.mHandler
        this.mInterceptors = builder.mInterceptors
        this.mErrorListener = builder.mErrorListener
        this.mCacheFile = builder.mCacheFile
        this.mRetrofitConfiguration = builder.mRetrofitConfiguration
        this.mOkhttpConfiguration = builder.mOkhttpConfiguration
        this.mRxCacheConfiguration = builder.mRxCacheConfiguration
        this.mGsonConfiguration = builder.mGsonConfiguration
        this.mPrintHttpLogLevel = builder.mPrintHttpLogLevel
        this.mFormatPrinter = builder.mFormatPrinter
        this.mExecutorService = builder.mExecutorService
        this.mCacheFactory = builder.mCacheFactory
    }

    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     *
     * @return
     */
    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl? {
        if (mBaseUrl != null) {
            val httpUrl = mBaseUrl?.url()
            if (httpUrl != null) {
                return httpUrl
            }
        }
        return mApiUrl ?: HttpUrl.parse("https://api.github.com/")
    }

    @Singleton
    @Provides
    fun provideInterceptors(): ArrayList<Interceptor>? {
        return mInterceptors
    }

    /**
     * 提供图片加载框架,默认使用 [Glide]
     *
     * @return
     */
    @Singleton
    @Provides
    fun provideImageLoaderStrategy(): BaseImageLoaderStrategy<ImageConfig>? {
        return mLoaderStrategy
    }

    /**
     * 提供处理 Http 请求和响应结果的处理类
     *
     * @return
     */
    @Singleton
    @Provides
    fun provideGlobalHttpHandler(): GlobalHttpHandler? {
        return mHandler
    }

    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    fun provideCacheFile(application: Application): File {
        return mCacheFile ?: DataHelper.getCacheFile(application)
    }

    /**
     * 提供处理 RxJava 错误的管理器的回调
     *
     * @return
     */
    @Singleton
    @Provides
    fun provideResponseErrorListener(): ResponseErrorListener {
        return mErrorListener ?: ResponseErrorListener.EMPTY
    }

    @Singleton
    @Provides
    fun provideRetrofitConfiguration(): ClientModule.RetrofitConfiguration? {
        return mRetrofitConfiguration
    }

    @Singleton
    @Provides
    fun provideOkhttpConfiguration(): ClientModule.OkhttpConfiguration? {
        return mOkhttpConfiguration
    }

    @Singleton
    @Provides
    fun provideRxCacheConfiguration(): ClientModule.RxCacheConfiguration? {
        return mRxCacheConfiguration
    }

    @Singleton
    @Provides
    fun provideGsonConfiguration(): AppModule.GsonConfiguration? {
        return mGsonConfiguration
    }

    @Singleton
    @Provides
    fun provideRoomConfiguration(): RoomConfiguration<*>? {
        return mRoomConfiguration ?: RoomConfiguration.EMPTY
    }

    @Singleton
    @Provides
    fun providePrintHttpLogLevel(): RequestInterceptor.Level {
        return mPrintHttpLogLevel ?: RequestInterceptor.Level.ALL
    }

    @Singleton
    @Provides
    fun provideFormatPrinter(): FormatPrinter {
        return mFormatPrinter ?: DefaultFormatPrinter()
    }


    @Singleton
    @Provides
    fun provideCacheFactory(application: Application): Cache.Factory? {
        if (mCacheFactory == null)
            mCacheFactory = Cache.Factory { type ->
                //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
                //使用 RepositoryConfigModule.Builder#cacheFactory() 即可扩展
                when (type.cacheTypeId) {
                    //Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                    CacheType.EXTRAS_TYPE_ID,
                    CacheType.ACTIVITY_CACHE_TYPE_ID,
                    CacheType.FRAGMENT_CACHE_TYPE_ID -> IntelligentCache<Any>(type.calculateCacheSize(application))
                    //其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
                    else -> LruCache<Any, Any>(type.calculateCacheSize(application))
                }
            }

        return mCacheFactory
    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [Executor]
     */
    @Singleton
    @Provides
    fun provideExecutorService(): ExecutorService {
        return mExecutorService ?: ThreadPoolExecutor(
                0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                SynchronousQueue(), Util.threadFactory("Arms Executor", false)
        )
    }


    class Builder(init: Builder.() -> Unit) {

        internal var mApiUrl: HttpUrl? = null
        internal var mBaseUrl: BaseUrl? = null
        internal var mLoaderStrategy: BaseImageLoaderStrategy<ImageConfig>? = null
        internal var mHandler: GlobalHttpHandler? = null
        internal var mInterceptors: ArrayList<Interceptor>? = null
        internal var mErrorListener: ResponseErrorListener? = null
        internal var mCacheFile: File? = null
        internal var mRetrofitConfiguration: ClientModule.RetrofitConfiguration? = null
        internal var mOkhttpConfiguration: ClientModule.OkhttpConfiguration? = null
        internal var mRxCacheConfiguration: ClientModule.RxCacheConfiguration? = null
        internal var mGsonConfiguration: AppModule.GsonConfiguration? = null
        internal var mRoomConfiguration: RoomConfiguration<*>? = null
        internal var mPrintHttpLogLevel: RequestInterceptor.Level? = null
        internal var mFormatPrinter: FormatPrinter? = null
        internal var mExecutorService: ExecutorService? = null
        internal var mCacheFactory: Cache.Factory? = null

        init {
            init()
        }

        fun apiUrl(init: Builder.() -> String) = apply {
            if (init().isBlank()) {
                throw NullPointerException("BaseUrl can not be empty")
            }
            mApiUrl = HttpUrl.parse(init())
        }

        fun baseUrl(init: Builder.() -> BaseUrl) = apply {
            mBaseUrl = init()
        }

        fun imageLoaderStrategy(init: Builder.() -> BaseImageLoaderStrategy<ImageConfig>) = apply {
            mLoaderStrategy = init()
        }

        fun globalHttpHandler(init: Builder.() -> GlobalHttpHandler) = apply {
            mHandler = init()
        }

        fun addInterceptor(init: Builder.() -> Interceptor) = apply {
            if (mInterceptors == null)
                mInterceptors = arrayListOf()

            mInterceptors?.add(init())
        }

        fun responseErrorListener(init: Builder.() -> ResponseErrorListener) = apply {
            mErrorListener = init()
        }

        fun cacheFile(init: Builder.() -> File) = apply {
            mCacheFile = init()
        }

        fun retrofitConfiguration(init: Builder.() -> ClientModule.RetrofitConfiguration) = apply {
            mRetrofitConfiguration = init()
        }

        fun okhttpConfiguration(init: Builder.() -> ClientModule.OkhttpConfiguration) = apply {
            mOkhttpConfiguration = init()
        }

        fun rxCacheConfiguration(init: Builder.() -> ClientModule.RxCacheConfiguration) = apply {
            mRxCacheConfiguration = init()
        }

        fun gsonConfiguration(init: Builder.() -> AppModule.GsonConfiguration) = apply {
            mGsonConfiguration = init()
        }

        fun roomConfiguration(init: Builder.() -> RoomConfiguration<*>) = apply {
            mRoomConfiguration = init()
        }

        fun printHttpLogLevel(init: Builder.() -> RequestInterceptor.Level) = apply {
            mPrintHttpLogLevel = init()
        }

        fun formatPrinter(init: Builder.() -> FormatPrinter) = apply {
            mFormatPrinter = init()
        }

        fun executorService(init: Builder.() -> ExecutorService) = apply {
            mExecutorService = init()
        }

        fun cacheFactory(init: Builder.() -> Cache.Factory) = apply {
            mCacheFactory = init()
        }

        fun build() = RepositoryConfigModule(this)
    }


}