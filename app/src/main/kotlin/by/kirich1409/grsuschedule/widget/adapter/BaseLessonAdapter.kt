package by.kirich1409.grsuschedule.widget.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.utils.inflate
import by.kirich1409.grsuschedule.widget.LessonBaseItemViewHolder

/**
 * Created by kirillrozov on 10/14/15.
 */
class BaseLessonAdapter(private val context: Context, private val lessons: Array<Lesson>) :
        RecyclerView.Adapter<LessonBaseItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            LessonBaseItemViewHolder(context.inflate(R.layout.item_lesson_base, parent))

    override fun getItemCount() = lessons.size()

    override fun onBindViewHolder(holder: LessonBaseItemViewHolder, position: Int)
            = holder.bind(lessons[position])
}