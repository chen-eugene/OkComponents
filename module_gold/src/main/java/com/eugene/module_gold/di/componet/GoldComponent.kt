package com.eugene.module_gold.di.componet

import android.app.Application
import com.eugene.commonsdk.base.BaseApplication
import com.eugene.commonsdk.di.component.SdkComponent
import com.eugene.commonsdk.integration.AppManager
import com.eugene.commonsdk.integration.IRepositoryManager
import com.eugene.module_gold.di.module.ActivityBuilder
import com.eugene.module_gold.di.module.ViewModelModule
import com.eugene.module_gold.di.scope.GoldScope
import dagger.Component

@GoldScope
@Component(dependencies = [SdkComponent::class], modules = [ActivityBuilder::class, ViewModelModule::class])
interface GoldComponent {

    fun application(): Application

    fun appManager(): AppManager?

    fun repositoryManager(): IRepositoryManager?

    fun inject(base: BaseApplication)

}