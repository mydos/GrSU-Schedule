package by.kirich1409.grsuschedule.schedule

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.Loader
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.LoaderCallbacksAdapter
import by.kirich1409.grsuschedule.model.Schedule
import by.kirich1409.grsuschedule.network.ScheduleSpiceService
import by.kirich1409.grsuschedule.network.request.ScheduleRequest
import by.kirich1409.grsuschedule.student.ScheduleAdapterDataLoader
import by.kirich1409.grsuschedule.utils.Constants
import by.kirich1409.grsuschedule.utils.getErrorMessage
import com.octo.android.robospice.SpiceManager
import com.octo.android.robospice.exception.NoNetworkException
import com.octo.android.robospice.persistence.DurationInMillis
import com.octo.android.robospice.persistence.exception.SpiceException
import com.octo.android.robospice.request.listener.PendingRequestListener
import com.octo.android.robospice.request.listener.RequestListener
import java.util.*

public class ScheduleFragmentDelegate(val fragment: Fragment,
                                      val callback: ScheduleFragmentDelegate.Callback) {

    val spiceManager: SpiceManager = SpiceManager(ScheduleSpiceService::class.java)
    private val cacheKey by lazy { callback.getCacheKey() }
    val loaderCallbacks = ScheduleDataCallbacks()
    val scheduleRequestListener = ScheduleRequestListener()
    private var dates: Pair<Date, Date> = newDates()

    init {
        fragment.setHasOptionsMenu(true)
    }

    public fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.data_refresh, menu)
    }

    public fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                loadSchedule(true)
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
    }

    fun loadSchedule(force: Boolean) {
        callback.setProgressVisible(true, true)
        val duration: Long
        if (force) {
            dates = newDates()
            duration = DurationInMillis.ALWAYS_EXPIRED
        } else {
            duration = DurationInMillis.ONE_DAY
        }
        val scheduleRequest = callback.newScheduleRequest(dates.first, dates.second)
        spiceManager.execute(scheduleRequest, cacheKey, duration, scheduleRequestListener)
    }

    private fun isDataInCache(): Boolean =
            spiceManager.isDataInCache(
                    Schedule::class.java, cacheKey, DurationInMillis.ALWAYS_RETURNED).get()

    private fun loadDataFromCache(listener: RequestListener<Schedule>) {
        spiceManager.getFromCache(
                Schedule::class.java, cacheKey, DurationInMillis.ALWAYS_RETURNED, listener)
    }

    private inner class ScheduleRequestListener() : PendingRequestListener<Schedule> {

        override fun onRequestSuccess(schedule: Schedule) {
            val args = Bundle(1)
            args.putParcelable(ARG_SCHEDULE, schedule)
            fragment.loaderManager.restartLoader(LOADER_SCHEDULE_ADAPTER_DATA, args, loaderCallbacks)
        }

        override fun onRequestFailure(error: SpiceException) {
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
        }

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Array<DaySchedule>>? {
            return when (id) {
                LOADER_SCHEDULE_ADAPTER_DATA -> ScheduleAdapterDataLoader(
                        fragment.context, args!!.getParcelable<Schedule?>(ARG_SCHEDULE))

                else -> super.onCreateLoader(id, args)
            }
        }
    }

    interface Callback {
        fun isDataEmpty(): Boolean

        fun isNoData(): Boolean

        fun onDataChanged(data: Array<DaySchedule>)

        fun onError(error: Exception)

        fun getCacheKey(): Any

        fun newScheduleRequest(startDate: Date?, endDate: Date?): ScheduleRequest

        fun setProgressVisible(visible: Boolean, animate: Boolean)
    }

    companion object {
        val ARG_SCHEDULE = if (BuildConfig.DEBUG) "schedule" else "a"
        val LOADER_SCHEDULE_ADAPTER_DATA = 1

        private fun newDates(): Pair<Date, Date> {
            var c = Calendar.getInstance(Constants.LOCALE_RU)
            val curDay = c.get(Calendar.DAY_OF_WEEK)
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val first = c.time

            c.set(Calendar.DAY_OF_WEEK, curDay)
            c.add(Calendar.WEEK_OF_YEAR, 1)
            return Pair(first, c.time)
        }
    }
}