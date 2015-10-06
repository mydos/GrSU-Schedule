package by.kirich1409.grsuschedule.preference

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseActivity
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.student.GroupPickerActivity
import by.kirich1409.grsuschedule.teacher.TeacherPickerActivity
import by.kirich1409.grsuschedule.utils.APP_MODE_STUDENT
import by.kirich1409.grsuschedule.utils.APP_MODE_TEACHER

/**
 * Created by kirillrozov on 9/14/15.
 */
public class ModeChooserActivity : BaseActivity() {

    val appPreference: AppPreference by lazy { AppPreference(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_picker)
        findViewById(R.id.mode_student)
                .setOnClickListener {
                    startActivityForResult(
                            Intent(this, GroupPickerActivity::class.java), REQUEST_PICK_GROUP) }
        findViewById(R.id.mode_teacher)
                .setOnClickListener {
                    startActivityForResult(
                            Intent(this, TeacherPickerActivity::class.java), REQUEST_PICK_TEACHER) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_GROUP) {
            if (resultCode == Activity.RESULT_OK) {
                val group: Group = data!!.getParcelableExtra(GroupPickerActivity.EXTRA_GROUP)
                appPreference.mode = APP_MODE_STUDENT
                appPreference.group = group

                setResult(Activity.RESULT_OK)
                finish()
            }
        } else if (requestCode == REQUEST_PICK_TEACHER) {
            if (resultCode == Activity.RESULT_OK) {
                val teacher: Teacher =
                        data!!.getParcelableExtra(TeacherPickerActivity.EXTRA_TEACHER)
                appPreference.mode = APP_MODE_TEACHER
                appPreference.teacher = teacher

                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    companion object {
        private val REQUEST_PICK_TEACHER = 1001
        private val REQUEST_PICK_GROUP = 1002

        public fun makeIntent(context: Context): Intent {
            return Intent(context, ModeChooserActivity::class.java)
        }
    }
}
