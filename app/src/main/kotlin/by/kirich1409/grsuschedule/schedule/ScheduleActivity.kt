package by.kirich1409.grsuschedule.schedule

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.DrawerActivity
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPreference
import by.kirich1409.grsuschedule.student.GroupScheduleListFragment
import by.kirich1409.grsuschedule.teacher.TeacherScheduleListFragment
import by.kirich1409.grsuschedule.utils.APP_MODE_STUDENT
import by.kirich1409.grsuschedule.utils.APP_MODE_TEACHER
import by.kirich1409.grsuschedule.utils.APP_MODE_UNKNOWN
import by.kirich1409.grsuschedule.utils.isPhone

/**
 * Created by kirillrozov on 9/19/15.
 */
public class ScheduleActivity : DrawerActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    lateinit var schedulePreference: ScheduleDisplayPreference
    var horizontalNavigation = false
    var scheduleMode = SCHEDULE_MODE_UNKNOWN
    override val screenName: String
        get() = when (scheduleMode) {
            SCHEDULE_MODE_PREFERENCE -> "ScheduleActivity(preference)"
            SCHEDULE_MODE_GROUP -> "ScheduleActivity(group)"
            SCHEDULE_MODE_TEACHER -> "ScheduleActivity(teacher)"
            else -> "ScheduleActivity"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedulePreference = ScheduleDisplayPreference(this)
        horizontalNavigation = schedulePreference.isHorizontalNavigation

        setContentView(R.layout.activity_drawer)
        if (savedInstanceState == null) {
            scheduleMode = extractScheduleMode()
            initContent()
        } else {
            scheduleMode = savedInstanceState.getInt(STATE_SCHEDULE_MODE)
        }

        initSubtitle()
    }

    private fun extractScheduleMode(): Int {
        val intent = intent
        val extras = intent.extras
        return if (extras == null || extras.isEmpty) {
            SCHEDULE_MODE_PREFERENCE
        } else if (intent.hasExtra(EXTRA_GROUP)) {
            SCHEDULE_MODE_GROUP
        } else if (intent.hasExtra(EXTRA_TEACHER)) {
            SCHEDULE_MODE_TEACHER
        } else {
            SCHEDULE_MODE_PREFERENCE
        }
    }

    private fun initSubtitle() {
        supportActionBar.subtitle = when (appPreference.mode) {
            APP_MODE_STUDENT -> appPreference.group!!.title
            APP_MODE_TEACHER -> appPreference.teacher!!.fullname
            APP_MODE_UNKNOWN -> null
            else -> null
        }
    }

    private fun initContent() {
        when (scheduleMode) {
            SCHEDULE_MODE_PREFERENCE -> {
                val mode = appPreference.mode
                when (mode) {
                    APP_MODE_STUDENT -> initStudentScheduleFragment()
                    APP_MODE_TEACHER -> initTeacherScheduleFragment()
                    APP_MODE_UNKNOWN -> startModePicker()
                    else -> throw RuntimeException("Unknown mode type = $mode")
                }
            }
            SCHEDULE_MODE_GROUP -> initStudentScheduleFragment()
            SCHEDULE_MODE_TEACHER -> initTeacherScheduleFragment()
            else -> throw IllegalStateException("Unhandled schedule mode state = $scheduleMode")
        }
    }

    private fun initTeacherScheduleFragment() {
        val teacher = if (scheduleMode == SCHEDULE_MODE_PREFERENCE) {
            appPreference.teacher!!
        } else {
            intent.getParcelableExtra<Teacher>(EXTRA_TEACHER)
        }

        val fragment: Fragment = if (schedulePreference.isHorizontalNavigation && isPhone()) {
            TeacherSchedulePagerFragment.newInstance(teacher.id)
        } else {
            TeacherScheduleListFragment.newInstance(teacher.id)
        }

        supportFragmentManager
                .beginTransaction().replace(R.id.content, fragment)
                .commitAllowingStateLoss()
    }

    private fun initStudentScheduleFragment() {
        val group = if (scheduleMode == SCHEDULE_MODE_PREFERENCE) {
            appPreference.group!!
        } else {
            intent.getParcelableExtra<Group>(EXTRA_GROUP)
        }

        val fragment: Fragment = if (schedulePreference.isHorizontalNavigation && isPhone()) {
            GroupSchedulePagerFragment.newInstance(group.id)
        } else {
            GroupScheduleListFragment.newInstance(group.id)
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commitAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        if (horizontalNavigation != schedulePreference.isHorizontalNavigation) {
            onNavigationModeChanged()
        }
        schedulePreference.registerOnPreferenceChangeListener(this)
    }

    override fun onStop() {
        schedulePreference.unregisterOnPreferenceChangeListener(this)
        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (ScheduleDisplayPreference.PREF_NAVIGATION_MODE == key
                && horizontalNavigation != schedulePreference.isHorizontalNavigation) {
            onNavigationModeChanged()
        }
    }

    private fun onNavigationModeChanged() {
        horizontalNavigation = schedulePreference.isHorizontalNavigation
        initContent()
    }

    override fun onModeChanged() {
        initContent()
        initSubtitle()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_SCHEDULE_MODE, scheduleMode)
    }

    companion object {
        private val STATE_SCHEDULE_MODE = if (BuildConfig.DEBUG) "scheduleMode" else "a"

        private val EXTRA_GROUP = if (BuildConfig.DEBUG) "group" else "a"
        private val EXTRA_TEACHER = if (BuildConfig.DEBUG) "teacher" else "b"

        private val SCHEDULE_MODE_UNKNOWN = -1
        private val SCHEDULE_MODE_GROUP = 1
        private val SCHEDULE_MODE_TEACHER = 2
        private val SCHEDULE_MODE_PREFERENCE = 3

        public fun makeIntent(context: Context, group: Group): Intent {
            val intent = Intent(context, ScheduleActivity::class.java)
            intent.putExtra(EXTRA_GROUP, group)
            return intent
        }

        public fun makeIntent(context: Context, teacher: Teacher): Intent {
            val intent = Intent(context, ScheduleActivity::class.java)
            intent.putExtra(EXTRA_TEACHER, teacher)
            return intent
        }
    }
}