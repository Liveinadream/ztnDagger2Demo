package com.ztn.app.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
open class BasePresenter<T : BaseView> : IPresenter<T> {

    var mRootView: Reference<T>? = null
        private set

    private var compositeDisposable = CompositeDisposable()


    override fun  attachView(mRootView: T) {
        this.mRootView = WeakReference<T>(mRootView)
    }

    override fun detachView() {
        mRootView = null

        //保证activity结束时取消所有正在执行的订阅
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }

    }

    private val isViewAttached: Boolean
        get() = mRootView != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private class MvpViewNotAttachedException internal constructor() :
        RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")
}

