package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.ListItemViewHolder
import by.kirich1409.grsuschedule.app.getListItemViewHolder
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.utils.bindView
import by.kirich1409.grsuschedule.utils.inflate

/**
 * Created by kirillrozov on 9/15/15.
 */
class TeacherViewHolder(context: Context, rootView: View) : ListItemViewHolder(context, rootView) {

    val teacherNameView: TextView by bindView(R.id.teacher_name)
    val teacherPostView: TextView by bindView(R.id.teacher_post)
    var teacher: Teacher? = null
        set(teacher) {
            field = teacher
            teacherNameView.text = teacher?.fullname
            teacherPostView.text = teacher?.post
        }

    companion object {
        public fun get(context: Context, convertView: View?, parent: ViewGroup): TeacherViewHolder {
            if (convertView == null) {
                val view = context.inflate(R.layout.item_teacher, parent)
                return TeacherViewHolder(context, view)
            } else {
                return convertView.getListItemViewHolder() as TeacherViewHolder
            }
        }
    }
}
