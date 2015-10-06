package by.kirich1409.grsuschedule.schedule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.DrawerActivity
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.preference.ModeChooserActivity
import by.kirich1409.grsuschedule.student.GroupScheduleFragment
import by.kirich1409.grsuschedule.teacher.TeacherScheduleFragment
import by.kirich1409.grsuschedule.utils.APP_MODE_STUDENT
import by.kirich1409.grsuschedule.utils.APP_MODE_TEACHER
import by.kirich1409.grsuschedule.utils.APP_MODE_UNKNOWN

/**
 * Created by kirillrozov on 9/19/15.
 */
public class ScheduleActivity() : DrawerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        if (savedInstanceState == null) {
            initContent()
        }

        initSubtitle()
    }

    private fun initSubtitle() {
        supportActionBar.subtitle = when (appPreference.mode) {
            APP_MODE_STUDENT -> appPreference.group?.title
            APP_MODE_TEACHER -> appPreference.teacher?.fullname
            APP_MODE_UNKNOWN -> null
            else -> null
        }
    }

    private fun initContent() {
        val mode = appPreference.mode
        when (mode) {
            APP_MODE_STUDENT -> {
                val group = appPreference.group!!
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content, GroupScheduleFragment.newInstance(group.id))
                        .commitAllowingStateLoss()
            }

            APP_MODE_TEACHER -> {
                val teacher = appPreference.teacher as Teacher
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content, TeacherScheduleFragment.newInstance(teacher.id))
                        .commitAllowingStateLoss()
            }

            APP_MODE_UNKNOWN -> {
                changeAppMode()
            }

            else -> throw RuntimeException("Unknown mode type = $mode")
        }
    }

    private fun changeAppMode() {
        startActivityForResult(
                android.content.Intent(this, ModeChooserActivity::class.java), REQUEST_CHOOSE_MODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_MODE) {
            if (resultCode == Activity.RESULT_OK) {
                initContent()
                initSubtitle()
            } else if (resultCode == Activity.RESULT_CANCELED
                    && appPreference.mode == APP_MODE_UNKNOWN) {
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_schedule, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_change_app_mode -> {
                changeAppMode()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val REQUEST_CHOOSE_MODE = 1123
    }
}