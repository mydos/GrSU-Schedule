package by.kirich1409.grsuschedule.widget.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.AlinementLessonActivity
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.model.TimeInterval
import by.kirich1409.grsuschedule.schedule.AlinementLessons
import by.kirich1409.grsuschedule.schedule.DaySchedule
import by.kirich1409.grsuschedule.schedule.FreeTime
import by.kirich1409.grsuschedule.schedule.ParallelLesson
import by.kirich1409.grsuschedule.utils.inflate
import by.kirich1409.grsuschedule.utils.setTextOrHideIfEmpty
import by.kirich1409.grsuschedule.widget.DateViewHolder
import by.kirich1409.grsuschedule.widget.FreeTimeViewHolder

/**
 * Created by kirillrozov on 9/16/15.
 */
open class ScheduleAdapter(protected val context: Context, protected val schedule: DaySchedule) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LessonViewHolder -> holder.setLesson(schedule.items[position] as Lesson)
            is ParallelLessonViewHolder ->
                holder.setLessons(schedule.items[position] as ParallelLesson)
            is AlinementLessonsViewHolder ->
                holder.setLessons(schedule.items[position] as AlinementLessons)
            is FreeTimeViewHolder -> holder.setFreeTime(schedule.items[position] as FreeTime)
        }
    }

    override fun getItemCount(): Int = schedule.items.size()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.id.view_type_lesson -> TimeLessonViewHolder(
                    context, context.inflate(R.layout.item_lesson_with_time, parent))

            R.id.view_type_alinement_lesson -> AlinementLessonsViewHolder(
                    context, context.inflate(R.layout.item_alinement_lessons, parent))

            R.id.view_type_parallel_lesson -> ParallelLessonViewHolder(context,
                    context.inflate(R.layout.item_parallel_lesson, parent))

            R.id.view_type_free_time -> FreeTimeViewHolder(
                    context, context.inflate(R.layout.item_free_time, parent))

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

class DateScheduleAdapter(context: Context, data: DaySchedule) : ScheduleAdapter(context, data) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> holder.setDate(schedule.date)
            else -> super.onBindViewHolder(holder, position - 1)
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                R.id.view_type_date ->
                    DateViewHolder(context, context.inflate(R.layout.item_date, parent))

                else -> super.onCreateViewHolder(parent, viewType)
            }

    override fun getItemViewType(position: Int) =
            when (position) {
                0 -> R.id.view_type_date
                else -> super.getItemViewType(position - 1)
            }
}

public class AlinementLessonsViewHolder(context: Context, rootView: View) :
        BaseRecyclerItemViewHolder(context, rootView) {

    private val titleView = rootView.findViewById(R.id.lesson_title) as TextView
    private val timeView = rootView.findViewById(R.id.lesson_time) as TextView
    private val lessonsCountView = rootView.findViewById(R.id.lessons_count) as TextView

    fun setLessons(alinimenetLessons: AlinementLessons) {
        titleView.text = alinimenetLessons.title
        timeView.text = alinimenetLessons.interval.toString(context)
        lessonsCountView.text = alinimenetLessons.lessons.size().toString()

        itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                v.context.startActivity(AlinementLessonActivity.makeIntent(context, alinimenetLessons))
            }
        })
    }
}

open class LessonViewHolder(context: Context, rootView: View) :
        BaseRecyclerItemViewHolder(context, rootView) {

    private val teacherView = rootView.findViewById(R.id.lesson_teacher) as TextView?
    private val subgroupView = rootView.findViewById(R.id.lesson_subgroup) as TextView
    private val titleView = rootView.findViewById(R.id.lesson_title) as TextView
    private val placeView = rootView.findViewById(R.id.lesson_place) as TextView
    private val typeView = rootView.findViewById(R.id.lesson_type) as TextView

    open fun setLesson(lesson: Lesson) {
        titleView.text = lesson.title
        typeView.text = lesson.type
        teacherView?.setTextOrHideIfEmpty(lesson.teacher?.fullname)
        placeView.text = lesson.fullAddress

        if (lesson.physicalCulture) {
            subgroupView.visibility = View.GONE
        } else {
            subgroupView.setTextOrHideIfEmpty(lesson.subgroup?.title)
        }
    }
}

class TimeLessonViewHolder(context: Context, rootView: View) :
        LessonViewHolder(context, rootView) {

    private val timeView = rootView.findViewById(R.id.lesson_time) as TextView

    override fun setLesson(lesson: Lesson) {
        super.setLesson(lesson)
        timeView.text = lesson.interval.toString(context)
    }
}

class ParallelLessonViewHolder(context: Context, rootView: View) :
        BaseRecyclerItemViewHolder(context, rootView) {

    private val timeView: TextView = rootView.findViewById(R.id.lesson_time) as TextView
    private val lessonsView: ViewGroup = rootView.findViewById(R.id.lessons_container) as ViewGroup

    fun setLessons(lesson: ParallelLesson) {
        timeView.text = lesson.time.toString(context)

        lessonsView.removeAllViews()
        lesson.lessons.forEach {
            val view = context.inflate(R.layout.item_lesson, lessonsView)
            val viewHolder = LessonViewHolder(context, view)
            viewHolder.setLesson(it)
            lessonsView.addView(viewHolder.itemView)
        }
    }
}

fun TimeInterval.toString(context: Context): String {
    val startTime = startTime.toString(context)
    val endTime = endTime.toString(context)
    return context.getString(R.string.lesson_time_format, startTime, endTime)
}
