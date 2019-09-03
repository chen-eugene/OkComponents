package com.eugene.module_gold.di.module

import androidx.lifecycle.ViewModelProvider
import com.eugene.mvvm.mvvm.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

//    @Binds
//    @IntoMap
//    @ViewModelMapKey(UserViewModel::class)
//    abstract fun userViewModel(vm: UserViewModel): ViewModel


}