package by.kirich1409.grsuschedule.network.request


import by.kirich1409.grsuschedule.network.QueryDate
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupScheduleRequest(val groupId: Int, startDate: Date?, endDate: Date?) :
        ScheduleRequest(startDate, endDate) {

    @Throws(Exception::class)
    override fun loadDataFromNetwork() = service.getGroupSchedule(groupId,
            QueryDate.valueOf(startDate), QueryDate.valueOf(endDate))
}
