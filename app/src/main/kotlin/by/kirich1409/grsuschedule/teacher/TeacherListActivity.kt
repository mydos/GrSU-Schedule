package by.kirich1409.grsuschedule.teacher

import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.DrawerActivity
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.schedule.ScheduleActivity

/**
 * Created by kirillrozov on 9/14/15.
 */
public class TeacherListActivity : DrawerActivity(), TeacherListFragment.Listener {

    private val delegate = TeacherListActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        delegate.onCreate(savedInstanceState)
    }

    override fun onTeacherSelected(teacher: Teacher) {
        startActivity(ScheduleActivity.makeIntent(this, teacher))
    }
}
