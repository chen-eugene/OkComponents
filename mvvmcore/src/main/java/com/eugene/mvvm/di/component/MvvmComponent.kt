package com.eugene.mvvm.di.component

import android.app.Application
import com.eugene.commonsdk.di.component.SdkComponent
import com.eugene.commonsdk.integration.AppManager
import com.eugene.commonsdk.integration.IRepositoryManager
import com.eugene.mvvm.di.module.ViewModelFactoryModule
import com.eugene.mvvm.di.scope.MvvmScope
import dagger.Component

@MvvmScope
@Component(dependencies = [SdkComponent::class], modules = [ViewModelFactoryModule::class])
interface MvvmComponent {

    fun application(): Application

    fun appManager(): AppManager?

    fun repositoryManager(): IRepositoryManager?

//    fun getViewModelFactory(): ViewModelProvider.Factory

}