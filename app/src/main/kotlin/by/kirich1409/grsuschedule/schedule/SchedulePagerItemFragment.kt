package by.kirich1409.grsuschedule.schedule

import android.os.Bundle
import android.view.View
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.RecyclerViewFragment
import by.kirich1409.grsuschedule.widget.adapter.ScheduleAdapter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import junit.framework.Assert

/**
 * Created by kirillrozov on 10/9/15.
 */
public class SchedulePagerItemFragment : RecyclerViewFragment() {

    private lateinit var schedule: DaySchedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedule = arguments!!.getParcelable(ARG_SCHEDULE)
        Assert.assertNotNull(schedule)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView!!.addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context)
                        .colorResId(R.color.divider)
                        .sizeResId(R.dimen.divider_size)
                        .build()
        )
        recyclerAdapter = ScheduleAdapter(context, schedule)
    }

    companion object {
        private val ARG_SCHEDULE = if (BuildConfig.DEBUG) "schedule" else "a"

        public fun newInstance(schedule: DaySchedule): SchedulePagerItemFragment {
            val args = Bundle(1)
            args.putParcelable(ARG_SCHEDULE, schedule)

            val fragment = SchedulePagerItemFragment()
            fragment.arguments = args
            return fragment
        }
    }
}