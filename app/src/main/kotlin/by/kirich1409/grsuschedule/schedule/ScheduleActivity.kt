package by.kirich1409.grsuschedule.schedule

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.ScheduleApp
import by.kirich1409.grsuschedule.app.DrawerActivity
import by.kirich1409.grsuschedule.db.ScheduleContract
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPreference
import by.kirich1409.grsuschedule.schedule.list.GroupScheduleListFragment
import by.kirich1409.grsuschedule.schedule.list.TeacherScheduleListFragment
import by.kirich1409.grsuschedule.schedule.pager.GroupSchedulePagerFragment
import by.kirich1409.grsuschedule.schedule.pager.TeacherSchedulePagerFragment
import by.kirich1409.grsuschedule.utils.*
import com.google.android.gms.analytics.HitBuilders

/**
 * Created by kirillrozov on 9/19/15.
 */
public open class ScheduleActivity : DrawerActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    lateinit var schedulePreference: ScheduleDisplayPreference
    var horizontalNavigation = false
    var scheduleMode = SCHEDULE_MODE_UNKNOWN
    override val screenName = "Schedule"

    val contentView: View? by lazy { findViewById(R.id.content) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedulePreference = ScheduleDisplayPreference(this)
        horizontalNavigation = schedulePreference.isHorizontalNavigation

        setContentView(R.layout.activity_drawer)
        if (savedInstanceState == null) {
            scheduleMode = extractScheduleMode(intent)
            if (scheduleMode != SCHEDULE_MODE_UNKNOWN) {
                initContent()
            } else {
                startModePicker()
            }
        } else {
            scheduleMode = savedInstanceState.getInt(STATE_SCHEDULE_MODE)
        }

    }

    override fun trackScreenView() {
        if (BuildConfig.ANALYTICS_ENABLE && scheduleMode != SCHEDULE_MODE_UNKNOWN) {
            val content = when (scheduleMode) {
                SCHEDULE_MODE_GROUP -> {
                    "group(${intent.getParcelableExtra<Group>(EXTRA_GROUP).id})"
                }
                SCHEDULE_MODE_TEACHER -> {
                    "teacher(${intent.getParcelableExtra<Teacher>(EXTRA_TEACHER).id})"
                }
                SCHEDULE_MODE_PREFERENCE -> {
                    when (appPreference.mode) {
                        APP_MODE_STUDENT -> "group(${appPreference.group!!.id})"
                        APP_MODE_TEACHER -> "teacher(${appPreference.teacher!!.id})"
                        else -> "Unknown"
                    }
                }
                else -> "Unknown"
            }

            val source = when {
                intent.hasExtra(EXTRA_FROM_FAVOURITE) -> "Favourite"
                intent.hasExtra(EXTRA_TEACHER) -> "Teacher picker"
                intent.hasExtra(EXTRA_GROUP) -> "Group picker"
                scheduleMode == SCHEDULE_MODE_PREFERENCE -> "Preference"
                else -> "Unknown"
            }

            (application as ScheduleApp).tracker.send(
                    HitBuilders.ScreenViewBuilder()
                            .set("Content", content)
                            .set("Source", source)
                            .build()
            )
        }
    }

    private fun extractScheduleMode(intent: Intent): Int {
        return if (intent.hasExtra(EXTRA_GROUP)) {
            SCHEDULE_MODE_GROUP
        } else if (intent.hasExtra(EXTRA_TEACHER)) {
            SCHEDULE_MODE_TEACHER
        } else if (appPreference.mode != APP_MODE_UNKNOWN) {
            SCHEDULE_MODE_PREFERENCE
        } else {
            SCHEDULE_MODE_UNKNOWN
        }
    }

    private fun initContent() {
        when (scheduleMode) {
            SCHEDULE_MODE_PREFERENCE -> {
                val mode = appPreference.mode
                when (mode) {
                    APP_MODE_STUDENT -> initStudentScheduleFragment(appPreference.group!!)
                    APP_MODE_TEACHER -> initTeacherScheduleFragment(appPreference.teacher!!)
                    else -> throw RuntimeException("Unknown mode type = $mode")
                }
            }
            SCHEDULE_MODE_GROUP -> {
                initStudentScheduleFragment(intent.getParcelableExtra<Group>(EXTRA_GROUP))
            }
            SCHEDULE_MODE_TEACHER -> {
                initTeacherScheduleFragment(intent.getParcelableExtra<Teacher>(EXTRA_TEACHER))
            }
            else -> throw IllegalStateException("Unhandled schedule mode state = $scheduleMode")
        }
    }

    private fun initTeacherScheduleFragment(teacher: Teacher) {
        val fragment: Fragment = if (schedulePreference.isHorizontalNavigation && isPhone()) {
            TeacherSchedulePagerFragment.newInstance(teacher.id)
        } else {
            TeacherScheduleListFragment.newInstance(teacher.id)
        }

        supportFragmentManager
                .beginTransaction().replace(R.id.content, fragment)
                .commitAllowingStateLoss()

        supportActionBar.subtitle = teacher.fullName
    }

    private fun initStudentScheduleFragment(group: Group) {
        val fragment: Fragment = if (schedulePreference.isHorizontalNavigation && isPhone()) {
            GroupSchedulePagerFragment.newInstance(group.id)
        } else {
            GroupScheduleListFragment.newInstance(group.id)
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commitAllowingStateLoss()

        supportActionBar.subtitle = group.title
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
        scheduleMode = SCHEDULE_MODE_PREFERENCE
        trackScreenView()
        supportInvalidateOptionsMenu()
        initContent()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.navigation_my_schedule
                && scheduleMode != SCHEDULE_MODE_PREFERENCE) {
            startSectionActivity(ScheduleActivity::class, true)
            return true
        } else {
            return super.onNavigationItemSelected(menuItem)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_SCHEDULE_MODE, scheduleMode)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        if (scheduleMode != SCHEDULE_MODE_UNKNOWN) {
            if (isFavourite()) {
                menuInflater.inflate(R.menu.schedule_remove_from_favourite, menu)
            } else {
                menuInflater.inflate(R.menu.schedule_add_to_favourite, menu)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_schedule_star -> {
                setFavourite(true)
                trackAddToFavourite()
                return true
            }
            R.id.menu_schedule_unstar -> {
                setFavourite(false)
                trackRemoveFromFavourite()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setFavourite(favourite: Boolean) {
        if (scheduleMode == SCHEDULE_MODE_GROUP) {
            intent.getParcelableExtra<Group>(EXTRA_GROUP).setFavourite(favourite)
        } else if (scheduleMode == SCHEDULE_MODE_TEACHER) {
            intent.getParcelableExtra<Teacher>(EXTRA_TEACHER).setFavourite(favourite)
        } else if (scheduleMode == SCHEDULE_MODE_PREFERENCE) {
            when (appPreference.mode) {
                APP_MODE_STUDENT -> appPreference.group!!.setFavourite(favourite)
                APP_MODE_TEACHER -> appPreference.teacher!!.setFavourite(favourite)
                else -> throw RuntimeException("Unknown mode type = $appPreference.mode")
            }
        } else {
            throw RuntimeException("Unhandled schedule mode")
        }

        val msgResId = if (favourite) {
            R.string.result_added_to_favorite
        } else {
            R.string.result_removed_from_favorite
        }
        Snackbar.make(contentView, msgResId, Snackbar.LENGTH_SHORT).show()

        supportInvalidateOptionsMenu()
    }

    private fun Group.setFavourite(favourite: Boolean) {
        setFavourite(favourite, title, id, ScheduleContract.Favourite.TYPE_GROUP)
    }

    private fun setFavourite(favourite: Boolean, title: String, id: Int, type: String) {
        if (favourite) {
            val values = ContentValues(3);
            values.put(ScheduleContract.Favourite.NAME, title)
            values.put(ScheduleContract.Favourite.CONTENT_ID, id)
            values.put(ScheduleContract.Favourite.TYPE, type)
            contentResolver.insert(ScheduleContract.Favourite.CONTENT_URI, values)
        } else {
            val where = ScheduleContract.Favourite.CONTENT_ID + " = ?" +
                    " AND " + ScheduleContract.Favourite.TYPE + " = ?"
            val args = arrayOf(id.toString(), type)
            contentResolver.delete(ScheduleContract.Favourite.CONTENT_URI, where, args)
        }
    }

    private fun Teacher.setFavourite(favourite: Boolean) {
        setFavourite(favourite, fullName, id, ScheduleContract.Favourite.TYPE_TEACHER)
    }

    private fun isInFavourite(id: Int, type: String): Boolean {
        val selection = ScheduleContract.Favourite.CONTENT_ID + " = ?" +
                " AND " + ScheduleContract.Favourite.TYPE + " = ?"
        val projection = arrayOf(ScheduleContract.Favourite._COUNT)
        val args = arrayOf(id.toString(), type)
        val cursor = contentResolver.query(
                ScheduleContract.Favourite.CONTENT_URI, projection, selection, args, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val countColumnIndex = cursor.getColumnIndex(ScheduleContract.Favourite._COUNT)
            val hasValues = cursor.getInt(countColumnIndex) > 0;
            cursor.close()
            return hasValues
        } else {
            return false
        }
    }

    private fun Group.isInFavourite() = isInFavourite(id, ScheduleContract.Favourite.TYPE_GROUP)

    private fun Teacher.isInFavourite() = isInFavourite(id, ScheduleContract.Favourite.TYPE_TEACHER)

    /**
     * Check the schedule is favourite
     */
    private fun isFavourite(): Boolean {
        return if (scheduleMode == SCHEDULE_MODE_GROUP) {
            intent.getParcelableExtra<Group>(EXTRA_GROUP).isInFavourite()
        } else if (scheduleMode == SCHEDULE_MODE_TEACHER) {
            intent.getParcelableExtra<Teacher>(EXTRA_TEACHER).isInFavourite()
        } else if (scheduleMode == SCHEDULE_MODE_PREFERENCE) {
            when (appPreference.mode) {
                APP_MODE_STUDENT -> appPreference.group!!.isInFavourite()
                APP_MODE_TEACHER -> appPreference.teacher!!.isInFavourite()
                else -> throw RuntimeException("Unknown mode type = $appPreference.mode")
            }
        } else {
            throw RuntimeException("Unhandled schedule mode")
        }
    }

    companion object {
        private val STATE_SCHEDULE_MODE = if (BuildConfig.DEBUG) "scheduleMode" else "a"

        private val EXTRA_GROUP = if (BuildConfig.DEBUG) "group" else "a"
        private val EXTRA_TEACHER = if (BuildConfig.DEBUG) "teacher" else "b"
        private val EXTRA_FROM_FAVOURITE = if (BuildConfig.DEBUG) "fromFavourite" else "c"

        private val SCHEDULE_MODE_UNKNOWN = -1
        private val SCHEDULE_MODE_GROUP = 1
        private val SCHEDULE_MODE_TEACHER = 2
        private val SCHEDULE_MODE_PREFERENCE = 3

        public fun makeIntent(context: Context, group: Group, fromFavourite: Boolean = false) =
                Intent(context, ScheduleActivity::class.java)
                        .putExtra(EXTRA_GROUP, group)
                        .putExtra(EXTRA_FROM_FAVOURITE, fromFavourite)

        public fun makeIntent(context: Context, teacher: Teacher, fromFavourite: Boolean = false) =
                Intent(context, ScheduleActivity::class.java)
                        .putExtra(EXTRA_TEACHER, teacher)
                        .putExtra(EXTRA_FROM_FAVOURITE, fromFavourite)
    }
}