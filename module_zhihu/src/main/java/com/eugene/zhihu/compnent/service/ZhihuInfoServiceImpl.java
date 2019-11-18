package com.eugene.zhihu.compnent.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eugene.commonsdk.utils.ArmsUtils;
import com.eugene.commonservice.core.RouterHub;
import com.eugene.commonservice.zhuhu.bean.ZhihuInfo;
import com.eugene.commonservice.zhuhu.service.ZhihuInfoService;

/**
 * ================================================
 * 向外提供服务的接口实现类, 在此类中实现一些具有特定功能的方法提供给外部, 即可让一个组件与其他组件或宿主进行交互
 *
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#2.2.3.2">CommonService wiki 官方文档</a>
 * Created by JessYan on 2018/4/27 14:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Route(path = RouterHub.ZHIHU_SERVICE_ZHIHUINFOSERVICE, name = "知乎信息服务")
public class ZhihuInfoServiceImpl implements ZhihuInfoService {
    private Context mContext;

    @Override
    public ZhihuInfo getInfo() {
//        return new ZhihuInfo(ArmsUtils.getString(mContext, R.string.public_name_zhihu));
        return new ZhihuInfo(ArmsUtils.getString(mContext, "知乎日报"));
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
