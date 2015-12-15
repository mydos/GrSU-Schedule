package by.kirich1409.grsuschedule.app

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.utils.fadeIn
import by.kirich1409.grsuschedule.utils.fadeOut
import by.kirich1409.grsuschedule.utils.getErrorMessage
import by.kirich1409.grsuschedule.utils.setDrawableTop

/**
 * Created by kirillrozov on 9/25/15.
 */
public class RecyclerViewFragmentDelegate(private val context: Context) {

    var recyclerView: RecyclerView? = null
        private set

    private var emptyView: View? = null
    private var progressView: View? = null

    var recyclerAdapter: RecyclerView.Adapter<*>?
        get() {
            return recyclerView!!.adapter
        }
        set(adapter) {
            recyclerView!!.adapter = adapter
            setProgressVisible(false)

            val emptyView = emptyView
            if (emptyView is TextView) {
                emptyView.setDrawableTop(null)
            }
        }

    var layoutManager: RecyclerView.LayoutManager
        get() = recyclerView!!.layoutManager
        set(layoutManager) {
            recyclerView!!.layoutManager = layoutManager
        }

    var progressVisible: Boolean = true
        set(visible) {
            field = visible
            setProgressVisible(visible)
        }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    fun onViewCreated(view: View) {
        recyclerView = view.findViewById(android.R.id.list) as RecyclerView
        emptyView = view.findViewById(android.R.id.empty)
        progressView = view.findViewById(android.R.id.progress)

        val recyclerView = recyclerView!!
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        setProgressVisible(true, false)
    }

    fun setProgressVisible(visible: Boolean, animate: Boolean = true) {
        val recyclerView = recyclerView!!

        if (visible) {
            recyclerView.fadeOut(false)
            emptyView?.fadeOut(false)
            progressView?.fadeIn(animate)
        } else {
            progressView?.fadeOut(animate)

            val adapter = recyclerView.adapter
            if (adapter != null && adapter.itemCount > 0) {
                emptyView?.fadeOut(false)
                recyclerView.fadeIn(animate)
            } else {
                recyclerView.fadeOut(false)
                emptyView?.fadeIn(animate)
            }
        }
    }

    fun showError(error: Exception) {
        val emptyView = emptyView
        if (emptyView is TextView) {
            val message = error.getErrorMessage(context)
            emptyView.text = message
            emptyView.setDrawableTop(R.drawable.ic_sad_smile_180dp)
        }

        setProgressVisible(false)
    }

    fun onDestroyView() {
        recyclerView = null
        emptyView = null
        progressView = null
    }
}