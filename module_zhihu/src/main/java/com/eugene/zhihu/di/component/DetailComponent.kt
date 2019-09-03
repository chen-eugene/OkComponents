package com.eugene.zhihu.di.component

import com.eugene.commonsdk.di.component.SdkComponent
import com.eugene.commonsdk.di.scope.ActivityScope
import com.eugene.zhihu.di.module.DetailModule
import com.eugene.zhihu.mvp.contract.DetailContract
import com.eugene.zhihu.mvp.ui.activity.DetailActivity
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(modules = [DetailModule::class], dependencies = [SdkComponent::class])
interface DetailComponent {

    fun inject(activity: DetailActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun view(view: DetailContract.View): Builder

        fun appComponent(appComponent: SdkComponent): Builder

        fun build(): DetailComponent

    }


}