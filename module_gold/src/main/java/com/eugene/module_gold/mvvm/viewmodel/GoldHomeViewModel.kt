package com.eugene.module_gold.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.eugene.module_gold.mvvm.model.GoldModel
import com.eugene.module_gold.mvvm.model.entry.GoldListBean
import com.eugene.mvvm.mvvm.BaseViewModel
import javax.inject.Inject

class GoldHomeViewModel @Inject constructor(application: Application, model: GoldModel)
    : BaseViewModel<GoldModel>(application, model) {

    val mGoldListLiveData: MediatorLiveData<List<GoldListBean>> by lazy {
        MediatorLiveData<List<GoldListBean>>()
    }

    fun getGoldList(type: String, num: Int, page: Int) {
        val resource = mModel?.getGoldList(type, num, page)
        if (resource != null) {
            mGoldListLiveData.addSource(resource) {
                mGoldListLiveData.removeSource(resource)
                mGoldListLiveData.addSource(resource) {
                    mGoldListLiveData.value = it?.data
                }
            }
        }
    }
}