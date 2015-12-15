package by.kirich1409.grsuschedule.schedule

import android.content.Context
import android.content.SharedPreferences
import by.kirich1409.grsuschedule.app.SimpleAsyncTaskLoader
import by.kirich1409.grsuschedule.model.*
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPreference

/**
 * Created by kirillrozov on 9/23/15.
 */
class ScheduleAdapterDataLoader(context: Context, val schedule: Schedule?) :
        SimpleAsyncTaskLoader<Array<DaySchedule>>(context) {

    private val scheduleDisplayPreference by lazy { ScheduleDisplayPreference(context) }

    private val prefChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (ScheduleDisplayPreference.PREF_SHOW_EMPTY_LESSON == key) {
            onContentChanged()
        }
    }

    override fun loadInBackground(): Array<DaySchedule> {
        if (schedule == null) {
            return emptyArray()
        }

        val days: List<Day> = schedule.days.asList()

        if (days.isEmpty()) {
            return emptyArray()
        } else {
            val showEmptyLesson = scheduleDisplayPreference.showEmptyLesson

            return days.map {
                val groupedLessons = it.lessons.groupBy({ it.interval })
                val dayItems = groupedLessons.keys
                        .sorted()
                        .map({ groupedLessons[it]!!.toList() })
                        .map({ mapLessons(it) })
                        .toArrayList()

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
            for (i in 1 until dayItems.size) {
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
        val lessonsCount = lessons.size
        return when {
            lessonsCount == 1 -> lessons[0]
            lessonsCount > 1 -> {
                val lesson = lessons[0]
                val interval = lesson.interval
                val title = lesson.title
                val lessonEquals: (Lesson) -> Boolean =
                        { it.interval == interval && it.title.equals(title, ignoreCase = true) }
                if (lessons.all(lessonEquals)) {
                    val alinementLessons =
                            AlinementLessons(interval, title, lessons.toTypedArray())

                    run {
                        val addresses = lessons.map({ it.address }).toSet()
                        if (addresses.size == 1) {
                            alinementLessons.address = addresses.first()
                        }
                    }

                    run {
                        val types = lessons.map({ it.type }).toSet()
                        if (types.size == 1) {
                            alinementLessons.type = types.first()
                        }
                    }

                    alinementLessons
                } else {
                    ParallelLesson(interval, lessons.toTypedArray())
                }
            }
            else -> throw RuntimeException("Impossible lessons size = $lessonsCount")
        }
    }

    override fun registerListeners() {
        scheduleDisplayPreference.registerOnPreferenceChangeListener(prefChangeListener)
    }

    override fun unregisterListeners() {
        scheduleDisplayPreference.unregisterOnPreferenceChangeListener(prefChangeListener)
    }
}