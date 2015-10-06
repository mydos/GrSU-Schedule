package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.utils.bindView
import by.kirich1409.grsuschedule.utils.inflate

/**
 * Created by kirillrozov on 9/16/15.
 */
public class TeacherTimeLessonViewHolder(context: Context, itemView: View) :
        TeacherLessonViewHolder(context, itemView) {

    private val timeView: TextView by bindView(R.id.lesson_time)
    override var lesson: Lesson?
        get() = super.lesson
        set(lesson) {
            super.lesson = lesson
            val interval = lesson?.interval
            timeView.text = if (interval == null) {
                null
            } else {
                context.getString(R.string.lesson_time_format, interval.startTime, interval.endTime)
            }
        }

    companion object {
        public fun newInstance(context: Context, parent: ViewGroup): TeacherLessonViewHolder {
            val view = context.inflate(R.layout.item_lesson_time, parent) as ViewGroup
            view.addView(context.inflate(R.layout.item_teacher_lesson_content, view))
            return TeacherTimeLessonViewHolder(context, view)
        }
    }
}