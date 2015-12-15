package by.kirich1409.grsuschedule.app

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.ListAdapter
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.model.Teachers
import by.kirich1409.grsuschedule.utils.getErrorMessage
import com.octo.android.robospice.exception.NoNetworkException
import com.octo.android.robospice.persistence.DurationInMillis
import com.octo.android.robospice.persistence.exception.SpiceException
import com.octo.android.robospice.request.SpiceRequest
import com.octo.android.robospice.request.listener.PendingRequestListener

/**
 * Created by kirillrozov on 10/3/15.
 */
abstract class SimpleSpiceListFragment<E> : SpiceListFragment() {

    protected abstract val cacheKey: String
    protected abstract val dataClass: Class<E>
    protected open val dataCacheDuration = DurationInMillis.ONE_DAY
    private val requestListener = DataRequestListener()
    protected var lastVisiblePosition = -1

    override var listAdapter: ListAdapter?
        get() = super.listAdapter
        set(value) {
            super.listAdapter = value
            if (lastVisiblePosition > 0) {
                listView!!.setSelection(lastVisiblePosition)
                lastVisiblePosition = -1
            }
        }

    private fun hasCachedData() = spiceManager.isDataInCache(
            Teachers::class.java, cacheKey, DurationInMillis.ALWAYS_RETURNED).get()

    override fun onStart() {
        super.onStart()
        if (spiceManager.pendingRequestCount > 0) {
            spiceManager.addListenerIfPending(dataClass, cacheKey, requestListener);
        } else if (listView!!.adapter == null) {
            loadData(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            lastVisiblePosition = savedInstanceState.getInt(STATE_LIST_POSITION)
        }
    }

    protected open fun onRequestFailure(error: SpiceException) {
    }

    protected open fun onRequestSuccess(data: E) {
    }

    override fun loadData(force: Boolean) {
        setProgressVisible(true)
        val duration: Long
        if (force) {
            lastVisiblePosition = -1
            duration = DurationInMillis.ALWAYS_EXPIRED
        } else {
            duration = dataCacheDuration
        }
        spiceManager.getFromCacheAndLoadFromNetworkIfExpired(
                newSpiceRequest(), cacheKey, duration, requestListener)
    }

    protected abstract fun newSpiceRequest(): SpiceRequest<E>

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val listView = listView
        if (listView != null) {
            outState.putInt(STATE_LIST_POSITION, listView.firstVisiblePosition)
        }
    }

    private inner class DataRequestListener : PendingRequestListener<E> {

        override fun onRequestFailure(error: SpiceException) {
            showErrorMessage(error)
            if (error is NoNetworkException && !hasListData() && hasCachedData()) {
                spiceManager.getFromCache(
                        dataClass, cacheKey, DurationInMillis.ALWAYS_RETURNED, this)
            }
            this@SimpleSpiceListFragment.onRequestFailure(error)
        }

        private fun showErrorMessage(error: SpiceException) {
            if (hasListData()) {
                setProgressVisible(false)
                Snackbar.make(view, error.getErrorMessage(context), Snackbar.LENGTH_LONG).show()
            } else {
                showError(error)
            }
        }

        override fun onRequestSuccess(data: E) =
                this@SimpleSpiceListFragment.onRequestSuccess(data)

        override fun onRequestNotFound() {
            if (listAdapter == null) loadData(false)
        }
    }

    companion object {
        val STATE_LIST_POSITION = if (BuildConfig.DEBUG) "listPosition" else "a"
    }
}