package by.kirich1409.grsuschedule.student

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.SpiceListFragment
import by.kirich1409.grsuschedule.model.Department
import by.kirich1409.grsuschedule.model.Faculty
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.model.Groups
import by.kirich1409.grsuschedule.network.request.GroupsRequest
import by.kirich1409.grsuschedule.utils.GROUPS_TIME_CACHE
import com.octo.android.robospice.persistence.DurationInMillis
import com.octo.android.robospice.persistence.exception.SpiceException
import com.octo.android.robospice.request.listener.PendingRequestListener

/**
 * Created by kirillrozov on 9/13/15.
 */
public class GroupListFragment : SpiceListFragment() {

    private var listener: Listener? = null
    private var department: Department? = null
    private var faculty: Faculty? = null
    private var course = -1
    private val groupsRequestListener = GroupsListener()
    lateinit var cacheKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        department = args.getParcelable(ARG_DEPARTMENT)
        faculty = args.getParcelable(ARG_FACULTY)
        course = args.getInt(ARG_COURSE)
        cacheKey = "departmentId=${department!!.id},facultyId=${faculty!!.id},course=$course"
    }

    override fun loadData(force: Boolean) {
        spiceManager.getFromCacheAndLoadFromNetworkIfExpired(
                GroupsRequest(department!!.id, faculty!!.id, course),
                cacheKey,
                if (force) DurationInMillis.ALWAYS_EXPIRED else GROUPS_TIME_CACHE,
                groupsRequestListener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else if (BuildConfig.DEBUG) {
            throw RuntimeException(
                    "Host context must implements GroupListFragment.Listener interface")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity as AppCompatActivity
        val actionBar = activity.supportActionBar
        if (actionBar != null) {
            actionBar.title = getText(R.string.activity_label_group)
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
        if (listener != null) listener!!.onGroupSelected(l.getItemAtPosition(position) as Group)
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
            if (listAdapter == null) loadData(false)
        }
    }

    companion object {
        val ARG_COURSE = if (BuildConfig.DEBUG) "course" else "a"
        val ARG_DEPARTMENT = if (BuildConfig.DEBUG) "department" else "b"
        val ARG_FACULTY = if (BuildConfig.DEBUG) "faculty" else "c"

        public fun newInstance(department: Department, faculty: Faculty, course: Int): GroupListFragment {
            val fragment = GroupListFragment()
            fragment.arguments = Bundle(3).apply {
                putInt(ARG_COURSE, course)
                putParcelable(ARG_DEPARTMENT, department)
                putParcelable(ARG_FACULTY, faculty)
            }
            return fragment
        }
    }
}
