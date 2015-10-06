package by.kirich1409.grsuschedule.student

import android.content.Context
import android.content.Intent
import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.DrawerActivity
import by.kirich1409.grsuschedule.model.Group

/**
 * Created by kirillrozov on 9/14/15.
 */
public class GroupScheduleActivity : DrawerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        val group = intent.getParcelableExtra<Group>(EXTRA_GROUP)
        supportActionBar.subtitle = group.title

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, GroupScheduleFragment.newInstance(group.id))
                    .commit()
        }
    }

    companion object {
        private val EXTRA_GROUP = "group"

        public fun makeIntent(context: Context, group: Group): Intent {
            val intent = Intent(context, GroupScheduleActivity::class.java)
            intent.putExtra(EXTRA_GROUP, group)
            return intent
        }
    }
}
