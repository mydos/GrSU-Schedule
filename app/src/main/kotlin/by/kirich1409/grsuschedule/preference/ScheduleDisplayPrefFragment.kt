package by.kirich1409.grsuschedule.preference

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import by.kirich1409.grsuschedule.R
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

/**
 * Created by kirillrozov on 9/25/15.
 */
public class ScheduleDisplayPrefFragment() : PreferenceFragmentCompat() {

    var scheduleDisplayPref: ScheduleDisplayPreference? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scheduleDisplayPref = ScheduleDisplayPreference(context)
    }

    override fun onDetach() {
        scheduleDisplayPref = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById(R.id.list) as? RecyclerView
        recyclerView?.addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context)
                        .sizeResId(R.dimen.divider_size)
                        .colorResId(R.color.divider)
                        .marginResId(R.dimen.margin_8)
                        .build())
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

        initCheckBoxPreference(
                getText(R.string.pref_only_actual_schedule_key),
                scheduleDisplayPref!!.onlyActualSchedule,
                { pref, newValue ->
                    scheduleDisplayPref!!.onlyActualSchedule = newValue
                    true
                }
        )
    }

    private fun initCheckBoxPreference(
            key: CharSequence, value: Boolean, listener: (Preference, Boolean) -> Boolean) {
        val preference: CheckBoxPreference = findPreference(key) as CheckBoxPreference
        preference.isChecked = value
        preference.setOnPreferenceChangeListener (
                { preference, newValue -> listener(preference, newValue as Boolean) })
    }
}