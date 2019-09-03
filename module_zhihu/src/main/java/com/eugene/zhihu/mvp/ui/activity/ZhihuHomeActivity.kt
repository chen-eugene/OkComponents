/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eugene.zhihu.mvp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.alibaba.android.arouter.facade.annotation.Route
import com.eugene.commonsdk.base.DefAdapter
import com.eugene.commonsdk.utils.ArmsUtils
import com.eugene.mvpcore.base.BaseActivity
import com.eugene.commonservice.core.RouterHub
import com.eugene.zhihu.R
import com.eugene.zhihu.mvp.contract.ZhihuHomeContract
import com.eugene.zhihu.mvp.presenter.ZhihuHomePresenter

import javax.inject.Inject

import timber.log.Timber


/**
 * ================================================
 * 展示 View 的用法
 *
 * @see [View wiki 官方文档](https://github.com/JessYanCoding/MVPArms/wiki.2.4.2)
 * Created by JessYan on 09/04/2016 10:59
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@Route(path = RouterHub.ZHIHU_HOMEACTIVITY)
class ZhihuHomeActivity : BaseActivity<ZhihuHomePresenter>(), ZhihuHomeContract.View, SwipeRefreshLayout.OnRefreshListener {


    internal var mRecyclerView: RecyclerView? = null
    internal var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    @set:Inject
    internal var mLayoutManager: RecyclerView.LayoutManager? = null
    @set:Inject
    internal var mAdapter: RecyclerView.Adapter<*>? = null

    override fun injectable(): Boolean {
        return true
    }

//    override fun setupActivityComponent(appComponent: SdkComponent) {
//        DaggerZhihuHomeComponent
//                .builder()
//                .appComponent(appComponent)
//                .view(this)
//                .build()
//                .inject(this)
//    }

    override fun initLayout(savedInstanceState: Bundle?): Int {
        return R.layout.zhihu_activity_home
    }

    override fun initView(savedInstanceState: Bundle?) {
        initRecyclerView()
        mRecyclerView!!.adapter = mAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun onRefresh() {
        mPresenter?.requestDailyList()
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRecyclerView() {
        mSwipeRefreshLayout?.setOnRefreshListener(this)
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager)
    }


    override fun showLoading() {
        Timber.tag(TAG).w("showLoading")
        mSwipeRefreshLayout?.isRefreshing = true
    }

    override fun hideLoading() {
        Timber.tag(TAG).w("hideLoading")
        mSwipeRefreshLayout?.isRefreshing = false
    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        ArmsUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun onDestroy() {
        DefAdapter.releaseAllHolder(mRecyclerView)//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy()
    }
}
