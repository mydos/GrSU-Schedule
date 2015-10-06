package by.kirich1409.grsuschedule.network.request


import by.kirich1409.grsuschedule.model.Schedule
import by.kirich1409.grsuschedule.network.ScheduleService
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public abstract class ScheduleRequest(val startDate: Date? = null, val endDate: Date? = null) :
        RetrofitSpiceRequest<Schedule, ScheduleService>(
                Schedule::class.java, ScheduleService::class.java) {
}
