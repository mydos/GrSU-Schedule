package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.FreeTime
import by.kirich1409.grsuschedule.utils.bindView

/**
 * Created by kirillrozov on 9/25/15.
 */
public class FreeTimeViewHolder(context: Context, rootView: View) :
        BaseRecyclerItemViewHolder(context, rootView) {

    val textView: TextView by bindView(android.R.id.text1)
    var freeTime: FreeTime? = null
        set(freeTime) {
            field = freeTime
            val interval = freeTime?.interval
            if (interval != null) {
                textView.text = context.getString(
                        R.string.free_time_format, interval.startTime, interval.endTime)
            } else {
                textView.text = null
            }
        }
}