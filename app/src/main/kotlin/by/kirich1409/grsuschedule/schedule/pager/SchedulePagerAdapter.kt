package by.kirich1409.grsuschedule.schedule.pager

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import by.kirich1409.grsuschedule.model.DaySchedule

/**
 * Created by kirillrozov on 10/9/15.
 */
class SchedulePagerAdapter(fm: FragmentManager, val items: Array<DaySchedule>) :
        FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = SchedulePagerItemFragment.newInstance(items[position])

    override fun getCount() = items.size

    override fun getPageTitle(position: Int) = items[position].date.format(true, false)
}