package by.kirich1409.grsuschedule.model

/**
 * Created by kirillrozov on 9/16/15.
 */
public class Course(val title: String, val number: Int) {

    override fun equals(other: Any?): Boolean {
        return when(other) {
            this -> true
            null, other.javaClass !== Course::class.javaClass -> false
            else -> number == (other as Course).number
        }
    }

    override fun hashCode(): Int {
        return number
    }

    override fun toString(): String {
        return title
    }
}