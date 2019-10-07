# 尚在开发中

> 架构 mvp+dagger2+okHttp3+Retrofit2


> 功能点

1.打开另外一个应用 原理:利用 manifest 中 export 的特性,配合 intent.component 打开

2.查看手机中安装的应用 原理: packageManager 可以获取手机中已安装的应用，过滤后得到非系统应用，从 packageInfo 中获取应用信息

3.查看手机中的文件并简单排序，可以打开文件 原理: 获取 Environment.getExternalStorageDirectory().path 可以到达根目录
，之后使用 listFiles 获取文件列表，通过 isDirectory 属性判断文件类型，这些操作不是 UI 操作，所以使用 RX 异步进行，拿到列
表后切换线程在 UI 中展示。打开文件功能在高版本使用 FileProvider，需要在清单文件中注册。

4.加入两个简单的自定义 view，一个是仪表盘，一个是波浪线。波浪线使用自定义 view 和 surfaceView 两种方式制作，可以在项目中自行替换，主要使用了 path ，
绘制贝塞尔曲线，ValueAnimator 实现对应的参数变化。

5.加入一个使用 socketIo 实现的简单的 IM 沟通，webSocket 方式暂未实现。