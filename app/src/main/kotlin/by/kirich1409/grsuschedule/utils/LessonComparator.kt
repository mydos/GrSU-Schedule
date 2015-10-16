package by.kirich1409.grsuschedule.utils

import by.kirich1409.grsuschedule.model.BaseItem
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.model.Subgroup
import java.util.*

/**
 * Created by kirillrozov on 9/23/15.
 */

class LessonComparator : Comparator<Lesson> {

    val subgroupComparator = BaseItem.TitleComparator<Subgroup>()

    override fun compare(lhs: Lesson, rhs: Lesson): Int {
        val intervalCompareTo = lhs.interval.compareTo(rhs.interval)
        if (intervalCompareTo != 0) {
            return intervalCompareTo
        } else if (lhs.subgroup == null && rhs.subgroup == null) {
            return 0
        } else if (lhs.subgroup == null) {
            return -1;
        } else if (rhs.subgroup == null) {
            return 1;
        } else {
            val lhsTitleParts = lhs.subgroup.title.split('.')
            val rhsTitleParts = rhs.subgroup.title.split('.')

            val itemsCount = lhsTitleParts.size()
            if (itemsCount > 0 && itemsCount == rhsTitleParts.size()) {
                for (j in 0 until itemsCount) {
                    if (lhsTitleParts[j].isDigitsOnly() && rhsTitleParts[j].isDigitsOnly()) {
                        val compare = lhsTitleParts[j].toInt().compareTo(rhsTitleParts[j].toInt())
                        if (compare != 0) {
                            return compare
                        }
                    }
                }
            }
            return subgroupComparator.compare(lhs.subgroup, rhs.subgroup)
        }
    }
}
