-optimizations !code/allocation/variable

-dontwarn rx.**
-dontwarn com.fasterxml.jackson.**
-dontwarn org.w3c.dom.**
-dontwarn com.octo.android.robospice.**
-dontwarn kotlin.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn com.squareup.**
-dontwarn com.google.gson.**
-dontwarn retrofit.converter.GsonConverter
-dontwarn by.kirich1409.grsuschedule.lesson.LessonFragment


-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-dontnote com.google.android.gms.**
-dontnote retrofit.Platform
-dontnote rx.Observable
-dontnote com.squareup.okhttp.**
-dontnote io.fabric.sdk.android.services.common.AdvertisingInfoReflectionStrategy
-dontnote libcore.icu.ICU
-dontnote android.graphics.Insets
-dontnote kotlin.jvm.internal.**
-dontnote android.support.v7.**
-dontnote android.support.design.**
-dontnote android.support.v4.**


-keep class kotlin.jvm.** { *; }
-keep class kotlin.reflect.** { *; }

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class com.squareup.** { *; }
-keep class okio.** { *; }
-keep class retrofit.** { *; }
-keep class com.fasterxml.jackson.** { *; }

-keep class by.kirich1409.grsuschedule.network.QueryDate { *; }
-keep class by.kirich1409.grsuschedule.model.*  { *; }

-keep class android.support.design.widget.CoordinatorLayout.DefaultBehavior
-keep class android.support.design.widget.NavigationView { *; }
-keep class android.support.design.widget.NavigationView$OnNavigationItemSelectedListener { *; }
-keep class android.support.design.widget.Snackbar* { *; }
-keep class android.support.design.widget.TabLayout$OnTabSelectedListener { *; }
-keep class android.support.design.internal.** { *; }

-keep class android.support.v7.preference.PreferenceInflater { *; }
-keep class android.support.v7.preference.PreferenceManager { *; }
-keep class android.support.v7.preference.PreferenceFragmentCompat { *; }
-keep class android.support.v7.preference.CheckBoxPreference { *; }
-keep class android.support.v7.preference.ListPreference { *; }
-keep class android.support.v7.preference.PreferenceScreen { *; }
-keep class android.support.v7.preference.PreferenceViewHolder { *; }

-keep class android.support.v7.widget.SearchView { *; }
-keep class android.support.v7.widget.RecyclerView$SavedState { *; }
-keep class android.support.v4.view.WindowInsetsCompat
-keep class android.support.v4.view.ViewPager
-keep class android.support.v7.widget.RecyclerView* { *; }

# Keep CoordinatorLayout behaviour classes
-keep @android.support.design.widget.CoordinatorLayout.DefaultBehavior class *

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepclasseswithmembers class * {
    @com.fasterxml.jackson.annotation.* <fields>;
    @com.fasterxml.jackson.annotation.* <init>(...);
}

-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,EnclosingMethod

# Needed for Parcelable/SafeParcelable Creators to not get stripped
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}


-assumenosideeffects class junit.framework.Assert {
    public static void assertEquals(...);
    public static void assertNotNull(...);
    public static void assertNull(...);
    public static void assertFalse(...);
    public static void assertTrue(...);
    public static void assertThat(...);
    public static void assertSame(...);
    public static void assertNotSame(...);
    public static void fail(...);
    public static void failSame(...);
    public static void failNotSame(...);
    public static void failNotEquals(...);
}
