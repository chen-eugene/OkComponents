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
package com.eugene.mvpcore.utils;

import com.eugene.commonsdk.integration.lifecycle.IActivityLifecycle;
import com.eugene.commonsdk.integration.lifecycle.IFragmentLifecycle;
import com.eugene.commonsdk.integration.lifecycle.ILifecycle;
import com.eugene.commonsdk.utils.Preconditions;
import com.eugene.mvpcore.mvp.IView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.annotations.NonNull;

/**
 * ================================================
 * 使用此类操作 RxLifecycle 的特性
 * <p>
 * Created by JessYan on 26/08/2017 17:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */

public class RxLifecycleUtils {

    private RxLifecycleUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 绑定 Activity 的指定生命周期
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final ActivityEvent event) {
        Preconditions.checkNotNull(view, "view == null");
        if (view instanceof IActivityLifecycle) {
            return bindUntilEvent((IActivityLifecycle) view, event);
        } else {
            throw new IllegalArgumentException("view isn't ActivityLifecycleable");
        }
    }

    /**
     * 绑定 Fragment 的指定生命周期
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final FragmentEvent event) {
        Preconditions.checkNotNull(view, "view == null");
        if (view instanceof IFragmentLifecycle) {
            return bindUntilEvent((IFragmentLifecycle) view, event);
        } else {
            throw new IllegalArgumentException("view isn't FragmentLifecycleable");
        }
    }

    public static <T, R> LifecycleTransformer<T> bindUntilEvent(@NonNull final ILifecycle<R> lifecycleable,
                                                                final R event) {
        Preconditions.checkNotNull(lifecycleable, "lifecycleable == null");
        return RxLifecycle.bindUntilEvent(lifecycleable.provideLifecycleSubject(), event);
    }


    /**
     * 绑定 Activity/Fragment 的生命周期
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull IView view) {
        Preconditions.checkNotNull(view, "view == null");
        if (view instanceof ILifecycle) {
            return bindToLifecycle((ILifecycle) view);
        } else {
            throw new IllegalArgumentException("view isn't ILifecycle");
        }
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull ILifecycle lifecycle) {
        Preconditions.checkNotNull(lifecycle, "lifecycleable == null");
        if (lifecycle instanceof IActivityLifecycle) {
            return RxLifecycleAndroid.bindActivity(((IActivityLifecycle) lifecycle).provideLifecycleSubject());
        } else if (lifecycle instanceof IFragmentLifecycle) {
            return RxLifecycleAndroid.bindFragment(((IFragmentLifecycle) lifecycle).provideLifecycleSubject());
        } else {
            throw new IllegalArgumentException("Lifecycleable not match");
        }
    }

}
