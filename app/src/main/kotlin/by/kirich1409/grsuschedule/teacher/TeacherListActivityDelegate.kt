package by.kirich1409.grsuschedule.teacher

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R

/**
 * Created by kirillrozov on 9/16/15.
 */
public class TeacherListActivityDelegate(val activity: FragmentActivity) {

    public fun onCreate(savedInstanceState: Bundle?) {
        val activity = activity
        activity.setContentView(R.layout.activity_drawer)
        if (savedInstanceState == null) {
            activity.supportFragmentManager.beginTransaction()
                    .add(R.id.content, TeacherListFragment(), FRAGMENT_TEACHERS)
                    .commit()
        }
    }

    companion object {
        private val FRAGMENT_TEACHERS = if (BuildConfig.DEBUG) "teachers" else "a"
    }
}