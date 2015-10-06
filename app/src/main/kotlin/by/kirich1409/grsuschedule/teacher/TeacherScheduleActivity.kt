package by.kirich1409.grsuschedule.teacher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.DrawerActivity
import by.kirich1409.grsuschedule.model.Teacher

/**
 * Created by kirillrozov on 9/14/15.
 */
public class TeacherScheduleActivity : DrawerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        val teacher = intent.getParcelableExtra<Teacher>(EXTRA_TEACHER)
        supportActionBar!!.subtitle = teacher.fullname

        if (savedInstanceState == null) {
            val teacherScheduleFragment = TeacherScheduleFragment.newInstance(teacher.id)
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, teacherScheduleFragment)
                    .commitAllowingStateLoss()
        }
    }

    companion object {
        public  val EXTRA_TEACHER = "teacher"

        public fun makeIntent(context: Context, teacher: Teacher): Intent {
            val intent = Intent(context, TeacherScheduleActivity::class.java)
            intent.putExtra(EXTRA_TEACHER, teacher)
            return intent
        }
    }
}
