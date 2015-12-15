package by.kirich1409.grsuschedule.schedule.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.DaySchedule
import by.kirich1409.grsuschedule.schedule.viewholder.DayViewHolder
import by.kirich1409.grsuschedule.utils.inflate

class DateScheduleAdapter(context: Context, data: DaySchedule) : ScheduleAdapter(context, data) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DayViewHolder -> holder.setDaySchedule(schedule)
            else -> super.onBindViewHolder(holder, position - 1)
        }
    }

    override fun getItemCount() = super.getItemCount() + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                R.id.view_type_date ->
                    DayViewHolder(context.inflate(R.layout.item_day_header, parent))

                else -> super.onCreateViewHolder(parent, viewType)
            }

    override fun getItemViewType(position: Int) =
            when (position) {
                0 -> R.id.view_type_date
                else -> super.getItemViewType(position - 1)
            }
}