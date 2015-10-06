package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.LessonGroup
import by.kirich1409.grsuschedule.utils.bindView

/**
 * Created by kirillrozov on 9/27/15.
 */
public class LessonGroupViewHolder(context: Context, itemView: View) :
        BaseRecyclerItemViewHolder(context, itemView) {

    private val titleView: TextView by bindView(R.id.lesson_title)
    private val placeView: TextView by bindView(R.id.lesson_place)
    private val timeView: TextView by bindView(R.id.lesson_time)

    var lesson: LessonGroup? = null
        set(lesson) {
            field = lesson

            titleView.text = lesson?.title

            val address = lesson?.address
            placeView.text = if (address.isNullOrEmpty()) {
                null
            } else {
                val room = lesson?.room
                if (room.isNullOrEmpty()) {
                    address
                } else {
                    context.getString(R.string.lesson_place_format, address, room)
                }
            }

            val interval = lesson?.interval
            timeView.text = if (interval == null) {
                null
            } else {
                context.getString(R.string.lesson_time_format, interval.startTime, interval.endTime)
            }
        }
}