package by.kirich1409.grsuschedule.schedule.viewholder

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.lesson.LessonActivity
import by.kirich1409.grsuschedule.model.Lesson


/**
 * Created by kirillrozov on 10/14/15.
 */
open class LessonViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView), View.OnClickListener {

    private val mTypeView: TextView
    private val mTitleView: TextView
    private val mSubgroupView: TextView
    private val mTeacherView: TextView
    private val mPlaceView: TextView

    private var mLesson: Lesson? = null

    init {
        mTypeView = rootView.findViewById(R.id.lesson_type) as TextView
        mTitleView = rootView.findViewById(R.id.lesson_title) as TextView
        mSubgroupView = rootView.findViewById(R.id.lesson_subgroup) as TextView
        mTeacherView = rootView.findViewById(R.id.lesson_teacher) as TextView
        mPlaceView = rootView.findViewById(R.id.lesson_place) as TextView
        rootView.setOnClickListener(this)
    }

    @CallSuper
    open fun bind(lesson: Lesson) {
        mLesson = lesson

        mTypeView.text = lesson.type
        mTitleView.text = lesson.title

        run {
            val subgroup = lesson.subgroup
            val text = subgroup?.title
            if (TextUtils.isEmpty(text)) {
                mSubgroupView.visibility = View.GONE
            } else {
                mSubgroupView.text = text
                mSubgroupView.visibility = View.VISIBLE
            }
        }

        run {
            val teacher = lesson.teacher
            val text = teacher?.fullName
            if (TextUtils.isEmpty(text)) {
                mTeacherView.visibility = View.GONE
            } else {
                mTeacherView.text = text
                mTeacherView.visibility = View.VISIBLE
            }
        }

        run {
            val address = lesson.fullAddress
            if (TextUtils.isEmpty(address)) {
                mPlaceView.visibility = View.GONE
            } else {
                mPlaceView.text = address
                mPlaceView.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(v: View) {
        if (mLesson != null) {
            v.context.startActivity(LessonActivity.makeIntent(v.context, mLesson!!))
        }
    }
}
