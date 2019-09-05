package com.eugene.mvvm.di.module

import androidx.lifecycle.ViewModelProvider
import com.eugene.mvvm.mvvm.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}