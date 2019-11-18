package com.eugene.commonsdk.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxUtil {

    companion object {

        private val schedulersTransformer by lazy {
            ObservableTransformer<Any, Any> {
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun clicks(view: View?): Observable<String>? {
            view ?: return null
            return ViewClickObservable(view)
                .throttleFirst(200, TimeUnit.MILLISECONDS)  // 才发送 2s内第1次点击按钮的事件
        }

        fun textChange(view: TextView?): Observable<String> {
            return TextChangeObservable(view).debounce(1, TimeUnit.SECONDS)
        }


        fun <T> applySchedulers(): ObservableTransformer<T, T> {
            return schedulersTransformer as ObservableTransformer<T, T>
        }

    }

}

class ViewClickObservable(val view: View?) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>?) {
        view?.setOnClickListener {
            observer?.onNext("onClick")
        }
    }

}

class TextChangeObservable(val view: TextView?) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>?) {
        view?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                observer?.onNext(s.toString())
            }
        })
    }

}
