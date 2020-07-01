package com.eugene.module_gold.di.module

import androidx.lifecycle.ViewModel
import com.eugene.module_gold.mvvm.viewmodel.GoldHomeViewModel
import com.eugene.mvvm.di.module.ViewModelFactoryModule
import com.eugene.mvvm.di.scope.ViewModelMapKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelMapKey(GoldHomeViewModel::class)
    abstract fun userViewModel(vm: GoldHomeViewModel): ViewModel


}