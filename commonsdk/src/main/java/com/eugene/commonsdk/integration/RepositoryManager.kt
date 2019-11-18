package com.eugene.commonsdk.integration

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eugene.commonsdk.integration.cache.Cache
import com.eugene.commonsdk.integration.cache.CacheType
import com.eugene.commonsdk.module.db.RoomConfiguration
import com.eugene.commonsdk.utils.Preconditions
import dagger.Lazy
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import io.rx_cache2.internal.RxCache
import retrofit2.Retrofit

import javax.inject.Inject
import javax.inject.Singleton
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.Callable

/**
 * ================================================
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 {IModel} 层必要的 Api 做数据处理
 *
 * @see [RepositoryManager wiki 官方文档](https://github.com/JessYanCoding/MVPArms/wiki.2.3)
 * Created by JessYan on 13/04/2017 09:52
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@Singleton
class RepositoryManager @Inject
constructor() : IRepositoryManager {

    @Inject
    internal var mRetrofit: Lazy<Retrofit>? = null
    @Inject
    internal var mRxCache: Lazy<RxCache>? = null
    @Inject
    internal var mApplication: Application? = null
    @Inject
    internal var mCacheFactory: Cache.Factory? = null
    private var mRetrofitServiceCache: Cache<String, Any>? = null
    private var mCacheServiceCache: Cache<String, Any>? = null
    private var mRoomDatabaseCache: Cache<String, Any>? = null
    private val mRoomConfiguration: RoomConfiguration<*>? = null

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
    </T> */
    @Synchronized
    override fun <T> obtainRetrofitService(serviceClass: Class<T>): T {
        return createWrapperService(serviceClass)
    }

    /**
     * 根据 https://zhuanlan.zhihu.com/p/40097338 对 Retrofit 进行的优化
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
    </T> */
    private fun <T> createWrapperService(serviceClass: Class<T>): T {
        // 通过二次代理，对 Retrofit 代理方法的调用包进新的 Observable 里在 io 线程执行。

        return Proxy.newProxyInstance(serviceClass.classLoader, arrayOf<Class<*>>(serviceClass),
                InvocationHandler { o, method, args ->
                    if (method.returnType == Observable<*>::class.java) {
                        // 如果方法返回值是 Observable 的话，则包一层再返回
                        return@InvocationHandler Observable.defer {
                            val service = getRetrofitService(serviceClass)
                            //                                // 执行真正的 Retrofit 动态代理的方法
                            (getRetrofitMethod(service!!, method)
                                    .invoke(service, *args) as Observable<*>)
                                    .subscribeOn(Schedulers.io())
                        }.subscribeOn(Schedulers.single())
                    }
                    // 返回值不是 Observable 的话不处理
                    val service = getRetrofitService(serviceClass)
                    getRetrofitMethod(service!!, method).invoke(service, *args)
                }) as T
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
    </T> */
    private fun <T> getRetrofitService(serviceClass: Class<T>): T? {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCacheFactory!!.build(CacheType.RETROFIT_SERVICE_CACHE)
        }
        Preconditions.checkNotNull<Cache<String, Any>>(mRetrofitServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method")
        var retrofitService = mRetrofitServiceCache!!.get(serviceClass.canonicalName) as T?
        if (retrofitService == null) {
            retrofitService = mRetrofit!!.get().create(serviceClass)
            mRetrofitServiceCache!!.put(serviceClass.canonicalName, retrofitService)
        }
        return retrofitService
    }

    @Throws(NoSuchMethodException::class)
    private fun <T> getRetrofitMethod(service: T, method: Method): Method {
        return service.javaClass.getMethod(method.name, *method.parameterTypes)
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cacheClass Cache class
     * @param <T>        Cache class
     * @return Cache
    </T> */
    @Synchronized
    override fun <T> obtainCacheService(cacheClass: Class<T>): T? {
        if (mCacheServiceCache == null) {
            mCacheServiceCache = mCacheFactory!!.build(CacheType.CACHE_SERVICE_CACHE)
        }
        Preconditions.checkNotNull<Cache<String, Any>>(mCacheServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method")
        var cacheService = mCacheServiceCache!!.get(cacheClass.canonicalName) as T?
        if (cacheService == null) {
            cacheService = mRxCache!!.get().using(cacheClass)
            mCacheServiceCache!!.put(cacheClass.canonicalName, cacheService)
        }
        return cacheService
    }

    override fun <DB : RoomDatabase> obtainRoomDatabase(database: Class<DB>, dbName: String): DB {
        if (mRoomDatabaseCache == null) {
            mRoomDatabaseCache = mCacheFactory!!.build(CacheType.ROOM_DATABASE_CACHE_TYPE)
        }
        Preconditions.checkNotNull<Cache<String, Any>>(mRoomDatabaseCache, "Cannot return null from a Cache.Factory#build(int) method")
        var roomDatabase: DB?
        synchronized(mRoomDatabaseCache) {
            roomDatabase = mRoomDatabaseCache!!.get(database.name) as DB?
            if (roomDatabase == null) {
                val builder = Room.databaseBuilder(mApplication!!, database, dbName)
                //自定义 Room 配置
                mRoomConfiguration?.configRoom(mApplication, builder)
                roomDatabase = builder.build()
                mRoomDatabaseCache!!.put(database.name, roomDatabase)
            }
        }
        return roomDatabase
    }

    /**
     * 清理所有缓存
     */
    override fun clearAllCache() {
        mRxCache!!.get().evictAll().subscribe()
    }

    override fun getContext(): Context {
        return mApplication
    }


}
