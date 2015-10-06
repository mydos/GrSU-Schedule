package by.kirich1409.grsuschedule.schedule

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.SpiceRecyclerViewFragment
import by.kirich1409.grsuschedule.model.Schedule
import by.kirich1409.grsuschedule.network.request.ScheduleRequest
import by.kirich1409.grsuschedule.preference.ScheduleDisplayPrefActivity
import by.kirich1409.grsuschedule.student.ScheduleAdapterDataLoader
import by.kirich1409.grsuschedule.utils.Constants
import by.kirich1409.grsuschedule.utils.getErrorMessage
import by.kirich1409.grsuschedule.widget.DayHeaderViewHolder
import by.kirich1409.grsuschedule.widget.LessonViewHolder
import by.kirich1409.grsuschedule.widget.MarginItemDecoration
import by.kirich1409.grsuschedule.widget.ScheduleAdapter
import com.octo.android.robospice.exception.NoNetworkException
import com.octo.android.robospice.persistence.DurationInMillis
import com.octo.android.robospice.persistence.exception.SpiceException
import com.octo.android.robospice.request.listener.PendingRequestListener
import com.octo.android.robospice.request.listener.RequestListener
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import java.util.*

public abstract class ScheduleFragment : SpiceRecyclerViewFragment() {

    abstract val cacheKey: String
    open val loaderCallbacks = ScheduleDataCallbacks()
    open val scheduleRequestListener = ScheduleRequestListener()
    private val startDate: Date by lazy {
        val c = Calendar.getInstance(Constants.LOCALE_RU)
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        c.time
    }
    private val endDate: Date by lazy {
        val c = Calendar.getInstance(Constants.LOCALE_RU)
        c.add(Calendar.WEEK_OF_YEAR, 1)
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        c.time
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = this.recyclerView!!
        recyclerView.addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context)
                        .sizeResId(R.dimen.divider_size)
                        .colorResId(R.color.divider)
                        .build())

        recyclerView.addItemDecoration(
                MarginItemDecoration(
                        marginTop = resources.getDimensionPixelSize(R.dimen.margin_16),
                        filter = { viewHolder ->
                            viewHolder is DayHeaderViewHolder && viewHolder.adapterPosition > 0
                        })
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_schedule, menu)
        inflater.inflate(R.menu.data_refresh, menu)
    }

    override fun onStart() {
        super.onStart()
        if (spiceManager.pendingRequestCount > 0) {
            if (recyclerView!!.adapter == null) {
                spiceManager.addListenerIfPending(
                        Schedule::class.java, cacheKey, scheduleRequestListener);
            }
        } else if (isNoRecyclerData()) {
            loadSchedule(false)
        }
    }

    private fun loadSchedule(force: Boolean) {
        setProgressVisible(true)

        val scheduleRequest = newScheduleRequest(startDate, endDate)
        val duration = if (force) DurationInMillis.ALWAYS_EXPIRED else DurationInMillis.ONE_DAY
        spiceManager.getFromCacheAndLoadFromNetworkIfExpired(
                scheduleRequest, cacheKey, duration, scheduleRequestListener)
    }

    protected abstract fun newLessonsGroupItemViewHolder(parent: ViewGroup): LessonViewHolder

    protected abstract fun newLessonViewHolder(parent: ViewGroup): LessonViewHolder

    protected abstract fun newScheduleRequest(startDate: Date?, endDate: Date?): ScheduleRequest;

    private fun isDataInCache(): Boolean =
            spiceManager.isDataInCache(
                    Schedule::class.java, cacheKey, DurationInMillis.ALWAYS_RETURNED).get()

    private fun loadDataFromCache(listener: RequestListener<Schedule>) {
        spiceManager.getFromCache(
                Schedule::class.java, cacheKey, DurationInMillis.ALWAYS_RETURNED, listener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_schedule_display_pref -> {
                startActivity(ScheduleDisplayPrefActivity.makeIntent(context))
                true
            }

            R.id.menu_refresh -> {
                loadSchedule(true)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private inner class ScheduleRequestListener() : PendingRequestListener<Schedule> {

        override fun onRequestSuccess(schedule: Schedule) {
            val args = Bundle(1)
            args.putParcelable(ARG_SCHEDULE, schedule)
            loaderManager.restartLoader(LOADER_SCHEDULE_ADAPTER_DATA, args, loaderCallbacks)
        }

        override fun onRequestFailure(error: SpiceException) {
            val noListData = isNoRecyclerData()
            if (error is NoNetworkException && !noListData && isDataInCache()) {
                loadDataFromCache(this)
            } else {
                if (noListData) {
                    showError(error)
                } else {
                    Snackbar.make(view, error.getErrorMessage(context), Snackbar.LENGTH_LONG).show()
                }
            }
        }

        override fun onRequestNotFound() {
            if (isNoRecyclerData()) loadSchedule(false)
        }
    }

    private fun isNoRecyclerData(): Boolean {
        val recyclerAdapter = recyclerAdapter
        return recyclerAdapter == null || recyclerAdapter.itemCount == 0
    }

    private inner class ScheduleDataCallbacks : LoaderManager.LoaderCallbacks<ScheduleAdapter.Data> {
        override fun onLoaderReset(loader: Loader<ScheduleAdapter.Data>) {
        }

        override fun onLoadFinished(
                loader: Loader<ScheduleAdapter.Data>, data: ScheduleAdapter.Data) {
            recyclerAdapter = ScheduleAdapter(
                    context, data,
                    { parent -> newLessonViewHolder(parent) },
                    { parent -> newLessonsGroupItemViewHolder(parent) })
        }

        override fun onCreateLoader(id: Int, args: Bundle): Loader<ScheduleAdapter.Data>? {
            return when (id) {
                LOADER_SCHEDULE_ADAPTER_DATA ->
                    ScheduleAdapterDataLoader(context, args.getParcelable<Schedule?>(ARG_SCHEDULE))

                else -> throw RuntimeException("Unknown loader id=$id")
            }
        }
    }

    companion object {
        const val ARG_SCHEDULE = "schedule"
        const val LOADER_SCHEDULE_ADAPTER_DATA = 1
    }
}