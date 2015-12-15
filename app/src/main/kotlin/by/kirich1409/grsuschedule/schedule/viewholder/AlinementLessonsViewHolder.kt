package by.kirich1409.grsuschedule.schedule.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseRecyclerItemViewHolder
import by.kirich1409.grsuschedule.lesson.AlinementLessonActivity
import by.kirich1409.grsuschedule.model.AlinementLessons
import java.util.*

/**
 * Created by kirillrozov on 10/14/15.
 */
class AlinementLessonsViewHolder(rootView: View) : BaseRecyclerItemViewHolder(rootView.context, rootView) {

    private val mTitleView: TextView
    private val mTimeView: TextView
    private val mSubgroupsView: TextView
    private val mPlaceView: TextView
    private val mTypeView: TextView

    private var mLessons: AlinementLessons? = null

    init {
        mTitleView = rootView.findViewById(R.id.lesson_title) as TextView
        mTimeView = rootView.findViewById(R.id.lesson_time) as TextView
        mSubgroupsView = rootView.findViewById(R.id.lesson_subgroups) as TextView
        mPlaceView = rootView.findViewById(R.id.lesson_place) as TextView
        mTypeView = rootView.findViewById(R.id.lesson_type) as TextView

        rootView.setOnClickListener { v ->
            val lessons = mLessons
            if (lessons != null) {
                v.context.startActivity(
                        AlinementLessonActivity.makeIntent(v.context, lessons))
            }
        }
    }

    fun bind(lessons: AlinementLessons) {
        mLessons = lessons

        mTitleView.text = lessons.title

        val interval = lessons.interval
        mTimeView.text = mTimeView.resources.getString(
                R.string.lesson_time_format, interval.startTime, interval.endTime)

        run {
            val allLessons = lessons.lessons
            val groups = ArrayList<String>(allLessons.size * 2)
            //noinspection ForLoopReplaceableByForEach
            for (i in allLessons.indices) {
                val subgroup = allLessons[i].subgroup
                if (subgroup != null) {
                    groups.add(subgroup.title)
                } else {
                    groups.add(allLessons[i].title)
                }
            }
            mSubgroupsView.text = TextUtils.join(", ", groups)
        }

        run {
            val address = lessons.address
            if (TextUtils.isEmpty(address)) {
                mPlaceView.visibility = View.GONE
            } else {
                mPlaceView.text = address
                mPlaceView.visibility = View.VISIBLE
            }
        }

        run {
            val type = lessons.type
            if (TextUtils.isEmpty(type)) {
                mTypeView.visibility = View.GONE
            } else {
                mTypeView.text = type
                mTypeView.visibility = View.VISIBLE
            }
        }
    }
}
