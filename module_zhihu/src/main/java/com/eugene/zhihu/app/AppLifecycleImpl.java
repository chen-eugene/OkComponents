package com.eugene.zhihu.app;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import com.eugene.commonsdk.base.service.IAppLifecycle;
import com.eugene.commonsdk.integration.cache.IntelligentCache;
import com.eugene.commonsdk.utils.ArmsUtils;
import com.eugene.zhihu.BuildConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

import static com.eugene.zhihu.mvp.model.api.Api.ZHIHU_DOMAIN;
import static com.eugene.zhihu.mvp.model.api.Api.ZHIHU_DOMAIN_NAME;

/**
 * ================================================
 * 展示 {@link IAppLifecycle} 的用法
 * ================================================
 */
public class AppLifecycleImpl implements IAppLifecycle {

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //使用 RetrofitUrlManager 切换 BaseUrl
        RetrofitUrlManager.getInstance().putDomain(ZHIHU_DOMAIN_NAME, ZHIHU_DOMAIN);
        //当所有模块集成到宿主 App 时, 在 App 中已经执行了以下代码
        if (BuildConfig.IS_APP) {
            //leakCanary内存泄露检查
            ArmsUtils.obtainSdkComponentFromContext(application).extras()
                    .put(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())
                            , BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
