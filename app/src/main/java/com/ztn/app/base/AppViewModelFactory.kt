package com.ztn.app.base

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ztn.app.data.DemoRepository
import com.ztn.app.ui.login.LoginViewModel
import com.ztn.app.ui.map.MapViewModel
import com.ztn.app.ui.user.UserInfoViewModel

/**
 * Created by goldze on 2019/3/26.
 */
class AppViewModelFactory private constructor(
    private val mApplication: Application,
    private val mRepository: DemoRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(mApplication, mRepository) as T
        } else if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            return UserInfoViewModel(mApplication, mRepository) as T
        } else if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(mApplication, mRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): AppViewModelFactory? {
            if (INSTANCE == null) {
                synchronized(AppViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            AppViewModelFactory(application, Injection.provideDemoRepository())
                    }
                }
            }
            return INSTANCE
        }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}