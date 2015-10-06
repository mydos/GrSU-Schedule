package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SectionIndexer
import by.kirich1409.grsuschedule.model.Teacher

/**
 * Created by kirillrozov on 9/14/15.
 */
public class TeacherAdapter(private val mContext: Context, private val data: TeacherAdapter.Data) :
        BaseAdapter(), SectionIndexer {

    override fun getCount(): Int = data.teachers.size()

    override fun getItem(position: Int): Teacher = data.teachers.get(position)

    override fun getItemId(position: Int): Long = data.teachers.get(position).id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = TeacherViewHolder.get(mContext, convertView, parent)
        binding.teacher = data.teachers.get(position)
        return binding.rootView
    }

    override fun getSections(): Array<out Any> = data.sections

    override fun getPositionForSection(sectionIndex: Int): Int = data.positionForSection[sectionIndex]

    override fun getSectionForPosition(position: Int): Int = data.sectionForPosition[position]

    public class Data(public val sections: Array<out Any>,
                      public val sectionForPosition: IntArray,
                      public val positionForSection: IntArray,
                      public val teachers: Array<Teacher>)
}
