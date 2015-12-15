package by.kirich1409.grsuschedule.schedule.list

import android.os.Bundle
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.network.request.GroupScheduleRequest
import java.util.*
import kotlin.test.assertTrue

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupScheduleListFragment : ScheduleListFragment() {

    private var groupId = 0
    private lateinit var cacheKeyValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments.getInt(ARG_GROUP_ID)
        assertTrue(groupId > 0, "Group id must be positive value")
        cacheKeyValue = "groupId=$groupId"
    }

    override fun getCacheKey() = cacheKeyValue

    override fun newScheduleRequest(startDate: Date?, endDate: Date?) =
            GroupScheduleRequest(groupId, startDate, endDate)

    companion object {
        val ARG_GROUP_ID = if (BuildConfig.DEBUG) "groupId" else "a"

        public fun newInstance(groupId: Int): GroupScheduleListFragment {
            val fragment = GroupScheduleListFragment()
            fragment.arguments = Bundle(1).apply { putInt(ARG_GROUP_ID, groupId) }
            return fragment
        }
    }

}
