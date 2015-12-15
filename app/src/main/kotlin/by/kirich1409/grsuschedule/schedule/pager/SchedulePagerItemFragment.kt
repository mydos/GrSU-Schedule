package by.kirich1409.grsuschedule.schedule.pager

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.RecyclerViewFragment
import by.kirich1409.grsuschedule.model.DaySchedule
import by.kirich1409.grsuschedule.schedule.list.ScheduleAdapter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlin.test.assertNotNull

/**
 * Created by kirillrozov on 10/9/15.
 */
public class SchedulePagerItemFragment : RecyclerViewFragment() {

    private lateinit var schedule: DaySchedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedule = arguments!!.getParcelable(ARG_SCHEDULE)
        assertNotNull(schedule)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = recyclerView!!
        recyclerView.addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context)
                        .colorResId(R.color.divider)
                        .sizeResId(R.dimen.divider_size)
                        .build()
        )
        recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS
        recyclerAdapter = ScheduleAdapter(context, schedule)
    }

    companion object {
        private val ARG_SCHEDULE = if (BuildConfig.DEBUG) "schedule" else "a"

        public fun newInstance(schedule: DaySchedule): SchedulePagerItemFragment {
            val fragment = SchedulePagerItemFragment()
            fragment.arguments = Bundle(1).apply { putParcelable(ARG_SCHEDULE, schedule) }
            return fragment
        }
    }
}