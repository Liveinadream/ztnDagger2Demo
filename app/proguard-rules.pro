# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#gomap sdk
-keepattributes Signature, *Annotation*, EnclosingMethod
# Reflection on classes from native code
-keep class com.google.gson.JsonArray { *; }
-keep class com.google.gson.JsonElement { *; }
-keep class com.google.gson.JsonObject { *; }
-keep class com.google.gson.JsonPrimitive { *; }
-dontnote com.google.gson.**
# dontnote for keeps the entry point x but not the descriptor class y
-dontnote com.gomap.sdk.maps.MapboxMap$OnFpsChangedListener
-dontnote com.gomap.sdk.style.layers.PropertyValue
-dontnote com.gomap.sdk.maps.MapboxMap
-dontnote com.gomap.sdk.maps.MapboxMapOptions
-dontnote com.gomap.sdk.log.LoggerDefinition
-dontnote com.gomap.sdk.location.engine.LocationEnginePriority
# config for geojson
#-keep class com.gomap.sdk.** { *; }
-keep class com.gomap.sdk.annotations.Marker
-keep class com.gomap.sdk.annotations.Polygon
-keep class com.gomap.sdk.annotations.Polyline
-keep class com.gomap.sdk.geometry.LatLng
-keep class com.gomap.sdk.geometry.LatLngBounds
-keep class com.gomap.sdk.geometry.LatLngQuad
-keep class com.gomap.sdk.navigation.** { *; }
-keep class com.gomap.sdk.snapshotter.MapSnapshot
-keep class com.gomap.sdk.snapshotter.MapSnapshotter
-keep class com.gomap.sdk.style.** { *; }
-keep class com.gomap.sdk.text.LocalGlyphRasterizer
-keep class com.gomap.sdk.util.DefaultStyle
-keep class com.gomap.sdk.util.TileServerOptions
-keep class com.gomap.sdk.net.NativeConnectivityListener
-keep class com.gomap.sdk.storage.** { *; }
-keep class com.gomap.sdk.log.Logger
-keep class com.gomap.sdk.maps.renderer.MapRenderer
-keep class com.gomap.sdk.Mapbox
-keep class com.gomap.sdk.maps.NativeMapView
# config for geojson
-keep class com.gomap.geojson.** { *; }
# config for okhttp 3.11.0, https://github.com/square/okhttp/pull/3354
-dontwarn javax.annotation.**
-dontnote okhttp3.internal.**
-dontwarn org.codehaus.**
-dontwarn com.google.auto.value.**
# config for additional notes
-dontnote org.robolectric.Robolectric
-dontnote libcore.io.Memory
-dontnote com.google.protobuf.**
-dontnote android.net.**
-dontnote org.apache.http.**
# config for gomap-sdk-services
# while we don't include this dependency directly
# a large amount of users combine it with our SDK
# we aren't able to provide a proguard config in that project (jar vs aar)
-dontwarn com.sun.xml.internal.ws.spi.db.*