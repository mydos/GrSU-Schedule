package by.kirich1409.grsuschedule.app

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.ScheduleApp
import by.kirich1409.grsuschedule.preference.AppPreference
import by.kirich1409.grsuschedule.preference.AppSettingsActivity
import by.kirich1409.grsuschedule.preference.ModeChooserActivity
import by.kirich1409.grsuschedule.schedule.ScheduleActivity
import by.kirich1409.grsuschedule.student.GroupChooserActivity
import by.kirich1409.grsuschedule.teacher.TeacherListActivity
import by.kirich1409.grsuschedule.utils.APP_MODE_UNKNOWN
import by.kirich1409.grsuschedule.utils.Constants
import by.kirich1409.grsuschedule.utils.canHandle
import kotlin.reflect.KClass

/**
 * Created by kirillrozov on 9/14/15.
 */
public abstract class DrawerActivity : BaseActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    protected lateinit var appPreference: AppPreference
        private set
    private var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreference = AppPreference(this)
    }

    override fun onContentChanged() {
        super.onContentChanged()

        val drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout?
        this.drawerLayout = drawerLayout
        if (drawerLayout != null) {
            drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {
                override fun onDrawerClosed(drawerView: View?) {
                    super.onDrawerClosed(drawerView)
                    supportInvalidateOptionsMenu()
                }

                override fun onDrawerOpened(drawerView: View?) {
                    super.onDrawerOpened(drawerView)
                    supportInvalidateOptionsMenu()
                }
            }
            drawerLayout.setDrawerListener(drawerToggle)
            drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT)
        }

        navigationView = findViewById(R.id.navigation) as NavigationView
        navigationView!!.setNavigationItemSelectedListener(this)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuVisible = !drawerLayout!!.isDrawerOpen(Gravity.LEFT)
        for (i in 0 until menu.size()) {
            menu.getItem(i).setVisible(menuVisible)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_my_schedule -> startSectionActivity(ScheduleActivity::class)
            R.id.navigation_groups -> startSectionActivity(GroupChooserActivity::class)
            R.id.navigation_teachers -> startSectionActivity(TeacherListActivity::class)
            R.id.navigation_share_app -> shareAppLink()
            R.id.navigation_schedule_preferences ->
                startActivity(Intent(this, AppSettingsActivity::class.java))
            R.id.navigation_change_app_mode -> startModePicker()
        }
        drawerLayout?.closeDrawers()
        return true
    }

    private fun shareAppLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType(Constants.MIMETYPE_TEXT_PLAIN)
        shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.GOOGLE_PLAY_APP_LINK)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_share_checkout));
        if (shareIntent.canHandle(packageManager)) {
            startActivity(Intent.createChooser(shareIntent, getText(R.string.app_share_title)));
        } else {
            val contentView = findViewById(R.id.content)
            if (contentView != null) {
                Snackbar.make(contentView, R.string.app_share_can_not_handle, Snackbar.LENGTH_LONG)
                        .show()
            }
        }
    }

    protected fun startModePicker() {
        val intent = Intent(this, ModeChooserActivity::class.java)
        startActivityForResult(intent, REQUEST_CHOOSE_MODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_MODE) {
            if (resultCode == Activity.RESULT_OK) {
                onModeChanged()
            } else if (resultCode == Activity.RESULT_CANCELED
                    && appPreference.mode == APP_MODE_UNKNOWN) {
                finish()
            }
        }
    }

    protected open fun onModeChanged() {
    }

    private fun startSectionActivity(cls: KClass<out Activity>) {
        if (cls.java === this.javaClass ) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            val intent = Intent(this, cls.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            if (this.javaClass != ScheduleActivity::class.java) {
                finish()
            }
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
