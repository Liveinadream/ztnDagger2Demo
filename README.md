# 尚在开发中

> 架构 mvp+dagger2+okhttp3+Retrofit2


> 功能点

1.打开另外一个应用 原理:利用 manifest 中 export 的特性,配合 intent.component 打开

2.查看手机中安装的应用 原理: packageManager 可以获取手机中已安装的应用，过滤后得到非系统应用，从 packageInfo 中获取应用信息