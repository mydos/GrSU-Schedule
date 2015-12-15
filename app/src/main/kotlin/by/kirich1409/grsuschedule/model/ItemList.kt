package by.kirich1409.grsuschedule.model

import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
abstract class ItemList<E>(val items: Array<E>) {

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null || javaClass != other.javaClass -> false
            else -> Arrays.equals(items, (other as ItemList<*>).items)
        }
    }

    override fun hashCode() = Arrays.hashCode(items)

    override fun toString() = Arrays.toString(items)
}
