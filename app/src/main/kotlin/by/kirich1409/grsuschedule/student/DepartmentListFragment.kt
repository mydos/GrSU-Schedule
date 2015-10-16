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
import by.kirich1409.grsuschedule.model.Department
import by.kirich1409.grsuschedule.model.Departments
import by.kirich1409.grsuschedule.network.request.DepartmentsRequest
import by.kirich1409.grsuschedule.utils.setSupportActionBarSubtitle
import by.kirich1409.grsuschedule.utils.setSupportActionBarTitle

/**
 * Created by kirillrozov on 9/13/15.
 */
public class DepartmentListFragment : SimpleSpiceListFragment<Departments>() {

    private var listener: Listener? = null
    override val cacheKey = "departments"
    override val dataClass = Departments::class.java

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        } else if (BuildConfig.DEBUG) {
            throw RuntimeException(
                    "Host content must implements DepartmentListFragment.Listener interface.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBarTitle(R.string.label_department)
        activity.setSupportActionBarSubtitle(null)
    }

    override fun newSpiceRequest() = DepartmentsRequest()

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        listener!!.onDepartmentSelected(l.getItemAtPosition(position) as Department)
    }

    override fun onRequestSuccess(data: Departments) {
        listAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, data.items)
    }

    public interface Listener {
        fun onDepartmentSelected(department: Department)
    }
}
