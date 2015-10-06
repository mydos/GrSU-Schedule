package by.kirich1409.grsuschedule.app

import android.content.Context
import android.view.View
import by.kirich1409.grsuschedule.R

/**
 * Created by kirillrozov on 9/15/15.
 */
public open class ListItemViewHolder(protected val context: Context, public val rootView: View) {
    init {
        rootView.setTag(R.id.view_holder, this)
    }
}

public fun View.getListItemViewHolder(): ListItemViewHolder = this.getTag(R.id.view_holder) as ListItemViewHolder