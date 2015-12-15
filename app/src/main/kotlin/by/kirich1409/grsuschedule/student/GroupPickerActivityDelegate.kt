package by.kirich1409.grsuschedule.student

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Course
import by.kirich1409.grsuschedule.model.Department
import by.kirich1409.grsuschedule.model.Faculty
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.schedule.ScheduleActivity

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupPickerActivityDelegate(private val activity: AppCompatActivity) {

    private var course = -1
    private var department: Department? = null
    private var faculty: Faculty? = null

    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            activity.supportFragmentManager.beginTransaction()
                    .add(R.id.content, DepartmentListFragment(), FRAGMENT_DEPARTMENTS)
                    .commit()
        } else {
            course = savedInstanceState.getInt(STATE_COURSE, -1)
            department = savedInstanceState.getParcelable(STATE_DEPARTMENT)
            faculty = savedInstanceState.getParcelable(STATE_FACULTY)
        }
    }

    fun onCourseSelected(course: Course) {
        this.course = course.number
        val fragment = GroupListFragment.newInstance(department!!, faculty!!, this.course)
        changeStep(fragment, FRAGMENT_GROUPS)
    }

    private fun changeStep(fragment: Fragment, tag: String) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        // Transaction animation causes crash on native code level on Android 4.3
        // Disable animation for fix problem
        if (Build.VERSION.SDK_INT != 18) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        transaction.replace(R.id.content, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    fun onDepartmentSelected(department: Department) {
        this.department = department
        changeStep(FacultyListFragment(), FRAGMENT_FACULTIES)
    }

    fun onFacultySelected(faculty: Faculty) {
        this.faculty = faculty
        changeStep(CourseListFragment(), FRAGMENT_COURSES)
    }

    fun onGroupSelected(group: Group) {
        if (activity.callingActivity != null) {
            activity.setResult(Activity.RESULT_OK,
                    Intent().apply { putExtra(EXTRA_GROUP, group) })
            activity.finish()
        } else {
            activity.startActivity(ScheduleActivity.makeIntent(activity, group))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                activity.finish();
            }
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_COURSE, course)
        outState.putParcelable(STATE_DEPARTMENT, department)
        outState.putParcelable(STATE_FACULTY, faculty)
    }

    companion object {
        public val EXTRA_GROUP: String = if (BuildConfig.DEBUG) "group" else "a"

        private val FRAGMENT_DEPARTMENTS = if (BuildConfig.DEBUG) "departments" else "a"
        private val FRAGMENT_GROUPS = if (BuildConfig.DEBUG) "groups" else "b"
        private val FRAGMENT_FACULTIES = if (BuildConfig.DEBUG) "faculties" else "c"
        private val FRAGMENT_COURSES = if (BuildConfig.DEBUG) "courses" else "d"

        private val STATE_COURSE = if (BuildConfig.DEBUG) "course" else "a"
        private val STATE_DEPARTMENT = if (BuildConfig.DEBUG) "departmentId" else "b"
        private val STATE_FACULTY = if (BuildConfig.DEBUG) "facultyId" else "c"
    }
}
