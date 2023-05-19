package com.ztn.app.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yhao.floatwindow.PermissionListener
import com.yhao.floatwindow.ViewStateListener
import com.ztn.app.R
import com.ztn.app.ui.file.FileActivity
import com.ztn.app.ui.login.LoginActivity
import com.ztn.app.ui.user.UserInfoActivity
import com.ztn.app.ui.view.DiagramActivity
import com.ztn.commom.utils.FaceUtils
import com.ztn.commom.view.DiagramViewWithSurface
import com.ztn.common.ToastHelper
import com.ztn.common.framework.AppManager
import com.ztn.common.utils.animation.viewClick
import com.ztn.common.utils.show


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewStateListener: ViewStateListener
    private lateinit var permissionListener: PermissionListener
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var drawer_layout: DrawerLayout
    private lateinit var nav_view: NavigationView
    private lateinit var testImage: ImageView
    private lateinit var writePermission: Button
    private lateinit var createFile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        drawer_layout = findViewById(R.id.drawer_layout)
        nav_view = findViewById(R.id.nav_view)
        fab = findViewById(R.id.fab)
        testImage = findViewById(R.id.testImage)
        writePermission = findViewById(R.id.writePermission)
        createFile = findViewById(R.id.createFile)
        AppManager.get().addActivity(this)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        testImage.post {
            testImage.setImageBitmap(
                FaceUtils.getDefaultPortrait(testImage, "张天宁", "Android 开发")
            )
        }
        writePermission.setOnClickListener {
            RxPermissions(this).request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).subscribe({
                if (!it) {
                    ToastHelper.showToast("您未授予读取文件权限")
                    finish()
                }
            }, {}).dispose()
        }
        createFile.setOnClickListener {
            openFileOutput("test" + System.currentTimeMillis(), Context.MODE_PRIVATE)
        }

        val diagramView = DiagramViewWithSurface(this)
        Thread {
            diagramView.run()
        }.start()
        viewStateListener = object : ViewStateListener {
            override fun onBackToDesktop() {

            }

            override fun onMoveAnimStart() {
                diagramView.pause()

            }

            override fun onMoveAnimEnd() {
                diagramView.resume()

            }

            override fun onPositionUpdate(p0: Int, p1: Int) {
            }

            override fun onDismiss() {
                ToastHelper.show("关闭了")
            }

            override fun onShow() {
                diagramView.resume()
            }

            override fun onHide() {
                diagramView.pause()
            }
        }

        permissionListener = object : PermissionListener {

            override fun onSuccess() {
            }

            override fun onFail() {
            }
        }

//        if (FloatWindow.get() == null) {
//            FloatWindow
//                .with(applicationContext)
//                .setView((GLSurfaceView(this)))
//                .setWidth(100)                               //设置控件宽高
//                .setHeight(Screen.width, 0.2f)
//                .setX(100)                                   //设置控件初始位置
//                .setMoveStyle(1000, null)
//                .setY(Screen.height, 0.3f)
//                .setDesktopShow(true)                        //桌面显示
//                .setViewStateListener(viewStateListener)    //监听悬浮控件状态改变
//                .setPermissionListener(permissionListener)  //监听权限申请结果
//                .build()
//
//            createFile.postDelayed({
//                FloatWindow.destroy()
//            }, 20000)
//        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                viewClick(SettingActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.next -> {
                LoginActivity.startWithNothing(this)
            }
            R.id.another -> {

                if (checkPackInfo("com.ztn.recyclerviewdemo")) {
                    val intent = Intent(Intent.ACTION_MAIN)
                    val componentName =
                        ComponentName(
                            "com.ztn.recyclerviewdemo",
                            "com.ztn.recyclerviewdemo.MainActivity"
                        )
                    intent.component = componentName
                    intent.putExtra("come", "来自张天宁的app")
                    startActivity(intent)
                } else {
                    ToastHelper.showToast("没有安装对应的app")
                }

            }

            R.id.view_installed_apk -> {
                InstalledActivity.startWithNothing(this)
            }

            R.id.show_file_in_phone -> {
                FileActivity.startWithNothing(this)
            }
            R.id.selfView -> {
//                DoodlingActivity.startWithNothing(this)
                DiagramActivity.startWithNothing(this)
//                com.ztn.app.ui.login.LoginActivity.startWithNothing(this)

            }
            R.id.friend -> {
//                FriendActivity.startWithNothing(this)
                UserInfoActivity.startWithNothing(this)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun checkPackInfo(packageName: String): Boolean {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packageInfo != null
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.get().removeActivity(this)
    }

}
