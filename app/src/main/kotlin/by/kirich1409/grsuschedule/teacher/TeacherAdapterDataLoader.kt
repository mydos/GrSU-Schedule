package by.kirich1409.grsuschedule.teacher

import android.content.Context
import by.kirich1409.grsuschedule.app.SimpleAsyncTaskLoader
import by.kirich1409.grsuschedule.model.Teachers
import by.kirich1409.grsuschedule.utils.startWithIgnoreCase
import java.util.*

/**
 * Created by kirillrozov on 9/23/15.
 */

class TeacherAdapterDataLoader(context: Context, val teachers: Teachers, val query: String? = null) :
        SimpleAsyncTaskLoader<TeacherAdapter.Data>(context) {

    override fun loadInBackground(): TeacherAdapter.Data {
        val items = teachers.items.filter({ it.fullName.startWithIgnoreCase(query) })

        val sectionsCapacity = 30 * 30

        val sectionForPosition: ArrayList<Int> = ArrayList()
        val positionForSection: ArrayList<Int> = ArrayList(sectionsCapacity)
        val sections = ArrayList<String>(sectionsCapacity)

        var section: Int = 0
        items.forEachIndexed { position, teacher ->
            val nameFirstChar = teacher.fullName.substring(0, 2)
            if (sections.isEmpty() || sections.last() !== nameFirstChar) {
                sections.add(nameFirstChar)
                section++
                positionForSection.add(position)
            }
            sectionForPosition.add(section)
        }
        return TeacherAdapter.Data(
                sections.toTypedArray(),
                sectionForPosition.toIntArray(),
                positionForSection.toIntArray(),
                items.toTypedArray())
    }
}
