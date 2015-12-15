package by.kirich1409.grsuschedule.lesson

import android.content.Context
import android.content.Intent
import android.os.Bundle
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseActivity
import by.kirich1409.grsuschedule.model.AlinementLessons

/**
 * Created by kirillrozov on 10/14/15.
 */
class AlinementLessonActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_toolbar)

        val lesson = intent.getParcelableExtra<AlinementLessons>(EXTRA_LESSON)

        val actionBar = supportActionBar
        actionBar.title = lesson.title
        actionBar.subtitle = lesson.interval.toString()

        setTaskDescriptionCompat(lesson.title)

        if (savedInstanceState == null) {
            val fragment = AlinementLessonFragment.newInstance(lesson)
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, fragment)
                    .commit()
        }
    }

    companion object {
        private val EXTRA_LESSON = if (BuildConfig.DEBUG) "lessons" else "a"

        public fun makeIntent(context: Context, lessons: AlinementLessons)
                = Intent(context, AlinementLessonActivity::class.java).putExtra(EXTRA_LESSON, lessons)
    }

    override val screenName = "Alinement Lesson"
}