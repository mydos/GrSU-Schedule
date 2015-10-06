package by.kirich1409.grsuschedule.preference

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.content.SharedPreferencesCompat
import by.kirich1409.grsuschedule.BuildConfig

/**
 * Created by kirillrozov on 9/25/15.
 */
public class ScheduleDisplayPreference(context: Context) {
    private val preferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var onlyActualSchedule: Boolean
        get() {
            return preferences.getBoolean(PREF_ONLY_ACTUAL_SCHEDULE, PREF_ONLY_ACTUAL_SCHEDULE_DEFAULT)
        }
        set(onlyActualSchedule) {
            applyBooleanValue(PREF_ONLY_ACTUAL_SCHEDULE, onlyActualSchedule)
        }

    var showEmptyLesson: Boolean
        get() {
            return preferences.getBoolean(PREF_SHOW_EMPTY_LESSON, PREF_SHOW_EMPTY_LESSON_DEFAULT)
        }
        set(showEmptyLesson) {
            applyBooleanValue(PREF_SHOW_EMPTY_LESSON, showEmptyLesson)
        }

    private fun applyBooleanValue(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor)
    }

    public fun registerOnPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    public fun unregisterOnPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        val PREF_SHOW_EMPTY_LESSON = if (BuildConfig.DEBUG) "showEmptyLesson" else "sel"
        val PREF_ONLY_ACTUAL_SCHEDULE = if (BuildConfig.DEBUG) "onlyActualSchedule" else "oas"

        private val PREF_NAME = "scheduleDisplayPref"
        private val PREF_SHOW_EMPTY_LESSON_DEFAULT = false
        private val PREF_ONLY_ACTUAL_SCHEDULE_DEFAULT = true
    }
}