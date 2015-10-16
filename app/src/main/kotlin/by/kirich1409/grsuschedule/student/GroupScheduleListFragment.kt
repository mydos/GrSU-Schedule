package by.kirich1409.grsuschedule.student

import android.os.Bundle
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.network.request.GroupScheduleRequest
import by.kirich1409.grsuschedule.schedule.ScheduleListFragment
import junit.framework.Assert
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupScheduleListFragment : ScheduleListFragment() {

    private var groupId: Int = 0
    private lateinit var cacheKeyValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments.getInt(ARG_GROUP_ID)
        Assert.assertTrue("Group id must be positive value", groupId > 0)
        cacheKeyValue = "groupId=$groupId"
    }

    override fun getCacheKey() = cacheKeyValue

    override fun newScheduleRequest(startDate: Date?, endDate: Date?) =
            GroupScheduleRequest(groupId, startDate, endDate)

    companion object {
        val ARG_GROUP_ID = if (BuildConfig.DEBUG) "groupId" else "a"

        public fun newInstance(groupId: Int): GroupScheduleListFragment {
            val args = Bundle(1)
            args.putInt(ARG_GROUP_ID, groupId)

            val fragment = GroupScheduleListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
