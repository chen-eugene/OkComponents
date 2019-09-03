package com.eugene.mvvm.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eugene.mvvm.di.scope.MvvmScope
import com.eugene.mvvm.mvvm.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module(includes = [ViewModelFactoryModule.SubModule::class])
class ViewModelFactoryModule {

    @MvvmScope
    @Provides
    fun provideMap(): Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>> {
        return mapOf()
    }

    @Module
    interface SubModule {

        @MvvmScope
        @Binds
        abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

//        @Multibinds
//        abstract fun provideMap():  Map<Class<ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>

    }

}