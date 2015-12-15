package by.kirich1409.grsuschedule.schedule.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.lesson.LessonActivity
import by.kirich1409.grsuschedule.model.Lesson

/**
 * Created by kirillrozov on 10/14/15.
 */
class LessonBaseItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView), View.OnClickListener {

    private val mTeacherView: TextView
    private val mPlaceView: TextView
    private val mSubgroupView: TextView

    private var mLesson: Lesson? = null

    init {
        mTeacherView = rootView.findViewById(R.id.lesson_teacher) as TextView
        mPlaceView = rootView.findViewById(R.id.lesson_place) as TextView
        mSubgroupView = rootView.findViewById(R.id.lesson_subgroup) as TextView
        rootView.setOnClickListener(this)
    }

    fun bind(lesson: Lesson) {
        mLesson = lesson

        val teacher = lesson.teacher
        mTeacherView.text = teacher?.fullName

        mPlaceView.text = lesson.fullAddress
        val subgroup = lesson.subgroup
        if (subgroup == null) {
            mSubgroupView.visibility = View.GONE
        } else {
            mSubgroupView.text = subgroup.title
            mSubgroupView.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View) {
        if (mLesson != null) {
            val context = v.context
            context.startActivity(LessonActivity.makeIntent(context, mLesson!!))
        }
    }
}
