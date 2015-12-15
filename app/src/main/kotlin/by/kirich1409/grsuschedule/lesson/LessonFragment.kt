package by.kirich1409.grsuschedule.lesson

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.schedule.ScheduleActivity
import junit.framework.Assert.assertNotNull

/**
 * Created by kirillrozov on 12/12/15.
 */
class LessonFragment : Fragment() {
    private var mLesson: Lesson? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        assertNotNull(args)
        mLesson = args.getParcelable<Lesson>(ARG_LESSON)
        assertNotNull(mLesson)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_lesson, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        run {
            val teacher = mLesson!!.teacher
            val teacherName = teacher?.fullName
            val teacherFormView = view.findViewById(R.id.teacher_form)
            if (setText(teacherFormView, R.id.teacher, teacherName)) {
                teacherFormView.setOnClickListener {
                    assertNotNull(teacher)
                    startActivity(
                            ScheduleActivity.makeIntent(context, teacher!!, false))
                }
            }
        }

        val addressView = view.findViewById(R.id.address_form)
        if (setText(addressView, R.id.address, mLesson!!.fullAddress)) {
            addressView.setOnClickListener {
                val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + mLesson!!.mapAddress!!))
                if (intent.resolveActivity(activity.packageManager) != null) {
                    startActivity(intent)
                }
            }
        }

        run {
            setText(view, R.id.subgroup_form, R.id.subgroup, mLesson?.subgroup?.title)
        }

        run {
            val groups = mLesson!!.groups
            val groupsText = if (groups == null || groups.isEmpty()) {
                null
            } else {
                TextUtils.join(getText(R.string.groups_delimiter), groups)
            }
            setText(view, R.id.groups_form, R.id.groups, groupsText)
        }
    }

    companion object {

        val ARG_LESSON = "lesson"

        fun newInstance(lesson: Lesson): LessonFragment {
            val args = Bundle(1)
            args.putParcelable(ARG_LESSON, lesson)

            val fragment = LessonFragment()
            fragment.arguments = args
            return fragment
        }

        private fun setText(formView: View, @IdRes viewId: Int, text: CharSequence?): Boolean {
            if (TextUtils.isEmpty(text)) {
                formView.visibility = View.GONE
                return false
            } else {
                val textView = formView.findViewById(viewId) as TextView
                textView.text = text
                formView.visibility = View.VISIBLE
                return true
            }
        }

        private fun setText(view: View, @IdRes formId: Int, @IdRes viewId: Int, text: CharSequence?): Boolean {
            return setText(view.findViewById(formId), viewId, text)
        }
    }

}
