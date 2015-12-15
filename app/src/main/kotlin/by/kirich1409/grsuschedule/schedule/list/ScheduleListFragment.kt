package by.kirich1409.grsuschedule.schedule.list

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.SpiceRecyclerViewFragment
import by.kirich1409.grsuschedule.model.DaySchedule
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPreference
import by.kirich1409.grsuschedule.schedule.ScheduleFragmentDelegate
import by.kirich1409.grsuschedule.utils.getErrorMessage
import by.kirich1409.grsuschedule.utils.isPhone
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

public abstract class ScheduleListFragment : SpiceRecyclerViewFragment(),
        ScheduleFragmentDelegate.Callback {

    private val delegate = ScheduleFragmentDelegate(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState?.getBundle(STATE_DELEGATE))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val delegateState = Bundle(2)
        delegate.onSaveInstanceState(delegateState)
        outState.putBundle(STATE_DELEGATE, delegateState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context
        if (context.isPhone()) {
            val dividerItemDecoration = HorizontalDividerItemDecoration.Builder(context)
                    .colorResId(R.color.divider)
                    .sizeResId(R.dimen.divider_size)
                    .marginResId(R.dimen.divider_horizontal_margin)
                    .build()
            recyclerView!!.addItemDecoration(dividerItemDecoration)
        } else {
            layoutManager = newLayoutManager()
        }
    }

    private fun newLayoutManager(): RecyclerView.LayoutManager {
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
        recyclerAdapter = if (context.isPhone()) {
            PhoneDailyScheduleAdapter(context, data)
        } else {
            DailyScheduleAdapter(context, data)
        }
        recyclerView!!.setItemViewCacheSize(data.size)
    }

    override fun onError(error: Exception) {
        if (isNoData()) {
            showError(error)
        } else {
            Snackbar.make(view, error.getErrorMessage(context), Snackbar.LENGTH_LONG).show()
        }
    }

    override fun invalidateOptionsMenu() {
        activity.supportInvalidateOptionsMenu()
    }

    companion object {
        val STATE_DELEGATE = if (BuildConfig.DEBUG) "delegate" else "a"
    }
}