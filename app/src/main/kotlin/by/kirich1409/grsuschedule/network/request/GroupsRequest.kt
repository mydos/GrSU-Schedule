package by.kirich1409.grsuschedule.network.request

import by.kirich1409.grsuschedule.model.Groups
import by.kirich1409.grsuschedule.network.ScheduleService
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupsRequest(val departmentId: Int, val facultyId: Int, val course: Int) :
        RetrofitSpiceRequest<Groups, ScheduleService>(Groups::class.java, ScheduleService::class.java) {

    @Throws(Exception::class)
    override fun loadDataFromNetwork() = service.getGroups(departmentId, facultyId, course)
}
