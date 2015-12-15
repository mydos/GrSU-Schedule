package by.kirich1409.grsuschedule.favourite

import android.os.Bundle
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.app.DrawerActivity

/**
 * Created by kirillrozov on 12/5/15.
 */
class FavouritesActivity : DrawerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, FavouriteListFragment())
                    .commit()
        }
    }

    override val screenName = "Favourites"
}