package com.eugene.commonsdk.module.db;

import android.content.Context;
import androidx.room.RoomDatabase;

public interface RoomConfiguration<DB extends RoomDatabase> {
    /**
     * 提供接口，自定义配置 RoomDatabase
     *
     * @param context Context
     * @param builder RoomDatabase.Builder
     */
    void configRoom(Context context, RoomDatabase.Builder<DB> builder);

    RoomConfiguration EMPTY = new RoomConfiguration() {
        @Override
        public void configRoom(Context context, RoomDatabase.Builder builder) {

        }
    };
}
