package com.eugene.module_gold.mvvm.model

import com.eugene.module_gold.app.LEANCLOUD_ID
import com.eugene.module_gold.app.LEANCLOUD_SIGN
import com.eugene.module_gold.mvvm.model.api.service.GoldService
import com.eugene.module_gold.mvvm.model.entry.GoldBaseResponse
import com.eugene.module_gold.mvvm.model.entry.GoldListBean
import com.eugene.mvvm.mvvm.BaseModel
import io.reactivex.Observable
import javax.inject.Inject

class GoldModel @Inject constructor() : BaseModel() {

    fun getGoldList(type: String, num: Int, page: Int): Observable<GoldBaseResponse<List<GoldListBean>>>? {
        return mRepositoryManager
                ?.obtainRetrofitService(GoldService::class.java)
                ?.getGoldList(LEANCLOUD_ID, LEANCLOUD_SIGN,
                        "{\"category\":\"$type\"}", "-createdAt", "user,user.installation", num, page * num)
    }


}