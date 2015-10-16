package by.kirich1409.grsuschedule.preference

import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseActivity

/**
 * Created by kirillrozov on 9/25/15.
 */
public class AppSettingsActivity() : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_display_pref)

        val actionBar = supportActionBar
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
    }

    override fun onHomeItemSelected(): Boolean {
        finish()
        return true
    }
}