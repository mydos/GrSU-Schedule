package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.Day
import by.kirich1409.grsuschedule.utils.bindView

/**
 * Created by kirillrozov on 9/15/15.
 */
class DayHeaderViewHolder(context: Context, rootView: View) :
        BaseRecyclerItemViewHolder(context, rootView) {

    val mTextView: TextView by bindView(android.R.id.text1)
    var day: Day? = null
        set(day) {
            field = day
            mTextView.text = day?.formatDate
        }
}
