package by.kirich1409.grsuschedule.lesson

import android.os.Bundle
import android.view.View
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.app.RecyclerViewFragment
import by.kirich1409.grsuschedule.model.AlinementLessons

/**
 * Created by kirillrozov on 11/11/15.
 */
class AlinementLessonFragment : RecyclerViewFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alinementLessons = arguments.getParcelable<AlinementLessons>(ARG_LESSON)
        recyclerAdapter = LessonAdapter(context, alinementLessons.lessons)
    }

    companion object {
        private val ARG_LESSON = if (BuildConfig.DEBUG) "lessons" else "a"

        public fun newInstance(lessons: AlinementLessons): AlinementLessonFragment {
            val fragment = AlinementLessonFragment()
            fragment.arguments = Bundle(1).apply { putParcelable(ARG_LESSON, lessons) }
            return fragment
        }
    }
}