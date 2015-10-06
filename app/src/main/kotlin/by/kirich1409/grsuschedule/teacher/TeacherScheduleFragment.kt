package by.kirich1409.grsuschedule.teacher

import android.os.Bundle
import android.view.ViewGroup
import by.kirich1409.grsuschedule.network.request.TeacherScheduleRequest
import by.kirich1409.grsuschedule.schedule.ScheduleFragment
import by.kirich1409.grsuschedule.widget.LessonViewHolder
import by.kirich1409.grsuschedule.widget.TeacherLessonViewHolder
import by.kirich1409.grsuschedule.widget.TeacherTimeLessonViewHolder
import junit.framework.Assert
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class TeacherScheduleFragment : ScheduleFragment() {

    private var teacherId: Int = -1
    override lateinit var cacheKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teacherId = arguments.getInt(ARG_TEACHER_ID)
        Assert.assertTrue(teacherId > 0)
        cacheKey = "teacherId=$teacherId"
    }

    override fun newScheduleRequest(startDate: Date?, endDate: Date?) =
            TeacherScheduleRequest(teacherId, startDate, endDate)

    override fun newLessonViewHolder(parent: ViewGroup): LessonViewHolder =
            TeacherTimeLessonViewHolder.newInstance(context, parent)

    override fun newLessonsGroupItemViewHolder(parent: ViewGroup): LessonViewHolder =
            TeacherLessonViewHolder.newInstance(context, parent)

    companion object {
        const val ARG_TEACHER_ID = "teacherId"

        public fun newInstance(teacherId: Int): TeacherScheduleFragment {
            val args = Bundle(1)
            args.putInt(ARG_TEACHER_ID, teacherId)

            val fragment = TeacherScheduleFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
