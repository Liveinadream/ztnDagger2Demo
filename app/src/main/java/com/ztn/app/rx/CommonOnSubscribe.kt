package com.ztn.app.rx

import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

abstract class CommonOnSubscribe<T> : ObservableOnSubscribe<T> {

    abstract fun work(e: ObservableEmitter<T>)

    @Throws(Exception::class)
    override fun subscribe(e: ObservableEmitter<T>) {
        if (!e.isDisposed) {
            work(e)
        }
    }
}
