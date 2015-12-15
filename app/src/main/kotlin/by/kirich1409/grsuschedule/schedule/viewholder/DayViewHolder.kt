package by.kirich1409.grsuschedule.schedule.viewholder

import android.view.View
import android.widget.TextView

import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.DaySchedule

/**
 * Created by kirillrozov on 10/10/15.
 */
class DayViewHolder(rootView: View) : BaseRecyclerItemViewHolder(rootView.context, rootView) {

    private val mDayView: TextView

    init {
        mDayView = rootView.findViewById(R.id.day) as TextView
    }

    fun setDaySchedule(daySchedule: DaySchedule) {
        mDayView.text = daySchedule.date.format(true, false)
    }
}
