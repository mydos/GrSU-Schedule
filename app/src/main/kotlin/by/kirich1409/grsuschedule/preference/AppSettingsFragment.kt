package by.kirich1409.grsuschedule.preference

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import by.kirich1409.grsuschedule.R

/**
 * Created by kirillrozov on 9/25/15.
 */
public class AppSettingsFragment : PreferenceFragmentCompat() {

    protected var scheduleDisplayPref: ScheduleDisplayPreference? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scheduleDisplayPref = ScheduleDisplayPreference(context)
    }

    override fun onDetach() {
        scheduleDisplayPref = null
        super.onDetach()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (rootKey == null) {
            addPreferencesFromResource(R.xml.pref_schedule_display)
        }

        initCheckBoxPreference(
                getText(R.string.pref_show_empty_lesson_key),
                scheduleDisplayPref!!.showEmptyLesson,
                { pref, newValue ->
                    scheduleDisplayPref!!.showEmptyLesson = newValue
                    true
                }
        )

        val horizontalNavigationPreference =
                findPreference(getText(R.string.pref_navigation_mode_key)) as ListPreference
        horizontalNavigationPreference.setValueIndex(
                if (scheduleDisplayPref!!.isHorizontalNavigation) 1 else 0)
        horizontalNavigationPreference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference, newValue ->
                    scheduleDisplayPref!!.navigationMode = newValue.toString().toInt()
                    true
                }
    }

    private fun initCheckBoxPreference(
            key: CharSequence, value: Boolean, listener: (Preference, Boolean) -> Boolean) {
        val preference: CheckBoxPreference = findPreference(key) as CheckBoxPreference
        preference.isChecked = value
        preference.setOnPreferenceChangeListener (
                { preference, newValue -> listener(preference, newValue as Boolean) })
    }
}