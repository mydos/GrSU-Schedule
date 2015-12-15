package by.kirich1409.grsuschedule.teacher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SectionIndexer
import android.widget.TextView

import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Teacher

/**
 * Created by kirillrozov on 10/8/15.
 */
class TeacherAdapter(private val mContext: Context, private val mData: TeacherAdapter.Data) : BaseAdapter(), SectionIndexer {

    override fun getCount(): Int {
        return mData.teachers.size
    }

    override fun getItem(position: Int): Teacher {
        return mData.teachers[position]
    }

    override fun getItemId(position: Int): Long {
        return mData.teachers[position].id.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: TeacherViewHolder
        if (convertView == null) {
            binding = TeacherViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_teacher, parent, false))
            binding.root.setTag(R.id.view_holder, binding)
        } else {
            binding = convertView.getTag(R.id.view_holder) as TeacherViewHolder
        }
        binding.bind(mData.teachers[position])
        return binding.root
    }

    override fun getSections(): Array<Any> {
        return mData.sections
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mData.positionForSection[sectionIndex]
    }

    override fun getSectionForPosition(position: Int): Int {
        return mData.sectionForPosition[position]
    }

    class Data(internal var sections: Array<Any>,
               internal var sectionForPosition: IntArray,
               internal var positionForSection: IntArray,
               internal var teachers: Array<Teacher>)

    private class TeacherViewHolder(val root: View) {
        private val mNameView: TextView
        private val mPostView: TextView

        init {
            mNameView = root.findViewById(R.id.teacher_name) as TextView
            mPostView = root.findViewById(R.id.teacher_post) as TextView
        }

        fun bind(teacher: Teacher) {
            mNameView.text = teacher.fullName
            mPostView.text = teacher.post
        }
    }
}
