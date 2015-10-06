package by.kirich1409.grsuschedule.student

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Course
import by.kirich1409.grsuschedule.model.Department
import by.kirich1409.grsuschedule.model.Faculty
import by.kirich1409.grsuschedule.model.Group

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupPickerActivityDelegate(private val activity: AppCompatActivity) {

    private var mCourseNumber = -1
    private var mDepartmentId = -1
    private var mFacultyId = -1

    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            activity.supportFragmentManager.beginTransaction()
                    .add(R.id.content, DepartmentListFragment(), FRAGMENT_DEPARTMENTS)
                    .commit()
        } else {
            mCourseNumber = savedInstanceState.getInt(STATE_COURSE, -1)
            mDepartmentId = savedInstanceState.getInt(STATE_DEPARTMENT_ID, -1)
            mFacultyId = savedInstanceState.getInt(STATE_FACULTY_ID, -1)
        }
    }

    fun onCourseSelected(course: Course) {
        mCourseNumber = course.number
        val fragment = GroupListFragment.newInstance(mDepartmentId, mFacultyId, mCourseNumber)
        changeStep(fragment, FRAGMENT_GROUPS)
    }

    private fun changeStep(fragment: Fragment, tag: String) {
        activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.content, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    fun onDepartmentSelected(department: Department) {
        mDepartmentId = department.id
        changeStep(FacultiesListFragment(), FRAGMENT_FACULTIES)
    }

    fun onFacultySelected(faculty: Faculty) {
        mFacultyId = faculty.id
        changeStep(CoursesFragment(), FRAGMENT_COURSES)
    }

    fun onGroupSelected(group: Group) {
        if (activity.callingActivity != null) {
            val result = Intent()
            result.putExtra(EXTRA_GROUP, group)
            activity.setResult(Activity.RESULT_OK, result)
            activity.finish()
        } else {
            activity.startActivity(GroupScheduleActivity.makeIntent(activity, group))
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_COURSE, mCourseNumber)
        outState.putInt(STATE_DEPARTMENT_ID, mDepartmentId)
        outState.putInt(STATE_FACULTY_ID, mFacultyId)
    }

    companion object {
        public const val EXTRA_GROUP: String = "group"

        private const val FRAGMENT_DEPARTMENTS = "departments"
        private const val FRAGMENT_GROUPS = "groups"
        private const val FRAGMENT_FACULTIES = "faculties"
        private const val FRAGMENT_COURSES = "courses"

        private const val STATE_COURSE = "course"
        private const val STATE_DEPARTMENT_ID = "departmentId"
        private const val STATE_FACULTY_ID = "facultyId"
    }
}
