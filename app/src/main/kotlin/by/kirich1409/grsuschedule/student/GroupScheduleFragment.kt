package by.kirich1409.grsuschedule.student

import android.os.Bundle
import android.view.ViewGroup
import by.kirich1409.grsuschedule.network.request.GroupScheduleRequest
import by.kirich1409.grsuschedule.schedule.ScheduleFragment
import by.kirich1409.grsuschedule.widget.GroupLessonViewHolder
import by.kirich1409.grsuschedule.widget.GroupTimeLessonViewHolder
import by.kirich1409.grsuschedule.widget.LessonViewHolder
import junit.framework.Assert
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupScheduleFragment : ScheduleFragment() {

    private var groupId: Int = 0
    override lateinit var cacheKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments.getInt(ARG_GROUP_ID)
        Assert.assertTrue("Group id must be positive value", groupId > 0)
        cacheKey = "groupId=$groupId"
    }

    override fun newLessonsGroupItemViewHolder(parent: ViewGroup): LessonViewHolder =
            GroupLessonViewHolder.newInstance(context, parent)

    override fun newLessonViewHolder(parent: ViewGroup): LessonViewHolder =
            GroupTimeLessonViewHolder.newInstance(context, parent)

    override fun newScheduleRequest(startDate: Date?, endDate: Date?) =
            GroupScheduleRequest(groupId, startDate, endDate)

    companion object {
        const val ARG_GROUP_ID = "groupId"

        public fun newInstance(groupId: Int): GroupScheduleFragment {
            val args = Bundle(1)
            args.putInt(ARG_GROUP_ID, groupId)

            val fragment = GroupScheduleFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
