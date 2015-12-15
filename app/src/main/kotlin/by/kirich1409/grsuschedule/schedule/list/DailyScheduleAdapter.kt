package by.kirich1409.grsuschedule.schedule.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.DaySchedule
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPreference
import by.kirich1409.grsuschedule.utils.inflate

/**
 * Created by kirillrozov on 10/7/15.
 */
class DailyScheduleAdapter(val context: Context, val data: Array<DaySchedule>) :
        RecyclerView.Adapter<DailyScheduleAdapter.DayScheduleViewHolder>() {

    private val layoutResId = if (ScheduleDisplayPreference(context).isHorizontalNavigation) {
        R.layout.item_day_schedule_horizontal
    } else {
        R.layout.item_day_schedule
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = DayScheduleViewHolder(context, context.inflate(layoutResId, parent))

    override fun onBindViewHolder(holder: DayScheduleViewHolder, position: Int)
            = holder.setData(this.data[position])

    override fun getItemCount() = data.size

    class DayScheduleViewHolder(context: Context, content: View) :
            BaseRecyclerItemViewHolder(context, content) {

        val contentView: ViewGroup = content.findViewById(R.id.content) as ViewGroup

        fun setData(schedule: DaySchedule) {
            contentView.removeAllViews()

            val adapter = DateScheduleAdapter(context, schedule)
            for (position in 0 until adapter.itemCount) {
                val itemViewType = adapter.getItemViewType(position)
                val viewHolder = adapter.onCreateViewHolder(contentView, itemViewType)
                adapter.onBindViewHolder(viewHolder, position)
                contentView.addView(viewHolder.itemView)
            }
        }
    }
}