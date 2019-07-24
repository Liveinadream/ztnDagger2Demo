package com.ztn.commom.ktExt

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

/**
 * Created by 冒险者ztn on 2019-07-24.
 * Rx 拓展函数
 */


infix fun Disposable.disposeBy(disposeBag: CompositeDisposable) = disposeBag.add(this)

inline infix fun <T> BehaviorSubject<T>.new(newValue: T) = this.onNext(newValue)

//这里模拟一个类的构造函数
inline fun <T> Variable(defaultValue: T? = null) =
    if (defaultValue == null) BehaviorSubject.create<T>() else BehaviorSubject.createDefault(defaultValue)

inline fun <T> Observable<T>.bind(variable: BehaviorSubject<T>) = this.subscribe { variable.onNext(it) }


//把频繁的变更整理到一个tick里面
fun <T> Observable<T>.nextTick(window: Int = 5) = this.debounce(window.toLong(), TimeUnit.MILLISECONDS)