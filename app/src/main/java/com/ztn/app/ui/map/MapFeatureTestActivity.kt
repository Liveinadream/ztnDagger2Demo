package com.ztn.app.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.ViewModelProviders
import com.gomap.sdk.Mapbox
import com.gomap.sdk.camera.CameraPosition
import com.gomap.sdk.camera.CameraUpdateFactory
import com.gomap.sdk.geometry.LatLng
import com.gomap.sdk.location.LocationComponent
import com.gomap.sdk.location.LocationComponentActivationOptions
import com.gomap.sdk.location.LocationComponentOptions
import com.gomap.sdk.location.engine.LocationEngineCallback
import com.gomap.sdk.location.engine.LocationEngineResult
import com.gomap.sdk.maps.MapboxMap
import com.gomap.sdk.maps.OnMapReadyCallback
import com.gomap.sdk.maps.Style
import com.ztn.app.BR
import com.ztn.app.R
import com.ztn.app.base.AppViewModelFactory
import com.ztn.app.databinding.ActivityMapBinding
import com.ztn.common.ToastHelper
import com.ztn.library.rx.permission.PermissionSettings
import com.ztn.library.rx.permission.PermissionUtils
import com.ztn.library.rx.permission.PermissionUtils.RequestResult
import com.ztn.library.rx.permission.runtime.Permission.ACCESS_BACKGROUND_LOCATION
import com.ztn.library.rx.permission.runtime.Permission.ACCESS_COARSE_LOCATION
import com.ztn.library.rx.permission.runtime.Permission.ACCESS_FINE_LOCATION
import me.goldze.mvvmhabit.base.BaseActivity
import java.lang.Exception


/**
 * Created by 冒险者ztn on 2023/5/19 17:42
 */
class MapFeatureTestActivity : BaseActivity<ActivityMapBinding, MapViewModel>(),
    LocationEngineCallback<LocationEngineResult> {
    private lateinit var mapboxMap: MapboxMap
    private val CENTER = LatLng(24.4628, 54.3697)


    //ActivityLoginBinding类是 databinding 框架自定生成的,对activity_map.xml
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_map
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }


    override fun initViewModel(): MapViewModel {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        return ViewModelProviders.of(
            this,
            AppViewModelFactory.getInstance(application)
        )[MapViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.init(this)
        binding.mapView.onCreate(savedInstanceState)
        PermissionUtils.requestPermission(
            this, object : RequestResult {
                override fun onGranted(permissions: MutableList<String>?) {
                    initMap()
                    print("onGranted")
                }

                override fun onDenied(permissions: MutableList<String>?) {
                    ToastHelper.showToast("您未授予位置权限")
                    finish()
                    print("onDenied")
                }

                override fun onDeniedNotAsk(permissionSettings: PermissionSettings?) {
                    ToastHelper.showToast("位置权限已关闭，请在设置中打开")
                    print("onDeniedNotAsk")
                }

            },
            ACCESS_FINE_LOCATION
//            ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION
        )
    }

    override fun onStart() {
        super.onStart()
//        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
//        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    @SuppressLint("MissingPermission")
    fun initMap() {
        binding.mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
            this@MapFeatureTestActivity.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.getLocalStyle("Streets"), object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {
                    print("onStyleLoaded")
                    val styles = Style.getPredefinedStyles()
                    if (styles.isNotEmpty()) {
//                        val styleUrl = styles[0].url
//                        mapboxMap.setStyle(Style.Builder().fromUri(styleUrl))
                        val cameraPosition = CameraPosition.Builder()
                            .target(CENTER)
                            .zoom(10.0)
                            .tilt(30.0)
                            .tilt(0.0)
                            .build()
                        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                        val component: LocationComponent = mapboxMap.locationComponent
                        val locationComponentOptions =
                            LocationComponentOptions.builder(this@MapFeatureTestActivity)
                                .foregroundDrawable(R.drawable.ic_launcher_foreground)
                                .backgroundDrawable(R.drawable.ic_launcher_foreground)
                                .bearingDrawable(R.drawable.ic_launcher_foreground)
                                .pulseEnabled(true)
                                .pulseMaxRadius(50f)
                                .minZoomIconScale(0.6f)
                                .maxZoomIconScale(1.0f)
                                .pulseColor(Color.parseColor("#4B7DF6"))
                                .build()
                        val locationComponentActivationOptions = LocationComponentActivationOptions
                            .builder(this@MapFeatureTestActivity, style)
                            .locationComponentOptions(locationComponentOptions)
                            .useDefaultLocationEngine(true)
                            .build()
                        component.activateLocationComponent(
                            locationComponentActivationOptions
                        )
                        component.isLocationComponentEnabled = true
                        component.locationEngine?.getLastLocation(this@MapFeatureTestActivity)
                    }
                }
            })
        })
    }

    companion object {
        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, MapFeatureTestActivity::class.java))
        }
    }

    override fun onSuccess(result: LocationEngineResult?) {
        if (!binding.mapView.isDestroyed) mapboxMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(result?.lastLocation),
                12.0
            )
        )

    }

    override fun onFailure(p0: Exception) {
    }

}

