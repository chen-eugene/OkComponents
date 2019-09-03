package com.eugene.module_gold.di.componet

import com.eugene.commonsdk.di.component.SdkComponent
import com.eugene.module_gold.di.module.ActivityBuilder
import com.eugene.module_gold.di.module.ViewModelModule
import com.eugene.mvvm.di.component.MvvmComponent
import dagger.Component

@Component(dependencies = [MvvmComponent::class], modules = [ActivityBuilder::class, ViewModelModule::class])
interface GoldComponent {


    @Component.Builder
    interface Builder {
//        @BindsInstance
//        fun view(view: GoldHomeContract.View): GoldComponent.Builder

        fun appComponent(appComponent: SdkComponent): SdkComponent.Builder

        fun build(): GoldComponent
    }

}