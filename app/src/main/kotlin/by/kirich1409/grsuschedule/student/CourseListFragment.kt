package by.kirich1409.grsuschedule.student

import android.content.Context
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Course
import by.kirich1409.grsuschedule.utils.setSupportActionBarSubtitle
import by.kirich1409.grsuschedule.utils.setSupportActionBarTitle

/**
 * Created by kirillrozov on 9/13/15.
 */
public class CourseListFragment : ListFragment() {

    private var mListener: Listener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            mListener = context
        } else {
            throw RuntimeException(
                    "Host context must implements CoursesFragment.Listener interface.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBarTitle(R.string.activity_label_course)
        activity.setSupportActionBarSubtitle(null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courses: Array<Course> = resources.getStringArray(R.array.courses_title)
                .mapIndexed { index, title -> Course(title, index + 1) }
                .toTypedArray()
        listAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, courses)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        mListener!!.onCourseSelected(l.getItemAtPosition(position) as Course)
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    public interface Listener {
        public fun onCourseSelected(course: Course)
    }
}
