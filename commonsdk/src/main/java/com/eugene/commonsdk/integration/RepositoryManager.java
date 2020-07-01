package com.eugene.commonsdk.integration;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.eugene.commonsdk.integration.cache.Cache;
import com.eugene.commonsdk.integration.cache.CacheType;
import com.eugene.commonsdk.module.db.RoomConfiguration;
import com.eugene.commonsdk.utils.Preconditions;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * @author xiaobailong24
 * @date 2017/9/28
 * 数据管理层实现类
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {
    private Application mApplication;
    private Lazy<Retrofit> mRetrofit;
    private Lazy<RxCache> mRxCache;
    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;
    private Cache<String, Object> mRoomDatabaseCache;
    private final Cache.Factory mCacheFactory;
    private RoomConfiguration mRoomConfiguration;

    @Inject
    public RepositoryManager(Application application, Lazy<Retrofit> retrofit, Lazy<RxCache> rxCache,
                             Cache.Factory cacheFactory, RoomConfiguration roomConfiguration) {
        this.mApplication = application;
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
        this.mCacheFactory = cacheFactory;
        this.mRoomConfiguration = roomConfiguration;
    }

    @Override
    public <T> T obtainRetrofitService(Class<T> service) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCacheFactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mRetrofitServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService;
        synchronized (mRetrofitServiceCache) {
            retrofitService = (T) mRetrofitServiceCache.get(service.getName());
            if (retrofitService == null) {
                retrofitService = mRetrofit.get().create(service);
                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }


    @Override
    public <T> T obtainCacheService(Class<T> cache) {
        if (mCacheServiceCache == null) {
            mCacheServiceCache = mCacheFactory.build(CacheType.CACHE_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mCacheServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService;
        synchronized (mCacheServiceCache) {
            cacheService = (T) mCacheServiceCache.get(cache.getName());
            if (cacheService == null) {
                cacheService = mRxCache.get().using(cache);
                mCacheServiceCache.put(cache.getName(), cacheService);
            }
        }
        return cacheService;
    }

    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }

    @Override
    public Context getContext() {
        return this.mApplication;
    }

    @Override
    public <DB extends RoomDatabase> DB obtainRoomDatabase(Class<DB> database, String dbName) {
        if (mRoomDatabaseCache == null) {
            mRoomDatabaseCache = mCacheFactory.build(CacheType.ROOM_DATABASE_CACHE);
        }
        Preconditions.checkNotNull(mRoomDatabaseCache, "Cannot return null from a Cache.Factory#build(int) method");
        DB roomDatabase;
        synchronized (mRoomDatabaseCache) {
            roomDatabase = (DB) mRoomDatabaseCache.get(database.getName());
            if (roomDatabase == null) {
                RoomDatabase.Builder builder = Room.databaseBuilder(mApplication, database, dbName);
                //自定义 Room 配置
                if (mRoomConfiguration != null) {
                    mRoomConfiguration.configRoom(mApplication, builder);
                }
                roomDatabase = (DB) builder.build();
                mRoomDatabaseCache.put(database.getName(), roomDatabase);
            }
        }
        return roomDatabase;
    }
}
