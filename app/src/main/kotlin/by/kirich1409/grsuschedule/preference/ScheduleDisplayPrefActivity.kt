package by.kirich1409.grsuschedule.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseActivity

/**
 * Created by kirillrozov on 9/25/15.
 */
public class ScheduleDisplayPrefActivity() : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_display_pref)
    }

    companion object {
        public fun makeIntent(context: Context): Intent {
            return Intent(context, ScheduleDisplayPrefActivity::class.java)
        }
    }
}