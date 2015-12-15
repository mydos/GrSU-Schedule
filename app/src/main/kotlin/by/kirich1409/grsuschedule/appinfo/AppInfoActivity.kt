package by.kirich1409.grsuschedule.appinfo

import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.DrawerActivity

/**
 * Created by kirillrozov on 11/4/15.
 */
class AppInfoActivity : DrawerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, AppInfoFragment())
                    .commit()
        }
    }

    override val screenName = "App info"
}