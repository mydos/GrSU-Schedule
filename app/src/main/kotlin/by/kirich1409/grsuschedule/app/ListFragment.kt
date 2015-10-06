package by.kirich1409.grsuschedule.app

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.utils.fadeIn
import by.kirich1409.grsuschedule.utils.fadeOut
import by.kirich1409.grsuschedule.utils.getErrorMessage
import by.kirich1409.grsuschedule.utils.setDrawableTop

public open class ListFragment : Fragment() {

    open var listAdapter: ListAdapter? = null
        set(adapter) {
            field = adapter
            val listView = this.listView
            if (listView != null) {
                listView.adapter = adapter
                setProgressVisible(false)

                val emptyView = emptyView
                if (emptyView is TextView) {
                    emptyView.setDrawableTop(null)
                }
            }
        }

    public var listView: ListView? = null
        private set

    var emptyView: TextView? = null
        private set

    var progressView: View? = null
        private set

    var emptyText: CharSequence? = null
        set(emptyText) {
            field = emptyText

            val emptyView = this.emptyView
            if (emptyView != null) {
                emptyView.text = emptyText
            }
        }

    public fun setProgressVisible(visible: Boolean, animate: Boolean = true) {
        val listView = listView!!
        if (visible) {
            listView.fadeOut(animate)
            emptyView?.fadeOut(animate)
            progressView?.fadeIn(animate)
        } else {
            progressView?.fadeOut(animate)

            val adapter = listView.adapter
            if (adapter != null && adapter.count > 0) {
                emptyView?.fadeOut(animate)
                listView.fadeIn(animate)
            } else {
                listView.fadeOut(animate)
                emptyView?.fadeIn(animate)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(android.R.id.list) as ListView
        emptyView = view.findViewById(android.R.id.empty) as TextView?
        progressView = view.findViewById(android.R.id.progress)
        setProgressVisible(true, false)

        listView!!.setOnItemClickListener(
                { l, v, position, id -> onListItemClick(l as ListView, v!!, position, id) })
        if (emptyText != null) {
            this.emptyText = emptyText
        }
        if (listAdapter != null) {
            this.listAdapter = listAdapter
        }
    }

    override fun onDestroyView() {
        listView = null
        progressView = null
        emptyView = null
        super.onDestroyView()
    }

    public open fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
    }

    public fun setSelection(position: Int) = listView!!.setSelection(position)
    public fun getSelectedItemPosition(): Int = listView!!.selectedItemPosition
    public fun getSelectedItemId(): Long = listView!!.selectedItemId

    public fun showError(exception: Exception) {
        val listAdapter = listAdapter
        if (listAdapter == null || listAdapter.count == 0) {
            emptyView!!.text = exception.getErrorMessage(context)
            emptyView!!.setDrawableTop(R.drawable.ic_sad_smile_180dp)
            setProgressVisible(false)
        }
    }
}
