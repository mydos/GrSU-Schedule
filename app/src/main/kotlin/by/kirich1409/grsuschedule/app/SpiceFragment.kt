package by.kirich1409.grsuschedule.app

import android.os.Bundle
import android.support.v4.app.Fragment
import by.kirich1409.grsuschedule.network.ScheduleSpiceService
import com.octo.android.robospice.SpiceManager

/**
 * Created by kirillrozov on 9/13/15.
 */
public abstract class SpiceFragment : Fragment() {

    val spiceManager: SpiceManager = SpiceManager(ScheduleSpiceService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
