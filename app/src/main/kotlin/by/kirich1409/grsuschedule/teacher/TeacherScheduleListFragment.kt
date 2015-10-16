package by.kirich1409.grsuschedule.teacher

import android.os.Bundle
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.network.request.TeacherScheduleRequest
import by.kirich1409.grsuschedule.schedule.ScheduleListFragment
import junit.framework.Assert
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class TeacherScheduleListFragment : ScheduleListFragment() {

    private var teacherId: Int = -1
    private lateinit var cacheKeyValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teacherId = arguments.getInt(ARG_TEACHER_ID)
        Assert.assertTrue(teacherId > 0)
        cacheKeyValue = "teacherId=$teacherId"
    }

    override fun getCacheKey() = cacheKeyValue

    override fun newScheduleRequest(startDate: Date?, endDate: Date?) =
            TeacherScheduleRequest(teacherId, startDate, endDate)

    companion object {
        val ARG_TEACHER_ID = if (BuildConfig.DEBUG) "teacherId" else "a"

        public fun newInstance(teacherId: Int): TeacherScheduleListFragment {
            val args = Bundle(1)
            args.putInt(ARG_TEACHER_ID, teacherId)

            val fragment = TeacherScheduleListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
