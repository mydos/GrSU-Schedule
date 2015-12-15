package by.kirich1409.grsuschedule.schedule

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import by.kirich1409.grsuschedule.BuildConfig
import junit.framework.Assert
import java.util.*
import java.util.Calendar.*

/**
 * Created by kirillrozov on 12/12/15.
 */
class ScheduleStartDateDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener: Listener? = null

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val startDate = arguments.getSerializable(ARG_START_DATE) as Date?
        Assert.assertNotNull(startDate)

        val start = Calendar.getInstance()
        start.time = startDate
        val datePickerDialog = DatePickerDialog(context, this,
                start.get(YEAR), start.get(MONTH), start.get(DAY_OF_MONTH))
        val datePicker = datePickerDialog.datePicker
        datePicker.minDate = start.timeInMillis
        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if (listener != null) {
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
            listener!!.onDateSet(calendar.time)
        }
    }

    interface Listener {
        fun onDateSet(date: Date)
    }

    companion object {

        private val ARG_START_DATE = if (BuildConfig.DEBUG) "startDate" else "a"

        fun newInstance(startDate: Date): ScheduleStartDateDialogFragment {
            val fragment = ScheduleStartDateDialogFragment()
            fragment.arguments = Bundle(1).apply({ putSerializable(ARG_START_DATE, startDate) })
            return fragment
        }
    }
}
