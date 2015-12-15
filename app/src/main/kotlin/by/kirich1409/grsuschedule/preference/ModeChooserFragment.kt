package by.kirich1409.grsuschedule.preference

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.student.GroupPickerActivity
import by.kirich1409.grsuschedule.teacher.TeacherPickerActivity
import by.kirich1409.grsuschedule.utils.APP_MODE_STUDENT
import by.kirich1409.grsuschedule.utils.APP_MODE_TEACHER

/**
 * Created by kirillrozov on 11/9/15.
 */
class ModeChooserFragment : Fragment() {

    val appPreference: AppPreference by lazy { AppPreference(context) }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mode_chooser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById(R.id.mode_student)
                .setOnClickListener({
                    val intent =
                            Intent(context, GroupPickerActivity::class.java)
                    startActivityForResult(intent, REQUEST_PICK_GROUP)
                })

        view.findViewById(R.id.mode_teacher)
                .setOnClickListener({
                    val intent =
                            Intent(context, TeacherPickerActivity::class.java)
                    startActivityForResult(intent, REQUEST_PICK_TEACHER)
                })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val activity = activity
        if (requestCode == REQUEST_PICK_GROUP) {
            if (resultCode == Activity.RESULT_OK) {
                val group: Group = data!!.getParcelableExtra(GroupPickerActivity.EXTRA_GROUP)
                appPreference.mode = APP_MODE_STUDENT
                appPreference.group = group

                activity.setResult(Activity.RESULT_OK)
                activity.finish()
            }
        } else if (requestCode == REQUEST_PICK_TEACHER) {
            if (resultCode == Activity.RESULT_OK) {
                val teacher: Teacher = data!!.getParcelableExtra(TeacherPickerActivity.EXTRA_TEACHER)
                appPreference.mode = APP_MODE_TEACHER
                appPreference.teacher = teacher

                activity.setResult(Activity.RESULT_OK)
                activity.finish()
            }
        }
    }

    companion object {
        private val REQUEST_PICK_TEACHER = 1001
        private val REQUEST_PICK_GROUP = 1002
    }
}