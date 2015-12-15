package by.kirich1409.grsuschedule.schedule.viewholder

import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Lesson

/**
 * Created by kirillrozov on 10/14/15.
 */
class TimeLessonViewHolder(rootView: View) : LessonViewHolder(rootView) {

    private val mTimeView: TextView

    init {
        mTimeView = rootView.findViewById(R.id.lesson_time) as TextView
    }

    override fun bind(lesson: Lesson) {
        super.bind(lesson)
        val interval = lesson.interval
        mTimeView.text = mTimeView.resources.getString(
                R.string.lesson_time_format, interval.startTime, interval.endTime)
    }
}
