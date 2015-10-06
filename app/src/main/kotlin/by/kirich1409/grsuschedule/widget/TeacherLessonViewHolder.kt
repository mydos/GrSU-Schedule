package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.utils.inflate

/**
 * Created by kirillrozov on 9/16/15.
 */
public open class TeacherLessonViewHolder(context: Context, itemView: View) :
        LessonViewHolder(context, itemView) {

    companion object {
        public fun newInstance(context: Context, parent: ViewGroup): TeacherLessonViewHolder {
            val view = context.inflate(R.layout.item_teacher_lesson_content, parent)
            return TeacherLessonViewHolder(context, view)
        }
    }
}