package com.eugene.commonsdk.base.service

import android.app.Application
import android.content.Context

/**
 * 用于代理 {@link Application} 的生命周期
 */
interface IAppLifecycle {

    fun attachBaseContext(base: Context?)

    fun onCreate(base: Application)

    fun onTerminate(base: Application)

}

