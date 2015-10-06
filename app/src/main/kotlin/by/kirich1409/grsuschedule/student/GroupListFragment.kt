package by.kirich1409.grsuschedule.student

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.SpiceListFragment
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.model.Groups
import by.kirich1409.grsuschedule.network.request.GroupsRequest
import by.kirich1409.grsuschedule.utils.Constants
import com.octo.android.robospice.persistence.DurationInMillis
import com.octo.android.robospice.persistence.exception.SpiceException
import com.octo.android.robospice.request.listener.PendingRequestListener

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupListFragment : SpiceListFragment() {

    private var listener: Listener? = null
    private var departmentId: Int = -1
    private var facultyId: Int = -1
    private var course: Int = -1
    private val groupsRequestListener = GroupsListener()
    val cacheKey by lazy { "departmentId=$departmentId,facultyId=$facultyId,course=$course" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        course = args.getInt(ARG_COURSE)
        departmentId = args.getInt(ARG_DEPARTMENT_ID)
        facultyId = args.getInt(ARG_FACULTY_ID)
    }

    override fun loadData(force: Boolean) {
        spiceManager.getFromCacheAndLoadFromNetworkIfExpired(
                GroupsRequest(departmentId, facultyId, course),
                cacheKey,
                if (force) DurationInMillis.ALWAYS_EXPIRED else Constants.GROUPS_TIME_CACHE,
                groupsRequestListener)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw RuntimeException(
                    "Host content must implements GroupsFragment.Listener interface.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity as AppCompatActivity
        val actionBar = activity.supportActionBar
        if (actionBar != null) {
            actionBar.title = getText(R.string.label_groups)
            actionBar.subtitle = null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyText = getText(R.string.group_list_empty)
    }

    override fun onStart() {
        super.onStart()
        spiceManager.addListenerIfPending(Groups::class.java, cacheKey, groupsRequestListener)
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        if (listener != null) {
            listener!!.onGroupSelected(l.getItemAtPosition(position) as Group)
        }
    }

    public interface Listener {
        public fun onGroupSelected(group: Group)
    }

    private inner class GroupsListener : PendingRequestListener<Groups> {
        override fun onRequestFailure(spiceException: SpiceException) {
            setProgressVisible(false)
            Toast.makeText(context, "Error loading departments", Toast.LENGTH_SHORT).show()
        }

        override fun onRequestSuccess(groups: Groups) {
            listAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, groups.items)
        }

        override fun onRequestNotFound() {
            if (listAdapter == null) {
                loadData(false)
            }
        }
    }

    companion object {
        const val ARG_COURSE = "course"
        const val ARG_DEPARTMENT_ID = "departmentId"
        const val ARG_FACULTY_ID = "facultyId"

        public fun newInstance(departmentId: Int, facultyId: Int, course: Int): GroupListFragment {
            val args = Bundle(3)
            args.putInt(ARG_COURSE, course)
            args.putInt(ARG_DEPARTMENT_ID, departmentId)
            args.putInt(ARG_FACULTY_ID, facultyId)

            val fragment = GroupListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
