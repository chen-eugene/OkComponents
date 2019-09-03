package com.eugene.module_gold.app

import android.app.Application
import android.content.Context
import com.eugene.commonsdk.base.service.IAppLifecycle

/**
 * 展示 {@link IAppLifecycle} 的用法
 */
class AppLifecycleImpl : IAppLifecycle {

    override fun attachBaseContext(base: Context?) {
    }

    override fun onCreate(base: Application) {
    }

    override fun onTerminate(base: Application) {
    }

}