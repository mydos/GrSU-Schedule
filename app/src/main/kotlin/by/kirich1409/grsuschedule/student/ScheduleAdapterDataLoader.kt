package by.kirich1409.grsuschedule.student

import android.content.Context
import android.content.SharedPreferences
import by.kirich1409.grsuschedule.app.SimpleAsyncTaskLoader
import by.kirich1409.grsuschedule.model.*
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPreference
import by.kirich1409.grsuschedule.utils.Constants
import by.kirich1409.grsuschedule.widget.ScheduleAdapter
import java.util.*

/**
 * Created by kirillrozov on 9/23/15.
 */
class ScheduleAdapterDataLoader(context: Context, val schedule: Schedule?) :
        SimpleAsyncTaskLoader<ScheduleAdapter.Data>(context) {

    private val scheduleDisplayPreference by lazy { ScheduleDisplayPreference(context) }
    private val prefChangeListener = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String) {
            if (ScheduleDisplayPreference.PREF_ONLY_ACTUAL_SCHEDULE == key
                    || ScheduleDisplayPreference.PREF_SHOW_EMPTY_LESSON == key) {
                onContentChanged()
            }
        }
    }

    override fun loadInBackground(): ScheduleAdapter.Data? {
        if (schedule == null) {
            return ScheduleAdapter.Data(emptyArray())
        }

        val days: List<Day> = getScheduleDays(schedule)

        if (days.isEmpty()) {
            return ScheduleAdapter.Data(emptyArray())
        } else {
            val showEmptyLesson = scheduleDisplayPreference.showEmptyLesson
            val items = ArrayList<Any>((days.size() + 1) * 4 + 5)

            days.forEach {
                val groupedLessons = it.lessons.groupBy({ it.interval })
                items.add(it)
                val dayItems = groupedLessons.keySet()
                        .sorted()
                        .map({ groupedLessons.get(it)!!.toList() })
                        .map({ mapLessons(it) })
                        .toLinkedList()


                if (showEmptyLesson) {
                    addEmptyLessons(dayItems)
                }
                items.addAll(dayItems)
            }

            return ScheduleAdapter.Data(items.toArray())
        }
    }

    private fun addEmptyLessons(dayItems: LinkedList<Any>) {
        if (dayItems.isNotEmpty()) {
            var prevLesson: Lesson? = getLesson(dayItems[0])
            for (i in 1 until dayItems.size()) {
                val item = getLesson(dayItems[i])
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

    private fun mapLessons(it: List<Lesson>): Any {
        return when {
            it.size() == 1 -> it[0]
            it.size() > 1 -> {
                val lesson = it.get(0)
                val interval = lesson.interval
                val title = lesson.title
                val address = lesson.address
                val room = lesson.room
                if (it.all({
                    it.interval == interval
                            && it.title == title
                            && it.address == address
                            && it.room == room
                })) {
                    LessonGroup(interval, title, address, room, it.toTypedArray())
                } else {
                    it.toTypedArray()
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
                dateCal.time = it.date
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

    private fun getLesson(item: Any?): Lesson? {
        return when (item) {
            is Lesson -> item
            is Array<Lesson> -> item[0]
            is LessonGroup -> item.lessons[0]
            else -> null
        }
    }
}