package com.eugene.mvvm.mvvm

import android.app.Application
import com.eugene.commonsdk.integration.IRepositoryManager
import javax.inject.Inject

abstract class BaseModel : IModel {

    @set:Inject
    protected var mRepositoryManager: IRepositoryManager? = null

    @set:Inject
    protected var mApplication: Application? = null

//    init {
//        this.mRepositoryManager =
//    }


    override fun onDestroy() {
        mRepositoryManager = null
        mApplication = null
    }


}