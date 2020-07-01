package com.eugene.commonsdk.di.module

import android.app.Application
import android.content.Context
import android.net.sip.SipErrorCode.TIME_OUT
import com.eugene.commonsdk.module.network.GlobalHttpHandler
import com.eugene.commonsdk.module.network.log.RequestInterceptor
import com.eugene.commonsdk.utils.DataHelper
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener
import okhttp3.Dispatcher
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * 提供一些三方库客户端实例的 {@link Module}
 */

@Module(includes = [ClientModule.SubModule::class])
class ClientModule {

    @Singleton
    @Provides
    internal fun provideRetrofit(application: Application, configuration: RetrofitConfiguration?,
                                 builder: Retrofit.Builder, client: OkHttpClient,
                                 httpUrl: HttpUrl?, gson: Gson): Retrofit {
        builder.baseUrl(httpUrl)//域名
                .client(client)//设置okhttp

        configuration?.configRetrofit(application, builder)

        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 Rxjava
                .addConverterFactory(GsonConverterFactory.create(gson))//使用 Gson
        return builder.build()
    }


    @Singleton
    @Provides
    internal fun provideClient(application: Application, configuration: OkhttpConfiguration?,
                               builder: OkHttpClient.Builder, intercept: Interceptor,
                               interceptors: ArrayList<Interceptor>?, handler: GlobalHttpHandler?,
                               executorService: ExecutorService): OkHttpClient {
        builder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept)

        if (handler != null)
            builder.addInterceptor { chain -> chain.proceed(handler.onHttpRequestBefore(chain, chain.request())) }

        //如果外部提供了interceptor的集合则遍历添加
        interceptors?.forEach {
            builder.addInterceptor(it)
        }

        // 为 OkHttp 设置默认的线程池。
        builder.dispatcher(Dispatcher(executorService))

        configuration?.configOkhttp(application, builder)

        return builder.build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Singleton
    @Provides
    internal fun provideClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @Singleton
    @Provides
    internal fun provideRxCache(application: Application, configuration: RxCacheConfiguration?, @Named("RxCacheDirectory") cacheDirectory: File, gson: Gson): RxCache {
        val builder = RxCache.Builder()
        var rxCache: RxCache? = null
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder)
        }
        return if (rxCache != null) rxCache else builder
                .persistence(cacheDirectory, GsonSpeaker(gson))
    }

    /**
     * 需要单独给 {@link RxCache} 提供缓存路径
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    internal fun provideRxCacheDirectory(cacheDir: File): File {
        val cacheDirectory = File(cacheDir, "RxCache")
        return DataHelper.makeDirs(cacheDirectory)
    }

    /**
     * 提供处理 RxJava 错误的管理器
     */
    @Singleton
    @Provides
    internal fun proRxErrorHandler(application: Application, listener: ResponseErrorListener): RxErrorHandler {
        return RxErrorHandler
            .builder()
            .with(application)
            .responseErrorListener(listener)
            .build()
    }

    @Module
    interface SubModule {
        @Binds
        fun bindInterceptor(interceptor: RequestInterceptor): Interceptor
    }

    interface RetrofitConfiguration {
        fun configRetrofit(context: Context, builder: Retrofit.Builder)
    }

    interface OkhttpConfiguration {
        fun configOkhttp(context: Context, builder: OkHttpClient.Builder)
    }

    interface RxCacheConfiguration {
        /**
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson
         * 请 `return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());`, 否则请 `return null;`
         */
        fun configRxCache(context: Context, builder: RxCache.Builder): RxCache?
    }
}