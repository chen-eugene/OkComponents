package com.eugene.module_gold.mvvm.view.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.eugene.module_gold.R
import com.eugene.module_gold.mvvm.viewmodel.GoldHomeViewModel
import com.eugene.mvvm.base.BaseActivity

class GoldHomeActivity : BaseActivity<Nothing, GoldHomeViewModel>() {


    override fun initLayout(savedInstanceState: Bundle?) = R.layout.gold_activity_home_gold

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun registerObserver() {
        mViewModel?.mGoldListLiveData?.observe(this, Observer {

        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel = ViewModelProviders.of(this).get(GoldHomeViewModel::class.java)

        mViewModel?.getGoldList("", 8, 2)

    }

    override fun hideLoading() {
    }

    override fun showLoading() {
    }

}