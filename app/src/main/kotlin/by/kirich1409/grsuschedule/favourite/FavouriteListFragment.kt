package by.kirich1409.grsuschedule.favourite

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.widget.SimpleCursorAdapter
import android.view.View
import android.widget.ListView
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R

import by.kirich1409.grsuschedule.app.ListFragment
import by.kirich1409.grsuschedule.db.ScheduleContract
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.schedule.ScheduleActivity

/**
 * Created by kirillrozov on 12/5/15.
 */
class FavouriteListFragment : ListFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val projection = arrayOf(
            ScheduleContract.Favourite._ID,
            ScheduleContract.Favourite.NAME,
            ScheduleContract.Favourite.TYPE,
            ScheduleContract.Favourite.CONTENT_ID
    )
    private val from = arrayOf(ScheduleContract.Favourite.NAME)
    private val to = intArrayOf(android.R.id.text1)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderManager.initLoader(0, null, this)
        emptyText = getText(R.string.favourite_list_empty)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        listAdapter = SimpleCursorAdapter(context, R.layout.simple_list_item_1, data, from, to, 0)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(context,
                ScheduleContract.Favourite.CONTENT_URI, projection, null, null, null)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val adapter = l.adapter as SimpleCursorAdapter;
        val cursor = adapter.cursor
        cursor.moveToPosition(position);

        val type = cursor.getString(cursor.getColumnIndex(ScheduleContract.Favourite.TYPE))
        val name = cursor.getString(cursor.getColumnIndex(ScheduleContract.Favourite.NAME))
        val contentId = cursor.getInt(cursor.getColumnIndex(ScheduleContract.Favourite.CONTENT_ID))

        when (type) {
            ScheduleContract.Favourite.TYPE_GROUP ->
                startActivity(ScheduleActivity.makeIntent(context, Group(contentId, name), true))

            ScheduleContract.Favourite.TYPE_TEACHER ->
                startActivity(ScheduleActivity.makeIntent(context, Teacher(contentId, name), true))

            else -> {
                if (BuildConfig.DEBUG) throw RuntimeException("Unhandled favourite type='$type'")
            }
        }
    }
}
