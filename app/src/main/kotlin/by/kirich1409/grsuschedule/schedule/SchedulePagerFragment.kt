package by.kirich1409.grsuschedule.schedule

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.TabPagerFragment

/**
 * Created by kirillrozov on 10/9/15.
 */
abstract class SchedulePagerFragment : TabPagerFragment(), ScheduleFragmentDelegate.Callback {

    private val delegate by lazy { ScheduleFragmentDelegate(this, this) }
    private var currentPosition: Int = -1 // Temp value for return scroll to latest page before config change

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(STATE_POSITION)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager!!.pageMargin = resources.getDimensionPixelSize(R.dimen.divider_size)
        viewPager!!.setPageMarginDrawable(R.drawable.vertical_line)
    }

    override fun isDataEmpty(): Boolean {
        val adapter = viewPager?.adapter
        return adapter == null || adapter.count == 0
    }

    override fun onStart() {
        super.onStart()
        delegate.onStart()
    }

    override fun onStop() {
        delegate.onStop()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        delegate.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return delegate.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    override fun isNoData() = viewPager?.adapter == null

    override fun onDataChanged(data: Array<DaySchedule>) {
        val count = pagerAdapter?.count ?: -1
        val fragmentManager = childFragmentManager
        if (count > 0) {
            val transaction = fragmentManager.beginTransaction()
            var fragmentDeleted: Boolean = false
            for (i in 0 until count) {
                val name = "android:switcher:" + R.id.pager + ':' + i
                val fragment = fragmentManager.findFragmentByTag(name)
                if (fragmentDeleted && fragment == null) {
                    break
                } else if (fragment != null) {
                    transaction.remove(fragment)
                    fragmentDeleted = true
                }
            }
            transaction.commitAllowingStateLoss()
        }

        pagerAdapter = SchedulePagerAdapter(fragmentManager, data)
        if (currentPosition >= 0) {
            viewPager!!.currentItem = currentPosition
            currentPosition = -1
        }
    }

    override fun onError(error: Exception) {
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val viewPager = viewPager!!
        if (viewPager.adapter != null) {
            outState.putInt(STATE_POSITION, viewPager.currentItem)
        }
    }

    companion object {
        private val STATE_POSITION = if (BuildConfig.DEBUG) "position" else "a"
    }
}