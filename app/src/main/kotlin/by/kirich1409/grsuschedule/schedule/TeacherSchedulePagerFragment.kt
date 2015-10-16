package by.kirich1409.grsuschedule.schedule

import android.os.Bundle
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.network.request.TeacherScheduleRequest
import junit.framework.Assert
import java.util.*

/**
 * Created by kirillrozov on 10/9/15.
 */
class TeacherSchedulePagerFragment : SchedulePagerFragment() {

    private var teacherId: Int = 0
    private lateinit var cacheKeyValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teacherId = arguments.getInt(ARG_TEACHER_ID)
        Assert.assertTrue("Teacher id must be positive value", teacherId > 0)
        cacheKeyValue = "teacherId=$teacherId"
    }

    override fun getCacheKey() = cacheKeyValue

    override fun newScheduleRequest(startDate: Date?, endDate: Date?) =
            TeacherScheduleRequest(teacherId, startDate, endDate)

    companion object {
        private val ARG_TEACHER_ID = if (BuildConfig.DEBUG) "teacherId" else "a"

        public fun newInstance(groupId: Int): TeacherSchedulePagerFragment {
            val args = Bundle(1)
            args.putInt(ARG_TEACHER_ID, groupId)

            val fragment = TeacherSchedulePagerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}