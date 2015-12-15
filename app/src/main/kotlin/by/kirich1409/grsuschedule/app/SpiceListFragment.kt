package by.kirich1409.grsuschedule.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.network.ScheduleSpiceService
import com.octo.android.robospice.SpiceManager

/**
 * Created by kirillrozov on 9/13/15.
 */
public abstract class SpiceListFragment : ListFragment() {

    protected val spiceManager = SpiceManager(ScheduleSpiceService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    abstract fun loadData(force: Boolean)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.data_refresh, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                loadData(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    public fun hasListData(): Boolean {
        val listAdapter = listAdapter
        return listAdapter != null && listAdapter.count > 0
    }

    override fun onStart() {
        super.onStart()
        spiceManager.start(context)
    }

    override fun onStop() {
        spiceManager.shouldStop()
        super.onStop()
    }
}
