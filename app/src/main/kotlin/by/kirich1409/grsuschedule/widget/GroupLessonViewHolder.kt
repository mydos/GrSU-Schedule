package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.utils.bindView
import by.kirich1409.grsuschedule.utils.inflate
import by.kirich1409.grsuschedule.utils.setTextOrHideIfEmpty

/**
 * Created by kirillrozov on 9/15/15.
 */
open class GroupLessonViewHolder(context: Context, rootView: View) : LessonViewHolder(context, rootView) {

    val teacherView: TextView by bindView(R.id.lesson_teacher)
    val subgroupView: TextView by bindView(R.id.lesson_subgroup)

    override var lesson: Lesson?
        get() = super.lesson
        set(lesson) {
            super.lesson = lesson
            if (lesson == null) {
                return
            }

            teacherView.text = lesson.teacher?.fullname
            if (lesson.physicalCulture) {
                subgroupView.visibility = View.GONE
            } else {
                subgroupView.setTextOrHideIfEmpty(lesson.subgroup?.title)
            }
        }

    companion object {
        public fun newInstance(context: Context, parent: ViewGroup?): GroupLessonViewHolder {
            val view = context.inflate(R.layout.item_group_lesson_content, parent)
            return GroupLessonViewHolder(context, view)
        }
    }
}
