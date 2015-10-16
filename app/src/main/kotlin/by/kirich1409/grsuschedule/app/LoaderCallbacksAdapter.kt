package by.kirich1409.grsuschedule.app

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader

/**
 * Created by kirillrozov on 10/7/15.
 */
abstract class LoaderCallbacksAdapter<D> : LoaderManager.LoaderCallbacks<D> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<D>? {
        throw UnsupportedOperationException("Unknown loader id=$id")
    }

    override fun onLoadFinished(loader: Loader<D>, data: D) {
    }

    override fun onLoaderReset(loader: Loader<D>) {
    }
}