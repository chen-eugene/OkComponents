package com.eugene.module_gold.di.module

import com.eugene.commonsdk.di.scope.ActivityScope
import com.eugene.module_gold.mvvm.view.activity.GoldHomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeGoldHomeActivity(): GoldHomeActivity


}