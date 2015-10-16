package by.kirich1409.grsuschedule

import android.app.Application
import by.kirich1409.grsuschedule.utils.Constants
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class ScheduleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Locale.setDefault(Constants.LOCALE_RU)
    }
}
