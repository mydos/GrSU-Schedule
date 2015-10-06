-dontwarn kotlin.**
-dontwarn org.w3c.dom.events.*
-dontwarn org.jetbrains.kotlin.**
-dontwarn com.fasterxml.jackson.**
-dontwarn com.squareup.okhttp.**
-dontwarn retrofit.**
-dontwarn okio.**
-dontwarn android.support.**
-dontwarn com.sun.xml.internal.**
-dontwarn com.sun.istack.internal.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.springframework.**
-dontwarn java.awt.**
-dontwarn javax.security.**
-dontwarn java.beans.**
-dontwarn javax.xml.**
-dontwarn java.util.**
-dontwarn org.w3c.dom.**
-dontwarn com.google.common.**
-dontwarn com.octo.android.robospice.**
-dontwarn android.app.Notification
-dontwarn libcore.icu.ICU
-dontwarn android.graphics.Insets
-dontwarn com.android.org.conscrypt.OpenSSLSocketImpl
-dontwarn org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl
-dontwarn android.support.**
-dontwarn by.kirich1409.grsuschedule.network.QuceryDate
-dontwarn rx.**
-dontwarn com.crashlytics.**
-dontwarn android.support.design.**

-keepattributes SourceFile,LineNumberTable,*Annotation*
-keep class com.crashlytics.android.**
-keep class retrofit.** { *; }
-keep class com.fasterxml.jackson.** { *; }
-keep class kotlin.jvm.**
-keep class retrofit.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep class okio.** { *; }
-keep class by.kirich1409.grsuschedule.network.QueryDate
-keep class android.support.v7.preference.** { *; }
-keep class android.support.v7.widget.SearchView { *; }

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keepclassmembers class * {
    @com.fasterxml.jackson.annotation.* <fields>;
    @com.fasterxml.jackson.annotation.* <init>(...);
}

-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses

-assumenosideeffects class junit.framework.Assert { *; }