package com.ztn.app.di.scope

import javax.inject.Scope

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 标记为 fragment 生命周期的注解
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope
