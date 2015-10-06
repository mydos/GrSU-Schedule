package by.kirich1409.grsuschedule.network.request

import by.kirich1409.grsuschedule.model.Faculties
import by.kirich1409.grsuschedule.network.ScheduleService
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest

/**
 * Created by kirillrozov on 9/13/15.
 */
public class FacultiesRequest :
        RetrofitSpiceRequest<Faculties, ScheduleService>(
                Faculties::class.java, ScheduleService::class.java) {

    @Throws(Exception::class)
    override fun loadDataFromNetwork(): Faculties {
        return service.getFaculties()
    }
}
