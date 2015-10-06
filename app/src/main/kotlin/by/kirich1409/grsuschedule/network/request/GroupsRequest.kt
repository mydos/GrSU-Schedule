package by.kirich1409.grsuschedule.network.request

import by.kirich1409.grsuschedule.model.Groups
import by.kirich1409.grsuschedule.network.ScheduleService
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupsRequest(val mDepartmentId: Int, val mFacultyId: Int, val mCourse: Int) :
        RetrofitSpiceRequest<Groups, ScheduleService>(Groups::class.java, ScheduleService::class.java) {

    @Throws(Exception::class)
    override fun loadDataFromNetwork(): Groups {
        return service.getGroups(mDepartmentId, mFacultyId, mCourse)
    }
}
