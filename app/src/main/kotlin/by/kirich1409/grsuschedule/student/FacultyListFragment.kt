package by.kirich1409.grsuschedule.student

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.SimpleSpiceListFragment
import by.kirich1409.grsuschedule.model.Faculties
import by.kirich1409.grsuschedule.model.Faculty
import by.kirich1409.grsuschedule.network.request.FacultiesRequest
import by.kirich1409.grsuschedule.utils.setSupportActionBarSubtitle
import by.kirich1409.grsuschedule.utils.setSupportActionBarTitle

/**
 * Created by kirillrozov on 9/13/15.
 */
public class FacultyListFragment : SimpleSpiceListFragment<Faculties>() {

    private var listener: Listener? = null
    override val cacheKey = "faculties"
    override val dataClass = Faculties::class.java

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else if (BuildConfig.DEBUG) {
            throw RuntimeException(
                    "Host context must implements FacultyListFragment.Listener interface.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBarTitle(R.string.activity_label_faculty)
        activity.setSupportActionBarSubtitle(null)
    }

    override fun newSpiceRequest() = FacultiesRequest()

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        listener!!.onFacultySelected(l.getItemAtPosition(position) as Faculty)
    }

    override fun onRequestSuccess(data: Faculties) {
        listAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, data.items)
    }

    public interface Listener {
        fun onFacultySelected(faculty: Faculty)
    }
}
