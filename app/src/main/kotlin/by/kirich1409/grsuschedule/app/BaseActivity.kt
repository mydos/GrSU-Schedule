package by.kirich1409.grsuschedule.app

import android.app.ActivityManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.ScheduleApp
import by.kirich1409.grsuschedule.utils.LOCALE_RU
import com.google.android.gms.analytics.HitBuilders

/**
 * Created by kirillrozov on 9/13/15.
 */
public abstract class BaseActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = LOCALE_RU
        resources.updateConfiguration(configuration, resources.displayMetrics)

        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (BuildConfig.ANALYTICS_ENABLE) {
            (application as ScheduleApp).tracker.setScreenName(screenName)
            trackScreenView()
        }
    }

    protected open fun trackScreenView() {
        (application as ScheduleApp).tracker.send(HitBuilders.ScreenViewBuilder().build())
    }

    abstract val screenName: String

    override fun onContentChanged() {
        super.onContentChanged()
        toolbar = findViewById(R.id.toolbar) as Toolbar?
        if (toolbar != null) {
            toolbar!!.title = title
            setSupportActionBar(toolbar)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return keyCode == KeyEvent.KEYCODE_MENU || super.onKeyDown(keyCode, event)
    }

    protected fun setTaskDescriptionCompat(label: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(ActivityManager.TaskDescription(label))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onHomeItemSelected()
            else -> super.onOptionsItemSelected(item)
        }
    }

    open fun onHomeItemSelected(): Boolean {
        val upIntent: Intent = NavUtils.getParentActivityIntent(this)
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(intent)
                    .startActivities()
        } else {
            NavUtils.navigateUpTo(this, upIntent)
        }
        return true
    }
}
