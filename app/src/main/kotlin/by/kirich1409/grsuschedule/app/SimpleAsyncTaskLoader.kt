package by.kirich1409.grsuschedule.app

import android.content.Context
import android.support.v4.content.AsyncTaskLoader

public abstract class SimpleAsyncTaskLoader<E>(context: Context) : AsyncTaskLoader<E>(context) {
    public var data: Any? = null
        private set

    private var mListenerUnregistered = true

    @Suppress("UNCHECKED_CAST")
    override fun onStartLoading() {
        super.onStartLoading()
        if (data != null) {
            deliverResult(data as E)
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
            return
        }

        this.data = data

        if (isStarted) {
            super.deliverResult(data)
        }
    }

    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onReset() {
        onStopLoading()

        if (data != null) {
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
}
