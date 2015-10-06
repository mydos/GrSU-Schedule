package by.kirich1409.grsuschedule.app

import android.content.Context
import android.support.v4.content.AsyncTaskLoader

public abstract class SimpleAsyncTaskLoader<E>(context: Context) : AsyncTaskLoader<E>(context) {
    public var data: E? = null
        private set

    private var mListenerUnregistered = true

    override fun onStartLoading() {
        super.onStartLoading()
        if (this.data != null) {
            deliverResult(this.data as E)
        } else {
            if (mListenerUnregistered) {
                registerListeners()
                mListenerUnregistered = false
            }

            if (takeContentChanged() || data == null) {
                forceLoad()
            }
        }
    }

    override fun onContentChanged() {
        data = null
        super.onContentChanged()
    }

    override fun deliverResult(data: E) {
        if (isReset) {
            releaseResources(data)
            return
        }

        val oldData = this.data
        this.data = data

        if (isStarted) {
            super.deliverResult(data)
        }

        if (oldData != null && oldData !== data) {
            releaseResources(oldData)
        }
    }

    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onReset() {
        onStopLoading()

        if (data != null) {
            releaseResources(data as E)
            data = null
        }

        if (!mListenerUnregistered) {
            unregisterListeners()
            mListenerUnregistered = true
        }
    }

    protected open fun registerListeners() {
    }

    protected open fun unregisterListeners() {
    }

    override fun onCanceled(data: E?) {
        super.onCanceled(data)
        if (data != null) {
            releaseResources(data)
        }
    }

    @SuppressWarnings("Unused")
    protected fun releaseResources(data: E) {
    }
}
