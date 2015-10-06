package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Day
import by.kirich1409.grsuschedule.model.FreeTime
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.model.LessonGroup
import by.kirich1409.grsuschedule.utils.inflate

/**
 * Created by kirillrozov on 9/16/15.
 */
public class ScheduleAdapter(
        private val context: Context,
        private val data: ScheduleAdapter.Data,
        private val lessonBinder: (ViewGroup) -> LessonViewHolder,
        private val lessonBinderListItem: (ViewGroup) -> LessonViewHolder) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LessonViewHolder -> holder.lesson = data.items[position] as Lesson
            is DayHeaderViewHolder -> holder.day = data.items[position] as Day
            is SubgroupsLessonViewHolder -> holder.lessons = data.items[position] as Array<Lesson>
            is LessonGroupViewHolder -> holder.lesson = data.items[position] as LessonGroup
            is FreeTimeViewHolder -> holder.freeTime = data.items[position] as FreeTime
        }
    }

    override fun getItemCount(): Int = data.items.size()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.id.view_type_lesson -> lessonBinder(parent)
            R.id.view_type_day_header ->
                DayHeaderViewHolder(context, context.inflate(R.layout.item_day_header, parent))

            R.id.view_type_lesson_group ->
                LessonGroupViewHolder(context, context.inflate(R.layout.item_lesson_group, parent))

            R.id.view_type_subgroups_lesson ->
                SubgroupsLessonViewHolder.newInstance(context, parent, lessonBinderListItem)

            R.id.view_type_free_time ->
                FreeTimeViewHolder(context, context.inflate(R.layout.item_free_time, parent))

            else -> throw RuntimeException("Unknown item view type = $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = data.items[position]
        return when (item) {
            is Lesson -> R.id.view_type_lesson
            is Day -> R.id.view_type_day_header
            is Array<Lesson> -> R.id.view_type_subgroups_lesson
            is LessonGroup -> R.id.view_type_lesson_group
            is FreeTime -> R.id.view_type_free_time
            else -> throw RuntimeException(
                    "Unknown item view type in position=$position for item='$item'")
        }
    }

    public class Data(val items: Array<Any>)
}