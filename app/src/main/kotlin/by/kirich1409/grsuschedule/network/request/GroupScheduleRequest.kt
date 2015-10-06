package by.kirich1409.grsuschedule.network.request


import by.kirich1409.grsuschedule.model.Schedule
import by.kirich1409.grsuschedule.network.QueryDate
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupScheduleRequest(val groupId: Int, startDate: Date?, endDate: Date?) :
        ScheduleRequest(startDate, endDate) {

    @Throws(Exception::class)
    override fun loadDataFromNetwork(): Schedule {
        if (endDate != null && startDate != null) {
            return service.getGroupSchedule(groupId, QueryDate(startDate), QueryDate(endDate))
        } else if (startDate != null) {
            return service.getGroupSchedule(groupId, QueryDate(startDate))
        } else {
            return service.getGroupSchedule(groupId)
        }
    }
}
