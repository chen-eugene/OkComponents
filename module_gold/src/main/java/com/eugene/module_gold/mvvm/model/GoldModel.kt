package com.eugene.module_gold.mvvm.model

import androidx.lifecycle.MutableLiveData
import com.eugene.commonsdk.utils.RxUtil
import com.eugene.module_gold.app.ErrorHandleSubscriber
import com.eugene.module_gold.app.LEANCLOUD_ID
import com.eugene.module_gold.app.LEANCLOUD_SIGN
import com.eugene.module_gold.mvvm.model.api.service.GoldService
import com.eugene.module_gold.mvvm.model.entry.BaseResp
import com.eugene.module_gold.mvvm.model.entry.GoldListBean
import com.eugene.mvvm.mvvm.BaseModel
import io.reactivex.Observable
import javax.inject.Inject

class GoldModel @Inject constructor() : BaseModel() {


    fun getGoldList(type: String, num: Int, page: Int): MutableLiveData<BaseResp<List<GoldListBean>>>? {
        val mGoldListLiveData = MutableLiveData<BaseResp<List<GoldListBean>>>()
        mRepositoryManager
                ?.obtainRetrofitService(GoldService::class.java)
                ?.getGoldList(LEANCLOUD_ID, LEANCLOUD_SIGN,
                        "{\"category\":\"$type\"}", "-createdAt", "user,user.installation", num, page * num)
                ?.compose(RxUtil.applySchedulers())
                ?.subscribe(object : ErrorHandleSubscriber<List<GoldListBean>>() {

                    override fun onSuccess(t: List<GoldListBean>?) {
                        mGoldListLiveData.value = BaseResp.success(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        //弱出现网络错误等  直接跳转tag
                        if (e.message?.isNotBlank() == true)
                            mGoldListLiveData.value = BaseResp.error(e.message!!, null)
                    }

                })

        return mGoldListLiveData
    }


}