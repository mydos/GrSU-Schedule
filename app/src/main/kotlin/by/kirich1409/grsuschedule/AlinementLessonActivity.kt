package by.kirich1409.grsuschedule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import by.kirich1409.grsuschedule.app.BaseActivity
import by.kirich1409.grsuschedule.schedule.AlinementLessons
import by.kirich1409.grsuschedule.widget.adapter.BaseLessonAdapter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import junit.framework.Assert

/**
 * Created by kirillrozov on 10/14/15.
 */
class AlinementLessonActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_alinement_lesson_list)

        val lesson = intent.getParcelableExtra<AlinementLessons>(EXTRA_LESSON)

        val actionBar = findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        actionBar.title = lesson.title

        val timeView = findViewById(R.id.lesson_time) as TextView
        timeView.text = lesson.interval.toString()

        val lessons = lesson.lessons
        Assert.assertFalse(lessons.isEmpty())

        val recyclerView = findViewById(android.R.id.list) as RecyclerView
        val dividerItemDecoration = HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.divider)
                .sizeResId(R.dimen.divider_size)
                .build()
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = BaseLessonAdapter(this, lessons)
    }

    companion object {
        private val EXTRA_LESSON = "lesson"

        public fun makeIntent(context: Context, lessons: AlinementLessons): Intent {
            val intent = Intent(context, AlinementLessonActivity::class.java)
            intent.putExtra(EXTRA_LESSON, lessons)
            return intent
        }
    }
}