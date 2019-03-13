package com.ztn.app.ui.chat

import com.ztn.app.R
import com.ztn.app.base.BaseActivity

/**
 * Created by 冒险者ztn on 2019/3/13.
 */
class FriendActivity : BaseActivity<FriendPresenter>(), FriendContract.View {

    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun getLayout(): Int {
        return R.layout.activity_files
    }

    override fun initEventAndData() {
        mPresenter.attachView(this)
    }

    override fun loading() {
    }

    override fun dismissLoading() {
    }

    override fun showChatList() {
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }
}