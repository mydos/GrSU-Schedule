package by.kirich1409.grsuschedule.schedule

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.Loader
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.LoaderCallbacksAdapter
import by.kirich1409.grsuschedule.model.DaySchedule
import by.kirich1409.grsuschedule.model.Schedule
import by.kirich1409.grsuschedule.network.ScheduleSpiceService
import by.kirich1409.grsuschedule.network.request.ScheduleRequest
import by.kirich1409.grsuschedule.utils.LOCALE_RU
import by.kirich1409.grsuschedule.utils.getErrorMessage
import com.octo.android.robospice.SpiceManager
import com.octo.android.robospice.exception.NoNetworkException
import com.octo.android.robospice.persistence.DurationInMillis
import com.octo.android.robospice.persistence.exception.SpiceException
import com.octo.android.robospice.request.listener.PendingRequestListener
import com.octo.android.robospice.request.listener.RequestListener
import java.text.SimpleDateFormat
import java.util.*

public class ScheduleFragmentDelegate(val fragment: Fragment,
                                      val callback: ScheduleFragmentDelegate.Callback)
: ScheduleStartDateDialogFragment.Listener {

    val spiceManager = SpiceManager(ScheduleSpiceService::class.java)
    private val loaderCallbacks = ScheduleDataCallbacks()
    private val scheduleRequestListener = ScheduleRequestListener()
    private var endDate: Date? = null
    private var startDate: Date? = null
    private var pickStartDateFragment: ScheduleStartDateDialogFragment? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val cacheKey: String
        get() = "${callback.getCacheKey()}" +
                "&startDate=${dateFormat.format(startDate)}" +
                "&endDate=${dateFormat.format(endDate)}"
    private var dataRefreshing = true

    init {
        fragment.setHasOptionsMenu(true)
    }

    public fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            var c = Calendar.getInstance(LOCALE_RU)
            val curDay = c.get(Calendar.DAY_OF_WEEK)
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            startDate = c.time

            c.set(Calendar.DAY_OF_WEEK, curDay)
            c.add(Calendar.WEEK_OF_YEAR, 1)
            endDate = c.time
        } else {
            startDate = savedInstanceState.getSerializable(STATE_START_DATE) as Date
            endDate = savedInstanceState.getSerializable(STATE_END_DATE) as Date
            dataRefreshing = savedInstanceState.getBoolean(STATE_DATA_REFERSHING)
        }

        pickStartDateFragment = callback.getFragmentManager()
                .findFragmentByTag(FRAGMENT_START_DATE_PICKER) as ScheduleStartDateDialogFragment?
        if (pickStartDateFragment != null) {
            pickStartDateFragment!!.setListener(this)
        }
    }

    private fun setDateInterval(startDate: Date) {
        var c = Calendar.getInstance(LOCALE_RU)
        c.time = startDate
        this.startDate = startDate

        c.add(Calendar.WEEK_OF_YEAR, 1)
        this.endDate = c.time
    }

    public fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(STATE_START_DATE, startDate)
        outState.putSerializable(STATE_END_DATE, endDate)
        outState.putBoolean(STATE_DATA_REFERSHING, dataRefreshing)
    }

    public fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!dataRefreshing) {
            inflater.inflate(R.menu.data_refresh, menu)
            inflater.inflate(R.menu.date_pick, menu)
        }
    }

    public fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                loadSchedule(true)
                true
            }

            R.id.menu_pick_date -> {
                if (pickStartDateFragment == null) {
                    pickStartDateFragment = ScheduleStartDateDialogFragment.newInstance(startDate!!)
                            .apply {
                                setListener(this@ScheduleFragmentDelegate)
                                show(callback.getFragmentManager(), FRAGMENT_START_DATE_PICKER)
                            }
                }
                true
            }

            else -> false
        }
    }

    fun onStart() {
        spiceManager.start(fragment.context)

        if (spiceManager.pendingRequestCount > 0) {
            if (callback.isNoData()) {
                callback.setProgressVisible(true, false)
                spiceManager.addListenerIfPending(
                        Schedule::class.java, cacheKey, scheduleRequestListener);
            }
        } else if (callback.isDataEmpty()) {
            loadSchedule(false)
        }
    }

    fun onStop() {
        spiceManager.shouldStop()
        if (pickStartDateFragment != null) {
            pickStartDateFragment!!.setListener(null)
        }
    }

    fun loadSchedule(force: Boolean) {
        dataRefreshing = true
        callback.setProgressVisible(true, true)
        val duration = if (force) DurationInMillis.ALWAYS_EXPIRED else DurationInMillis.ONE_DAY
        val scheduleRequest = callback.newScheduleRequest(startDate, endDate)
        spiceManager.getFromCacheAndLoadFromNetworkIfExpired(
                scheduleRequest, cacheKey, duration, scheduleRequestListener)
    }

    private fun isDataInCache(): Boolean =
            spiceManager.isDataInCache(
                    Schedule::class.java, cacheKey, DurationInMillis.ALWAYS_RETURNED).get()

    private fun loadDataFromCache(listener: RequestListener<Schedule>) {
        spiceManager.getFromCache(
                Schedule::class.java, cacheKey, DurationInMillis.ALWAYS_RETURNED, listener)
    }

    private inner class ScheduleRequestListener() : PendingRequestListener<Schedule> {

        override fun onRequestSuccess(schedule: Schedule?) {
            if (schedule != null) {
                val args = Bundle(1)
                args.putParcelable(ARG_SCHEDULE, schedule)
                fragment.loaderManager.restartLoader(
                        LOADER_SCHEDULE_ADAPTER_DATA, args, loaderCallbacks)
            } else {
                onRequestFailure(
                        SpiceException("Schedule request finished success, but data in null."))
            }
        }

        override fun onRequestFailure(error: SpiceException) {
            dataRefreshing = false
            callback.setProgressVisible(false, true)

            val noListData = callback.isDataEmpty()
            if (error is NoNetworkException && !noListData && isDataInCache()) {
                loadDataFromCache(this)
            } else if (noListData) {
                callback.onError(error)
            } else {
                Snackbar.make(fragment.view,
                        error.getErrorMessage(fragment.context),
                        Snackbar.LENGTH_LONG).show()
            }

            callback.invalidateOptionsMenu()
        }

        override fun onRequestNotFound() {
            if (callback.isDataEmpty()) loadSchedule(false)
        }
    }

    private inner class ScheduleDataCallbacks : LoaderCallbacksAdapter<Array<DaySchedule>>() {

        override fun onLoadFinished(
                loader: Loader<Array<DaySchedule>>, data: Array<DaySchedule>) {
            callback.onDataChanged(data)
            callback.setProgressVisible(false, true)
            dataRefreshing = false
            callback.invalidateOptionsMenu()
        }

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Array<DaySchedule>>? {
            return when (id) {
                LOADER_SCHEDULE_ADAPTER_DATA -> ScheduleAdapterDataLoader(
                        fragment.context, args!!.getParcelable<Schedule?>(ARG_SCHEDULE))

                else -> super.onCreateLoader(id, args)
            }
        }
    }

    override fun onDateSet(date: Date) {
        setDateInterval(date);
        if (pickStartDateFragment != null) {
            pickStartDateFragment!!.apply {
                setListener(null)
                dismissAllowingStateLoss()
            }
            pickStartDateFragment = null
        }
        loadSchedule(false)
    }

    interface Callback {

        fun isDataEmpty(): Boolean

        fun isNoData(): Boolean

        fun onDataChanged(data: Array<DaySchedule>)

        fun onError(error: Exception)

        fun getCacheKey(): Any

        fun newScheduleRequest(startDate: Date?, endDate: Date?): ScheduleRequest

        fun setProgressVisible(visible: Boolean, animate: Boolean)

        fun getFragmentManager(): FragmentManager

        fun invalidateOptionsMenu()
    }

    companion object {
        val ARG_SCHEDULE = if (BuildConfig.DEBUG) "schedule" else "a"
        val LOADER_SCHEDULE_ADAPTER_DATA = 1
        val STATE_START_DATE = if (BuildConfig.DEBUG) "startDate" else "a"
        val STATE_END_DATE = if (BuildConfig.DEBUG) "endDate" else "b"
        val STATE_DATA_REFERSHING = if (BuildConfig.DEBUG) "dataRefresing" else "c"
        val FRAGMENT_START_DATE_PICKER = if (BuildConfig.DEBUG) "startDate" else "a"
    }
}