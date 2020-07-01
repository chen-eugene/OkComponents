package com.eugene.module_gold.di.module

import com.eugene.commonsdk.di.scope.ActivityScope
import com.eugene.module_gold.mvvm.view.activity.GoldHomeActivity
import com.eugene.mvvm.di.module.ViewModelFactoryModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelFactoryModule::class])
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeGoldHomeActivity(): GoldHomeActivity


}