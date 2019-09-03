package com.eugene.commonsdk.integration

import android.content.Context
import androidx.room.RoomDatabase

/**
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 {@link IModel} 必要的 Api 做数据处理
 *
 */
interface IRepositoryManager {

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T> obtainRetrofitService(service: Class<T>): T

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
    </T> */
    fun <T> obtainCacheService(cache: Class<T>): T

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param database RoomDatabase Class
     * @param <DB>     RoomDatabase
     * @param dbName   RoomDatabase name
     * @return RoomDatabase
    </DB> */
    fun <DB : RoomDatabase> obtainRoomDatabase(database: Class<DB>, dbName: String): DB

    /**
     * 清理所有缓存
     */
    fun clearAllCache()

    fun getContext(): Context


}