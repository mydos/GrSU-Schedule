package by.kirich1409.grsuschedule.schedule

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.SpiceRecyclerViewFragment
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPreference
import by.kirich1409.grsuschedule.utils.getErrorMessage
import by.kirich1409.grsuschedule.utils.isTablet
import by.kirich1409.grsuschedule.widget.adapter.DailyScheduleAdapter

public abstract class ScheduleListFragment : SpiceRecyclerViewFragment(), ScheduleFragmentDelegate.Callback {

    private val delegate = ScheduleFragmentDelegate(this, this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = context
        if (context.isTablet()) layoutManager = newLayoutManager()
    }

    private fun newLayoutManager(): RecyclerView.LayoutManager{
        if (ScheduleDisplayPreference(context).isHorizontalNavigation) {
            return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            val spanCount = context.resources.getInteger(R.integer.schedule_span_count);
            if (spanCount > 1) {
                return GridLayoutManager(
                        context, spanCount, LinearLayoutManager.VERTICAL, false)
            } else {
                return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
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

    override fun isDataEmpty(): Boolean {
        val recyclerAdapter = recyclerAdapter
        return recyclerAdapter == null || recyclerAdapter.itemCount == 0
    }

    override fun isNoData() = recyclerAdapter == null

    override fun onDataChanged(data: Array<DaySchedule>) {
        recyclerAdapter = DailyScheduleAdapter(context, data)
        recyclerView!!.setItemViewCacheSize(data.size())
    }

    override fun onError(error: Exception) {
        if (isNoData()) {
            showError(error)
        } else {
            Snackbar.make(view, error.getErrorMessage(context), Snackbar.LENGTH_LONG).show()
        }
    }
}