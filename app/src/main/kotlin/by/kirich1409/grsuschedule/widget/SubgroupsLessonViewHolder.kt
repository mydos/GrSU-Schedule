package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.utils.bindView
import by.kirich1409.grsuschedule.utils.inflate

/**
 * Created by kirillrozov on 9/21/15.
 */
public class SubgroupsLessonViewHolder(
        context: Context,
        rootView: View,
        private val lessonBinderListItem: (ViewGroup) -> LessonViewHolder) :
        BaseRecyclerItemViewHolder(context, rootView) {

    private val timeView: TextView by bindView(R.id.lesson_time)
    private val lessonsView: ViewGroup by bindView(R.id.lessons_container)
    var lessons: Array<Lesson>? = null
        set(lessons) {
            field = lessons
            val interval = if (lessons == null) null else lessons[0].interval
            timeView.text = if (interval == null) {
                null
            } else {
                context.getString(R.string.lesson_time_format, interval.startTime, interval.endTime)
            }

            lessonsView.removeAllViews()
            if (lessons != null) {
                for (lesson in lessons) {
                    val viewHolder = lessonBinderListItem(lessonsView)
                    viewHolder.lesson = lesson
                    lessonsView.addView(viewHolder.itemView)
                }
            }
        }

    companion object {
        public fun newInstance(context: Context, parent: ViewGroup?,
                               itemBinder: (ViewGroup) -> LessonViewHolder): SubgroupsLessonViewHolder {
            val view = context.inflate(R.layout.item_lesson_time, parent) as ViewGroup
            view.addView(context.inflate(R.layout.item_subgroups_lesson_content, view))
            return SubgroupsLessonViewHolder(context, view, itemBinder)
        }
    }
}