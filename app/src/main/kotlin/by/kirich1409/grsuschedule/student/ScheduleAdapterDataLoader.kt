package by.kirich1409.grsuschedule.student

import android.content.Context
import android.content.SharedPreferences
import by.kirich1409.grsuschedule.app.SimpleAsyncTaskLoader
import by.kirich1409.grsuschedule.model.Day
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.model.Schedule
import by.kirich1409.grsuschedule.model.TimeInterval
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPreference
import by.kirich1409.grsuschedule.schedule.AlinementLessons
import by.kirich1409.grsuschedule.schedule.DaySchedule
import by.kirich1409.grsuschedule.schedule.FreeTime
import by.kirich1409.grsuschedule.schedule.ParallelLesson
import by.kirich1409.grsuschedule.utils.Constants
import java.util.*

/**
 * Created by kirillrozov on 9/23/15.
 */
class ScheduleAdapterDataLoader(context: Context, val schedule: Schedule?) :
        SimpleAsyncTaskLoader<Array<DaySchedule>>(context) {

    private val scheduleDisplayPreference by lazy { ScheduleDisplayPreference(context) }
    private val prefChangeListener = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String) {
            if (ScheduleDisplayPreference.PREF_ONLY_ACTUAL_SCHEDULE == key
                    || ScheduleDisplayPreference.PREF_SHOW_EMPTY_LESSON == key) {
                onContentChanged()
            }
        }
    }

    override fun loadInBackground(): Array<DaySchedule> {
        if (schedule == null) {
            return emptyArray()
        }

        val days: List<Day> = getScheduleDays(schedule)

        if (days.isEmpty()) {
            return emptyArray()
        } else {
            val showEmptyLesson = scheduleDisplayPreference.showEmptyLesson

            return days.map {
                val groupedLessons = it.lessons.groupBy({ it.interval })
                val dayItems = groupedLessons.keySet()
                        .sorted()
                        .map({ groupedLessons.get(it)!!.toList() })
                        .map({ mapLessons(it) })
                        .toLinkedList()

                if (showEmptyLesson) {
                    addEmptyLessons(dayItems)
                }
                DaySchedule(it.date, dayItems.toTypedArray())
            }.toTypedArray()
        }
    }

    private fun addEmptyLessons(dayItems: MutableList<DaySchedule.Item>) {
        if (dayItems.isNotEmpty()) {
            var prevLesson: Lesson? = dayItems[0].getFirstLesson()
            for (i in 1 until dayItems.size()) {
                val item = dayItems[i].getFirstLesson()
                if (prevLesson is Lesson && item is Lesson) {
                    val interval = TimeInterval(
                            prevLesson.interval.endTime, item.interval.startTime)
                    if (interval.durationInMinutes > Lesson.DURATION) {
                        dayItems.add(i, FreeTime(interval))
                    }
                }
                prevLesson = item
            }
        }
    }

    private fun mapLessons(lessons: List<Lesson>): DaySchedule.Item {
        val lessonsCount = lessons.size()
        return when {
            lessonsCount == 1 -> lessons[0]
            lessonsCount > 1 -> {
                val lesson = lessons.get(0)
                val interval = lesson.interval
                val title = lesson.title
                val lessonEquals: (Lesson) -> Boolean =
                        { it.interval == interval && it.title.equals(title, ignoreCase = true) }
                if (lessons.all(lessonEquals)) {
                    AlinementLessons(interval, title, lessons.toTypedArray())
                } else {
                    ParallelLesson(interval, lessons.toTypedArray())
                }
            }
            else -> throw RuntimeException()
        }
    }

    private fun getScheduleDays(schedule: Schedule): List<Day> {
        return if (scheduleDisplayPreference.onlyActualSchedule) {
            val now = Calendar.getInstance(Constants.LOCALE_RU)
            val dateCal = Calendar.getInstance(Constants.LOCALE_RU)
            schedule.days.filter {
                dateCal.time = it.date.toDate()
                now.get(Calendar.YEAR) <= dateCal.get(Calendar.YEAR)
                        && now.get(Calendar.DAY_OF_YEAR) <= dateCal.get(Calendar.DAY_OF_YEAR)
            }
        } else {
            schedule.days.asList()
        }
    }

    override fun registerListeners() {
        scheduleDisplayPreference.registerOnPreferenceChangeListener(prefChangeListener)
    }

    override fun unregisterListeners() {
        scheduleDisplayPreference.unregisterOnPreferenceChangeListener(prefChangeListener)
    }
}