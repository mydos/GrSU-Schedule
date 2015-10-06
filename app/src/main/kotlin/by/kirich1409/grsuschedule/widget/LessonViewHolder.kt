package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.utils.bindView
import by.kirich1409.grsuschedule.utils.setTextOrHideIfEmpty

/**
 * Created by kirillrozov on 9/16/15.
 */
abstract class LessonViewHolder(context: Context, itemView: View) :
        BaseRecyclerItemViewHolder(context, itemView) {

    private val titleView: TextView by bindView(R.id.lesson_title)
    private val placeView: TextView by bindView(R.id.lesson_place)
    private val typeView: TextView by bindView(R.id.lesson_type)

    open var lesson: Lesson? = null
        set(lesson) {
            field = lesson

            titleView.text = lesson?.title
            typeView.text = lesson?.type

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

            typeView.setTextOrHideIfEmpty(lesson?.type)
        }
}