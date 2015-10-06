package by.kirich1409.grsuschedule.network

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class QueryDate(private val date: Date) {

    private val dateString: String by lazy { mDateFormat.format(date) }

    @SuppressLint("SimpleDateFormat")
    private val mDateFormat = SimpleDateFormat(DATE_FORMAT)

    override fun toString(): String {
        return dateString
    }

    companion object {
        public val DATE_FORMAT: String = "dd.MM.yyyy"
    }
}
