package by.kirich1409.grsuschedule.teacher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseActivity
import by.kirich1409.grsuschedule.model.Teacher

/**
 * Created by kirillrozov on 9/14/15.
 */
public class TeacherPickerActivity : BaseActivity(), TeacherListFragment.Listener {

    private val delegate = TeacherListActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_toolbar)
        delegate.onCreate(savedInstanceState)
    }

    override fun onTeacherSelected(teacher: Teacher) {
        val result = Intent()
        result.putExtra(EXTRA_TEACHER, teacher)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    companion object {
        public val EXTRA_TEACHER: String = "teacher"
    }
}
