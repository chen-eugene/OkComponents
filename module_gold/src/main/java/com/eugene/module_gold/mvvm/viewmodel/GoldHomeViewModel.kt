package com.eugene.module_gold.mvvm.viewmodel

import android.app.Application
import com.eugene.module_gold.mvvm.model.GoldModel
import com.eugene.mvvm.mvvm.BaseViewModel
import javax.inject.Inject

class GoldHomeViewModel @Inject constructor(application: Application, model: GoldModel)
    : BaseViewModel<GoldModel>(application, model) {

//    val mGoldListLiveData:MediatorLiveData<>

    fun getGoldList(pullToRefresh: Boolean) {

    }


}