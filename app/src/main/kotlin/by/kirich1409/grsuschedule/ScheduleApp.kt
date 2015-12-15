package by.kirich1409.grsuschedule

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import android.os.StrictMode
import by.kirich1409.grsuschedule.utils.LOCALE_RU
import com.google.android.gms.analytics.GoogleAnalytics
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class ScheduleApp : Application() {

    public val tracker by lazy {
        GoogleAnalytics.getInstance(this).newTracker(BuildConfig.GA_TRACKER_ID).apply {
            enableExceptionReporting(true)
            enableAdvertisingIdCollection(false)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Locale.setDefault(LOCALE_RU)

        if (BuildConfig.DEBUG) initStrictMode()
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build())

        val builder = StrictMode.VmPolicy.Builder()
                .penaltyLog()
                .penaltyDeath()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder.detectLeakedClosableObjects()
        }
        StrictMode.setVmPolicy(builder.build());
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        newConfig.locale = LOCALE_RU
        super.onConfigurationChanged(newConfig)
    }
}
