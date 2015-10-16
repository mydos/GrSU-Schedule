package by.kirich1409.grsuschedule.network

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class QueryDate(private val date: Date) {

    private val dateString: String by lazy { dateFormat.format(date) }

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(DATE_FORMAT)

    override fun toString() = dateString

    companion object {
        const val DATE_FORMAT: String = "dd.MM.yyyy"
        public fun valueOf(date: Date?): QueryDate? = if (date == null) null else QueryDate(date)

    }
}
