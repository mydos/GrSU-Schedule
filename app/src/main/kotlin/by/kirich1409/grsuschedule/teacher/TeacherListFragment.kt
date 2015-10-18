package by.kirich1409.grsuschedule.teacher

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.SimpleSpiceListFragment
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.model.Teachers
import by.kirich1409.grsuschedule.network.request.TeachersRequest
import by.kirich1409.grsuschedule.widget.TeacherAdapter
import com.octo.android.robospice.persistence.DurationInMillis
import com.octo.android.robospice.persistence.exception.SpiceException

/**
 * Created by kirillrozov on 9/14/15.
 */
public class TeacherListFragment : SimpleSpiceListFragment<Teachers>() {

    var listener: Listener? = null
    override val cacheKey = "teachers"
    override val dataClass = Teachers::class.java
    override val dataCacheDuration = DurationInMillis.ONE_DAY * 3

    var teachers: Teachers? = null
        set(value) {
            field = value
            activity?.supportInvalidateOptionsMenu()
        }

    val teacherLoaderCallback = TeacherLoaderCallbacks()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw RuntimeException(
                    "Host content must implements TeachersFragment.Listener interface.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView!!.isFastScrollEnabled = true
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        listener!!.onTeacherSelected(l.getItemAtPosition(position) as Teacher)
    }

    override fun newSpiceRequest() = TeachersRequest()

    override fun onRequestFailure(error: SpiceException) {
        teachers = null
    }

    override fun onRequestSuccess(data: Teachers) {
        val args = Bundle(1)
        args.putParcelable(ARG_TEACHERS, data)
        loaderManager.restartLoader(LOADER_TEACHERS, args, teacherLoaderCallback)

        this.teachers = data
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (teachers != null) {
            inflater.inflate(R.menu.fragment_teachers, menu)

            val searchTeachersMenuItem = menu.findItem(R.id.search_teachers)!!
            MenuItemCompat.setOnActionExpandListener(
                    searchTeachersMenuItem, TeachersSearchMenuExpandListener(menu))
            initSearchView(MenuItemCompat.getActionView(searchTeachersMenuItem) as SearchView)
        }
    }

    private fun initSearchView(searchView: SearchView) {
        searchView.queryHint = getText(R.string.search_teachers_hint)
        searchView.setIconifiedByDefault(true)
        searchView.inputType = InputType.TYPE_CLASS_TEXT and InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        searchView.setOnQueryTextListener(TeachersSearchQueryListener())
        searchView.setOnCloseListener({
            search("")
            true
        })
    }

    protected fun search(query: String) {
        if (teachers != null) {
            val args = Bundle(2)
            args.putParcelable(ARG_TEACHERS, teachers)
            args.putString(ARG_QUERY, query)
            loaderManager.restartLoader(LOADER_TEACHERS, args, teacherLoaderCallback)
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private inner class TeachersSearchQueryListener : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String): Boolean {
            search(query)
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            search(newText)
            return true
        }
    }

    private class TeachersSearchMenuExpandListener(val menu: Menu) :
            MenuItemCompat.OnActionExpandListener {

        override fun onMenuItemActionExpand(item: MenuItem): Boolean {
            setMenuItemsVisible(item, false)
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
            setMenuItemsVisible(item, true)
            return true
        }

        private fun setMenuItemsVisible(expandedItem: MenuItem, visible: Boolean) {
            for (i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)
                if (menuItem !== expandedItem) {
                    menuItem.setVisible(visible)
                }
            }
        }
    }

    private inner class TeacherLoaderCallbacks() : LoaderManager.LoaderCallbacks<TeacherAdapter.Data> {
        override fun onLoaderReset(loader: Loader<TeacherAdapter.Data>) {
        }

        override fun onLoadFinished(loader: Loader<TeacherAdapter.Data>, data: TeacherAdapter.Data) {
            listAdapter = TeacherAdapter(context, data)
        }

        override fun onCreateLoader(id: Int, args: Bundle): Loader<TeacherAdapter.Data> {
            val teachers = args.getParcelable<Teachers>(ARG_TEACHERS)
            val query = args.getString(ARG_QUERY)
            return TeacherAdapterDataLoader(context, teachers, query)
        }
    }

    public interface Listener {
        fun onTeacherSelected(teacher: Teacher)
    }

    companion object {
        val ARG_TEACHERS = if (BuildConfig.DEBUG) "teachers" else "a"
        val ARG_QUERY = if (BuildConfig.DEBUG) "query" else "b"
        const val LOADER_TEACHERS = 12312
    }
}
