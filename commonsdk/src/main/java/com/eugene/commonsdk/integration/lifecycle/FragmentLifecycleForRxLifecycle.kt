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
package com.eugene.commonsdk.integration.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ================================================
 * 配合 [IFragmentLifecycle] 使用,使 [Fragment] 具有 [RxLifecycle] 的特性
 *
 *
 * Created by JessYan on 26/08/2017 16:02
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
@Singleton
class FragmentLifecycleForRxLifecycle @Inject constructor() : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.ATTACH)
        }
    }


    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.CREATE)
        }
    }


    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.CREATE_VIEW)
        }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.START)
        }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.RESUME)
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.PAUSE)
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.STOP)
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.DESTROY_VIEW)
        }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.DESTROY)
        }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        if (f is IFragmentLifecycle) {
            obtainSubject(f).onNext(FragmentEvent.DETACH)
        }
    }

    private fun obtainSubject(fragment: Fragment): Subject<FragmentEvent> {
        return (fragment as IFragmentLifecycle).provideLifecycleSubject()
    }
}
