package com.eugene.zhihu.di.component

import com.eugene.commonsdk.di.component.SdkComponent
import com.eugene.commonsdk.di.scope.ActivityScope
import com.eugene.zhihu.di.module.ZhihuHomeModule
import com.eugene.zhihu.mvp.contract.ZhihuHomeContract
import com.eugene.zhihu.mvp.ui.activity.ZhihuHomeActivity
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(modules = [ZhihuHomeModule::class], dependencies = [SdkComponent::class])
interface ZhihuHomeComponent {

    fun inject(activity: ZhihuHomeActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun view(view: ZhihuHomeContract.View): Builder

        fun appComponent(appComponent: SdkComponent): Builder

        fun build(): ZhihuHomeComponent
    }


}