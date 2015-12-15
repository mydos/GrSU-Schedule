package by.kirich1409.grsuschedule.schedule.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.*
import by.kirich1409.grsuschedule.schedule.viewholder.AlinementLessonsViewHolder
import by.kirich1409.grsuschedule.schedule.viewholder.FreeTimeViewHolder
import by.kirich1409.grsuschedule.schedule.viewholder.ParallelLessonViewHolder
import by.kirich1409.grsuschedule.schedule.viewholder.TimeLessonViewHolder
import by.kirich1409.grsuschedule.utils.inflate

/**
 * Created by kirillrozov on 9/16/15.
 */
open class ScheduleAdapter(protected val context: Context, protected val schedule: DaySchedule) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TimeLessonViewHolder -> holder.bind(schedule.items[position] as Lesson)
            is ParallelLessonViewHolder ->holder.bind(schedule.items[position] as ParallelLesson)
            is AlinementLessonsViewHolder ->holder.bind(schedule.items[position] as AlinementLessons)
            is FreeTimeViewHolder -> holder.setFreeTime(schedule.items[position] as FreeTime)
        }
    }

    override fun getItemCount() = schedule.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.id.view_type_lesson -> TimeLessonViewHolder(
                    context.inflate(R.layout.item_lesson_with_time, parent))

            R.id.view_type_alinement_lesson -> AlinementLessonsViewHolder(
                    context.inflate(R.layout.item_alinement_lessons, parent))

            R.id.view_type_parallel_lesson -> ParallelLessonViewHolder(
                    context.inflate(R.layout.item_lesson_parallel, parent))

            R.id.view_type_free_time -> FreeTimeViewHolder(
                    context.inflate(R.layout.item_free_time, parent))

            else -> throw RuntimeException("Unknown item view type = $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = schedule.items[position]
        return when (item) {
            is Lesson -> R.id.view_type_lesson
            is ParallelLesson -> R.id.view_type_parallel_lesson
            is AlinementLessons -> R.id.view_type_alinement_lesson
            is FreeTime -> R.id.view_type_free_time
            else -> throw RuntimeException(
                    "Unknown item view type in position=$position for item='$item'")
        }
    }
}
