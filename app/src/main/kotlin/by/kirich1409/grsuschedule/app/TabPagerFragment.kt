package by.kirich1409.grsuschedule.app

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.utils.fadeIn
import by.kirich1409.grsuschedule.utils.fadeOut

/**
 * Created by kirillrozov on 10/12/15.
 */
open class TabPagerFragment : Fragment() {

    private var tabsNeedSetup = true

    protected var tabLayout: TabLayout? = null
        private set
    protected var viewPager: ViewPager? = null
        private set
    protected var progressView: View? = null
        private set
    protected var emptyView: TextView? = null
        private set

    var progressVisible: Boolean = true
        set(visible) {
            field = visible
            if (tabsNeedSetup) {
                tabLayout!!.setupWithViewPager(viewPager)
            }
            setProgressVisible(visible)
        }

    var pagerAdapter: PagerAdapter?
        get() = viewPager!!.adapter
        set(adapter) {
            viewPager!!.adapter = adapter
            tabLayout!!.setupWithViewPager(viewPager)
            progressVisible = false
        }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_tab_pager, container, false)
        viewPager = view.findViewById(R.id.pager) as ViewPager
        tabLayout = view.findViewById(android.R.id.tabs) as TabLayout
        progressView = view.findViewById(android.R.id.progress)
        emptyView = view.findViewById(android.R.id.empty) as TextView?
        setProgressVisible(true, false)
        return view
    }

    fun setEmptyText(text: CharSequence?) {
        emptyView!!.text = text
    }

    override fun onDestroyView() {
        tabLayout = null
        viewPager = null
        progressView = null
        emptyView = null
        super.onDestroyView()
    }

    fun setProgressVisible(visible: Boolean, animate: Boolean = true) {
        val viewPager = viewPager!!
        val tabLayout = tabLayout!!

        if (visible) {
            tabLayout.fadeOut(false)
            viewPager.fadeOut(false)
            emptyView?.fadeOut(false)
            progressView?.fadeIn(animate)
        } else {
            progressView?.fadeOut(animate)

            val adapter = viewPager.adapter
            if (adapter != null && adapter.count > 0) {
                emptyView?.fadeOut(false)
                tabLayout.fadeIn(false)
                viewPager.fadeIn(animate)
            } else {
                tabLayout.fadeOut(false)
                viewPager.fadeOut(false)
                emptyView?.fadeIn(animate)
            }
        }
    }
}