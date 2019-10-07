package com.ztn.app.base

/**
 * @author Jake.Ho
 * created: 2017/10/25
 * desc: Presenter 基类
 */

interface IPresenter<in T : BaseView> {

    fun attachView(mRootView: T?)

    fun detachView()

}
