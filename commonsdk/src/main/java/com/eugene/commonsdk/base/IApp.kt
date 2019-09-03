package com.eugene.commonsdk.base

import com.eugene.commonsdk.di.component.SdkComponent

interface IApp {

    fun getArmComponent(): SdkComponent?

}