package com.eugene.zhihu.mvp.ui.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.eugene.commonsdk.utils.ArmsUtils
import com.eugene.commonsdk.utils.HtmlUtil
import com.eugene.mvpcore.base.BaseActivity
import com.eugene.commonservice.core.RouterHub
import com.eugene.zhihu.R
import com.eugene.zhihu.app.ZhihuConstants
import com.eugene.zhihu.mvp.contract.DetailContract
import com.eugene.zhihu.mvp.model.entity.ZhihuDetailBean
import com.eugene.zhihu.mvp.presenter.DetailPresenter
import kotlinx.android.synthetic.main.zhihu_activity_detail.*
import javax.inject.Inject

@Route(path = RouterHub.ZHIHU_DETAILACTIVITY)
class DetailActivity : BaseActivity<DetailPresenter>(), DetailContract.View {

    @set:Inject
    var mDialog: Dialog? = null

//    override fun setupActivityComponent(appComponent: AppComponent) {
//        DaggerDetailComponent //如找不到该类,请编译一下项目
//                .builder()
//                .appComponent(appComponent)
//                .view(this)
//                .build()
//                .inject(this)
//    }

    override fun injectable(): Boolean {
        return true
    }

    override fun initLayout(savedInstanceState: Bundle?): Int {
        return R.layout.zhihu_activity_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        initWebView()
        loadTitle()
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter?.requestDetailInfo(intent.getIntExtra(ZhihuConstants.DETAIL_ID, 0))

    }

    private fun initWebView() {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.setSupportZoom(true)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }

    private fun loadTitle() {
        var title = intent.getStringExtra(ZhihuConstants.DETAIL_TITLE)
        if (title.length > 10) {
            title = title.substring(0, 10) + " ..."
        }
        setTitle(title)
    }

    override fun shonContent(bean: ZhihuDetailBean) {
        val htmlData = HtmlUtil.createHtmlData(bean.body, bean.css, bean.js)
        webView.loadData(htmlData, HtmlUtil.MIME_TYPE, ZhihuConstants.ENCODING)
    }

    override fun showMessage(message: String) {
        checkNotNull(message)
        ArmsUtils.snackbarText(message)
    }

    override fun killMyself() {
        finish()
    }

    override fun getActivity(): Activity {
        return this
    }


}