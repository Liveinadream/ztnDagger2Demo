package com.ztn.app.model.prefs

/**
 * Created by 冒险者ztn on 2019/2/15.
 * 介绍 todo
 */
interface PreferencesHelper {
    fun getNightModeState(): Boolean

    fun setNightModeState(state: Boolean)

    fun getNoImageState(): Boolean

    fun setNoImageState(state: Boolean)

    fun getAutoCacheState(): Boolean

    fun setAutoCacheState(state: Boolean)

    fun getCurrentItem(): Int

    fun setCurrentItem(item: Int)

    fun getLikePoint(): Boolean

    fun setLikePoint(isFirst: Boolean)

    fun getVersionPoint(): Boolean

    fun setVersionPoint(isFirst: Boolean)

    fun getManagerPoint(): Boolean

    fun setManagerPoint(isFirst: Boolean)
}