package by.kirich1409.grsuschedule.lesson

import android.content.Context
import android.content.Intent
import android.os.Bundle
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseActivity
import by.kirich1409.grsuschedule.model.Lesson

/**
 * Created by kirillrozov on 12/12/15.
 */
class LessonActivity : BaseActivity() {

    override val screenName = "Lesson"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_toolbar);

        val lesson = intent.getParcelableExtra<Lesson>(EXTRA_LESSON)
        val actionBar = supportActionBar!!
        actionBar.title = lesson.title
        actionBar.subtitle = lesson.type

        setTaskDescriptionCompat(lesson.title)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, LessonFragment.newInstance(lesson))
                    .commit()
        }
    }

    companion object {
        private val EXTRA_LESSON = if (BuildConfig.DEBUG) "lesson" else "a"

        public fun makeIntent(context: Context, lesson: Lesson): Intent =
                Intent(context, LessonActivity::class.java).putExtra(EXTRA_LESSON, lesson)
    }
}