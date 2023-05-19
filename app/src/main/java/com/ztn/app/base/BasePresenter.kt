package com.ztn.app.base

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by 冒险者ztn on 2019/2/12.
 * Presenter
 */
open class BasePresenter<T : BaseView> : IPresenter<T> {
    private var mRootView: Reference<T>? = null

    protected open fun getRootView(): T? {
        // Maybe this method is being used by non-UI thread
        val theHolder: Reference<T>? = mRootView
        return theHolder?.get()
    }

    protected open fun requireRootView(): T {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return Objects.requireNonNull(getRootView())!!
        } else {
            throw java.lang.RuntimeException("Should In main thread")
        }
    }


    var compositeDisposable = CompositeDisposable()


    override fun attachView(mRootView: T?) {
        this.mRootView = WeakReference(mRootView)
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

    protected open fun getRootViewLifeCycle(): Context? {
        val theView: T? = getRootView()
        if (theView != null) {
            when (theView) {
                is Activity -> {
                    return theView
                }
                is Fragment -> {
                    val fragment: Fragment = theView
                    return if (fragment.isAdded) {
                        fragment.activity
                    } else {
                        null
                    }
                }
                is View -> {
                    var context = theView.context
                    while (context is ContextWrapper) {
                        if (context is Activity) {
                            return context
                        }
                        context = context.baseContext
                    }
                }
            }
        }
        return null
    }

    private class MvpViewNotAttachedException internal constructor() :
        RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")
}

