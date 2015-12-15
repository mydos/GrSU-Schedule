package by.kirich1409.grsuschedule.schedule.pager

import android.os.Bundle
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.network.request.TeacherScheduleRequest
import java.util.*
import kotlin.test.assertTrue

/**
 * Created by kirillrozov on 10/9/15.
 */
class TeacherSchedulePagerFragment : SchedulePagerFragment() {

    private var teacherId = 0
    private lateinit var cacheKeyValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teacherId = arguments.getInt(ARG_TEACHER_ID)
        assertTrue(teacherId > 0, "Teacher id must be positive value")
        cacheKeyValue = "teacherId=$teacherId"
    }

    override fun getCacheKey() = cacheKeyValue

    override fun newScheduleRequest(startDate: Date?, endDate: Date?) =
            TeacherScheduleRequest(teacherId, startDate, endDate)

    companion object {
        private val ARG_TEACHER_ID = if (BuildConfig.DEBUG) "teacherId" else "a"

        public fun newInstance(groupId: Int): TeacherSchedulePagerFragment {
            val fragment = TeacherSchedulePagerFragment()
            fragment.arguments = Bundle(1).apply { putInt(ARG_TEACHER_ID, groupId) }
            return fragment
        }
    }
}