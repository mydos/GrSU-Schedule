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
 * Created by kirillrozov on 9/15/15.
 */
public class GroupTimeLessonViewHolder private constructor(context: Context, rootView: View) :
        GroupLessonViewHolder(context, rootView) {

    private val timeView: TextView by bindView(R.id.lesson_time)
    override var lesson: Lesson? = null
        set(lesson) {
            super.lesson = lesson
            val interval = lesson?.interval
            timeView.text = if (interval == null) {
                null
            } else {
                context.getString(R.string.lesson_time_format,
                        interval.startTime, interval.endTime)
            }
        }

    companion object {
        public fun newInstance(context: Context, parent: ViewGroup): GroupLessonViewHolder {
            val view = context.inflate(R.layout.item_lesson_time, parent) as ViewGroup
            val content = context.inflate(R.layout.item_group_lesson_content, view)
            view.addView(content)

            return GroupTimeLessonViewHolder(context, view)
        }
    }
}
