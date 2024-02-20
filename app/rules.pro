
## netty混淆配置
-keep class io.netty.** { *; }
-keep interface io.netty.** { *; }
-keep enum io.netty.** { *; }

-dontwarn io.netty.**
-dontwarn sun.**

## compress混淆配置
-keep class org.apache.commons.compress.** { *; }
-keepclassmembers class org.apache.commons.compress.** {
    *;
}
-dontwarn org.apache.commons.compress.**

## ddmlib配置
-keep class com.android.ddmlib.** { *; }
-dontwarn com.android.ddmlib.**

