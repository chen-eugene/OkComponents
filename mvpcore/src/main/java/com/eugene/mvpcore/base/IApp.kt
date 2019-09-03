package com.eugene.mvpcore.base

import com.eugene.commonsdk.di.component.SdkComponent

interface IApp {

    fun getAppComponent(): SdkComponent

}