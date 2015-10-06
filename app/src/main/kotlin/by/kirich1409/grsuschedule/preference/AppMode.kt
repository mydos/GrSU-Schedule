package by.kirich1409.grsuschedule.preference

import android.support.annotation.IntDef
import by.kirich1409.grsuschedule.utils.APP_MODE_STUDENT
import by.kirich1409.grsuschedule.utils.APP_MODE_TEACHER
import by.kirich1409.grsuschedule.utils.APP_MODE_UNKNOWN

/**
 * Created by kirillrozov on 9/27/15.
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(APP_MODE_UNKNOWN.toLong(),
        APP_MODE_STUDENT.toLong(),
        APP_MODE_TEACHER.toLong())
annotation public class AppMode