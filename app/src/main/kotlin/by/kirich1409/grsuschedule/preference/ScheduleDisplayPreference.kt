package by.kirich1409.grsuschedule.preference

import android.content.Context
import android.content.SharedPreferences
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.utils.SCHEDULE_NAV_MODE_HORIZONTAL
import by.kirich1409.grsuschedule.utils.SCHEDULE_NAV_MODE_VERTICAL
import by.kirich1409.grsuschedule.utils.isPhone

/**
 * Created by kirillrozov on 9/25/15.
 */
public class ScheduleDisplayPreference(private val context: Context) {

    private val preferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var onlyActualSchedule: Boolean
        get() = preferences.getBoolean(PREF_ONLY_ACTUAL_SCHEDULE, PREF_ONLY_ACTUAL_SCHEDULE_DEFAULT)
        set(onlyActualSchedule) {
            preferences.edit()
                    .putBoolean(PREF_ONLY_ACTUAL_SCHEDULE, onlyActualSchedule)
                    .apply()
        }

    var showEmptyLesson: Boolean
        get() = preferences.getBoolean(PREF_SHOW_EMPTY_LESSON, PREF_SHOW_EMPTY_LESSON_DEFAULT)
        set(showEmptyLesson) {
            preferences.edit()
                    .putBoolean(PREF_SHOW_EMPTY_LESSON, showEmptyLesson)
                    .apply()
        }

    var navigationMode: Int
        get() {
            val defaultNavMode = if (context.isPhone()) {
                SCHEDULE_NAV_MODE_HORIZONTAL
            } else {
                SCHEDULE_NAV_MODE_VERTICAL
            }
            return preferences.getInt(PREF_NAVIGATION_MODE, defaultNavMode)
        }
        set(navMode) {
            preferences.edit()
                    .putInt(PREF_NAVIGATION_MODE, navMode)
                    .apply()
        }

    val isHorizontalNavigation: Boolean
        get() = navigationMode == SCHEDULE_NAV_MODE_HORIZONTAL

    public fun registerOnPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    public fun unregisterOnPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        const val PREF_SHOW_EMPTY_LESSON = "showEmptyLesson"
        const val PREF_ONLY_ACTUAL_SCHEDULE = "onlyActualSchedule"
        val PREF_NAVIGATION_MODE = if (BuildConfig.DEBUG) "navMode" else "a"

        private val PREF_NAME = "scheduleDisplayPref"
        private val PREF_SHOW_EMPTY_LESSON_DEFAULT = false
        private val PREF_ONLY_ACTUAL_SCHEDULE_DEFAULT = true
    }
}