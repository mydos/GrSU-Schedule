package by.kirich1409.grsuschedule.schedule.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.ParallelLesson

/**
 * Created by kirillrozov on 10/14/15.
 */
class ParallelLessonViewHolder(rootView: View) : BaseRecyclerItemViewHolder(rootView.context, rootView) {

    private val mTimeView: TextView
    private val mLessonsView: ViewGroup

    init {
        mTimeView = rootView.findViewById(R.id.lesson_time) as TextView
        mLessonsView = rootView.findViewById(R.id.lessons_container) as ViewGroup
    }

    fun bind(lesson: ParallelLesson) {
        val interval = lesson.interval
        mTimeView.text = mTimeView.resources.getString(
                R.string.lesson_time_format, interval.startTime, interval.endTime)

        mLessonsView.removeAllViews()
        val lessons = lesson.lessons

        val layoutInflater = LayoutInflater.from(context)
        //noinspection ForLoopReplaceableByForEach
        for (i in lessons.indices) {
            val view = layoutInflater.inflate(R.layout.item_lesson, mLessonsView, false)
            val viewHolder = LessonViewHolder(view)
            viewHolder.bind(lessons[i])
            mLessonsView.addView(view)
        }
    }
}
