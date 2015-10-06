package by.kirich1409.grsuschedule.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by kirillrozov on 9/30/15.
 */
public class MarginItemDecoration(val marginLeft: Int = 0,
                                  val marginTop: Int = 0,
                                  val marginRight: Int = 0,
                                  val marginBottom: Int = 0,
                                  val filter: ((RecyclerView.ViewHolder) -> Boolean)? = null) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val viewHolder = parent.getChildViewHolder(view)
        if (filter == null || filter.invoke(viewHolder)) {
            outRect.bottom += marginBottom
            outRect.left += marginLeft
            outRect.right += marginRight
            outRect.top += marginTop
        }
    }
}