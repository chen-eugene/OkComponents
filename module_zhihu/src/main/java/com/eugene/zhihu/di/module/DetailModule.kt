package com.eugene.zhihu.di.module

import android.app.Dialog
import com.eugene.commonres.dialog.ProgressDialog
import com.eugene.commonsdk.di.scope.ActivityScope
import com.eugene.zhihu.mvp.contract.DetailContract
import com.eugene.zhihu.mvp.model.ZhihuModel
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [DetailModule.SubModule::class])
class DetailModule {

    @ActivityScope
    @Provides
    internal fun provideDialog(view: DetailContract.View): Dialog {
        return ProgressDialog(view.getActivity())
    }

    @Module
    interface SubModule {

        @Binds
        fun bindZhihuModel(model: ZhihuModel): DetailContract.Model

    }

}