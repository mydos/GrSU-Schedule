package by.kirich1409.grsuschedule.utils

import android.content.Context
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.ScheduleApp
import com.google.android.gms.analytics.HitBuilders

private const val CATEGORY_USER_ACTION = "User action"

public fun Context.trackException(cause: Exception?, fatal: Boolean) {
    if (BuildConfig.ANALYTICS_ENABLE) {
        if (cause != null) {
            trackEvent(
                    HitBuilders.ExceptionBuilder()
                            .setDescription(cause.message)
                            .setFatal(fatal)
                            .build()
            )
        }
    }
}

public fun Context.trackAddToFavourite() {
    if (BuildConfig.ANALYTICS_ENABLE) {
        trackEvent(
                HitBuilders.EventBuilder()
                        .setCategory(CATEGORY_USER_ACTION)
                        .setAction("Add schedule to favourite")
                        .build()
        )
    }
}

public fun Context.trackRemoveFromFavourite() {
    if (BuildConfig.ANALYTICS_ENABLE) {
        trackEvent(
                HitBuilders.EventBuilder()
                        .setCategory(CATEGORY_USER_ACTION)
                        .setAction("Remove schedule from favourite")
                        .build()
        )
    }
}

public fun Context.trackShareAppLink() {
    if (BuildConfig.ANALYTICS_ENABLE) {
        trackEvent(
                HitBuilders.EventBuilder()
                        .setCategory(CATEGORY_USER_ACTION)
                        .setAction("Share app link")
                        .build()
        )
    }
}

private fun Context.trackEvent(event: Map<String, String>) {
    (applicationContext as ScheduleApp).tracker.send(event)
}
