package by.kirich1409.grsuschedule.model

/**
 * Created by kirillrozov on 9/13/15.
 */
public class LessonGroup(val interval: TimeInterval,
                        val title: String,
                        val address: String?,
                        val room: String?,
                        val lessons: Array<Lesson>) {

    override fun toString(): String {
        return "'$title', $interval"
    }
}
