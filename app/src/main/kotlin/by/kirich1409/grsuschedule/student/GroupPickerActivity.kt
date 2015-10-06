package by.kirich1409.grsuschedule.student

import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseActivity
import by.kirich1409.grsuschedule.model.Course
import by.kirich1409.grsuschedule.model.Department
import by.kirich1409.grsuschedule.model.Faculty
import by.kirich1409.grsuschedule.model.Group

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupPickerActivity : BaseActivity(),
        CoursesFragment.Listener, DepartmentListFragment.Listener,
        FacultiesListFragment.Listener, GroupListFragment.Listener {

    private val delegate = GroupPickerActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_toolbar)
        delegate.onCreate(savedInstanceState)
    }

    override fun onCourseSelected(course: Course) {
        delegate.onCourseSelected(course)
    }

    override fun onDepartmentSelected(department: Department) {
        delegate.onDepartmentSelected(department)
    }

    override fun onFacultySelected(faculty: Faculty) {
        delegate.onFacultySelected(faculty)
    }

    override fun onGroupSelected(group: Group) {
        delegate.onGroupSelected(group)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        delegate.onSaveInstanceState(outState)
    }

    companion object{
        const val EXTRA_GROUP = GroupPickerActivityDelegate.EXTRA_GROUP
    }
}
