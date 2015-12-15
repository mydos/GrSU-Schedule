package by.kirich1409.grsuschedule.schedule.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.*
import by.kirich1409.grsuschedule.schedule.viewholder.*
import by.kirich1409.grsuschedule.utils.inflate

/**
 * Created by kirillrozov on 10/7/15.
 */
class PhoneDailyScheduleAdapter(val context: Context, val data: Array<DaySchedule>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = data.flatMap { arrayListOf<Any>(it).apply { addAll(it.items) } }.toTypedArray()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TimeLessonViewHolder -> holder.bind(items[position] as Lesson)
            is ParallelLessonViewHolder -> holder.bind(items[position] as ParallelLesson)
            is AlinementLessonsViewHolder -> holder.bind(items[position] as AlinementLessons)
            is FreeTimeViewHolder -> holder.setFreeTime(items[position] as FreeTime)
            is DayViewHolder -> holder.setDaySchedule(items[position] as DaySchedule)
        }
    }

    override fun getItemCount() = items.size

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

            R.id.view_type_date -> DayViewHolder(
                    context.inflate(R.layout.item_day_header, parent)
            )

            else -> throw RuntimeException("Unknown item view type = $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when (item) {
            is Lesson -> R.id.view_type_lesson
            is ParallelLesson -> R.id.view_type_parallel_lesson
            is AlinementLessons -> R.id.view_type_alinement_lesson
            is FreeTime -> R.id.view_type_free_time
            is DaySchedule -> R.id.view_type_date
            else -> throw RuntimeException(
                    "Unknown item view type in position=$position for item='$item'")
        }
    }
}