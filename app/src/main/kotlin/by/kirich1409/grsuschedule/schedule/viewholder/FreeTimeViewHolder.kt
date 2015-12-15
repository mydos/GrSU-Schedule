package by.kirich1409.grsuschedule.schedule.viewholder

import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.FreeTime

/**
 * Created by kirillrozov on 10/10/15.
 */
class FreeTimeViewHolder(rootView: View) : BaseRecyclerItemViewHolder(rootView.context, rootView) {

    internal val mTimeView: TextView

    init {
        mTimeView = rootView.findViewById(R.id.lesson_time) as TextView
    }

    fun setFreeTime(freeTime: FreeTime) {
        val resources = mTimeView.resources
        val interval = freeTime.interval
        mTimeView.text = resources.getString(
                R.string.lesson_time_format, interval.startTime, interval.endTime)
    }
}
