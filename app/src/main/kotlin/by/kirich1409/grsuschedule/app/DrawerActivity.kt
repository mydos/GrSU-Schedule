package by.kirich1409.grsuschedule.app

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ShareCompat
import android.support.v4.content.IntentCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.favourite.FavouritesActivity
import by.kirich1409.grsuschedule.preference.AppPreference
import by.kirich1409.grsuschedule.preference.AppSettingsActivity
import by.kirich1409.grsuschedule.preference.ModeChooserActivity
import by.kirich1409.grsuschedule.schedule.ScheduleActivity
import by.kirich1409.grsuschedule.student.GroupChooserActivity
import by.kirich1409.grsuschedule.teacher.TeacherListActivity
import by.kirich1409.grsuschedule.utils.*
import kotlin.reflect.KClass

/**
 * Created by kirillrozov on 9/14/15.
 */
public abstract class DrawerActivity : BaseActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    protected lateinit var appPreference: AppPreference
    private var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreference = AppPreference(this)
    }

    override fun onContentChanged() {
        super.onContentChanged()

        val drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        this.drawerLayout = drawerLayout
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(drawerToggle)
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT)

        val navigationView = drawerLayout.findViewById(R.id.navigation) as NavigationView
        this.navigationView = navigationView
        navigationView.setNavigationItemSelectedListener(this)

        // Fix for crash with save state
        navigationView.id = View.NO_ID

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val visibility = !drawerLayout!!.isDrawerOpen(Gravity.LEFT)
        for (i in 0 until menu.size()) {
            menu.getItem(i).setVisible(visibility)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_my_schedule -> startSectionActivity(ScheduleActivity::class)
            R.id.navigation_favourites -> startSectionActivity(FavouritesActivity::class)
            R.id.navigation_groups -> startSectionActivity(GroupChooserActivity::class)
            R.id.navigation_teachers -> startSectionActivity(TeacherListActivity::class)
            R.id.navigation_share_app -> shareAppLink()
            R.id.navigation_settings -> startActivity(AppSettingsActivity::class)
            R.id.navigation_feedback -> sendFeedback()
        }
        drawerLayout!!.closeDrawers()
        return true
    }

    private fun sendFeedback() {
        val intent = ShareCompat.IntentBuilder.from(this)
                .setType(MIMETYPE_MESSAGE_RFC822)
                .setEmailTo(arrayOf(getString(R.string.feedback_email)))
                .setSubject(getString(R.string.feedback_subject))
                .setChooserTitle(R.string.feedback_send_dialog_title)
                .createChooserIntent();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            val message = getText(R.string.feedback_send_impossible)
            Snackbar.make(findViewById(R.id.content), message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun shareAppLink() {
        val intent = ShareCompat.IntentBuilder.from(this)
                .setType(MIMETYPE_TEXT_PLAIN)
                .setSubject(getString(R.string.app_share_checkout))
                .setChooserTitle(R.string.app_share_title)
                .setText(GOOGLE_PLAY_APP_LINK)
                .createChooserIntent();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
            trackShareAppLink()
        } else {
            val message = getText(R.string.app_share_can_not_handle)
            Snackbar.make(findViewById(R.id.content), message, Snackbar.LENGTH_LONG).show()
        }
    }

    protected fun startModePicker() {
        val intent = Intent(this, ModeChooserActivity::class.java)
        startActivityForResult(intent, REQUEST_CHOOSE_MODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHOOSE_MODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    onModeChanged()
                } else if (resultCode == Activity.RESULT_CANCELED
                        && appPreference.mode == APP_MODE_UNKNOWN) {
                    finish()
                }
            }
        }
    }

    protected open fun onModeChanged() {
    }

    protected fun startSectionActivity(cls: KClass<out Activity>, force: Boolean = false) {
        if (!force && cls.java === this.javaClass ) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            startActivity(IntentCompat.makeRestartActivityTask(ComponentName(this, cls.java)))
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return drawerToggle?.onOptionsItemSelected(item) as Boolean || super.onOptionsItemSelected(item)
    }

    companion object {
        private const val REQUEST_CHOOSE_MODE = 1123
    }
}
