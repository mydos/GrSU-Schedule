package by.kirich1409.grsuschedule.teacher

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.utils.findSupportFragmentByTag

/**
 * Created by kirillrozov on 9/16/15.
 */
public class TeacherListActivityDelegate(val activity: FragmentActivity) {

    private var teacherListFragment: TeacherListFragment? = null

    public fun onCreate(savedInstanceState: Bundle?) {
        activity.setContentView(R.layout.activity_drawer)
        if (savedInstanceState == null) {
            teacherListFragment = TeacherListFragment()
            val fragmentManager = activity.supportFragmentManager
            fragmentManager.beginTransaction()
                    .add(R.id.content, teacherListFragment, FRAGMENT_TEACHERS)
                    .commit()
            fragmentManager.executePendingTransactions()
        } else {
            teacherListFragment = activity.findSupportFragmentByTag(FRAGMENT_TEACHERS)
        }
    }

    companion object {
        private const val FRAGMENT_TEACHERS = "teachers"
    }
}