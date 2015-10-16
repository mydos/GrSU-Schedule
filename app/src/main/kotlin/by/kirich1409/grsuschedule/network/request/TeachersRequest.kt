package by.kirich1409.grsuschedule.network.request

import by.kirich1409.grsuschedule.model.Teachers
import by.kirich1409.grsuschedule.network.ScheduleService
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest

/**
 * Created by kirillrozov on 9/13/15.
 */
public class TeachersRequest(val extended: Boolean = true) :
        RetrofitSpiceRequest<Teachers, ScheduleService>(
                Teachers::class.java, ScheduleService::class.java) {

    @Throws(Exception::class)
    override fun loadDataFromNetwork() = service.getTeachers(extended)
}
