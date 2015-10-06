package by.kirich1409.grsuschedule.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.grsuschedule.network.ScheduleSpiceService
import com.octo.android.robospice.SpiceManager

/**
 * Created by kirillrozov on 9/25/15.
 */
public open class SpiceRecyclerViewFragment : Fragment() {

    val recyclerView: RecyclerView?
        get() = delegate.recyclerView
    private val delegate by lazy { RecyclerViewFragmentDelegate(context) }
    protected val spiceManager = SpiceManager(ScheduleSpiceService::class.java)
    var recyclerAdapter: RecyclerView.Adapter<*>?
        get() = delegate.recyclerAdapter
        set(adapter) {
            delegate.recyclerAdapter = adapter
        }
    var layoutManager: RecyclerView.LayoutManager
        get() = delegate.layoutManager
        set(layoutManager) {
            delegate.layoutManager = layoutManager
        }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return delegate.onCreateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegate.onViewCreated(view)
    }

    fun setProgressVisible(visible: Boolean, animate: Boolean = true) {
        delegate.setProgressVisible(visible, animate)
    }

    fun showError(error: Exception) {
        delegate.showError(error)
    }

    override fun onResume() {
        super.onResume()
        spiceManager.start(context)
    }

    override fun onPause() {
        spiceManager.shouldStop()
        super.onPause()
    }

    override fun onDestroyView() {
        delegate.onDestroyView()
        super.onDestroyView()
    }
}