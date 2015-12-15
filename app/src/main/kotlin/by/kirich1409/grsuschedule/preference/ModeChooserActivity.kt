package by.kirich1409.grsuschedule.preference

import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.BaseActivity

/**
 * Created by kirillrozov on 9/14/15.
 */
public class ModeChooserActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, ModeChooserFragment())
                    .commit()
        }
    }

    override val screenName = "Mode Chooser"
}
