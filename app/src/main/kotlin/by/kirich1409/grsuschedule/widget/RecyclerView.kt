package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.content.res.TypedArray
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import by.kirich1409.grsuschedule.R
import uk.co.androidalliance.edgeeffectoverride.ContextWrapperEdgeEffect

/**
 * Created by kirillrozov on 10/6/15.
 */
class RecyclerView : android.support.v7.widget.RecyclerView {

    public constructor(context: Context) : super(ContextWrapperEdgeEffect(context)) {
        init(context, null, 0)
    }

    public constructor(context: Context, attrs: AttributeSet?) : super(ContextWrapperEdgeEffect(context), attrs) {
        init(context, attrs, 0)
    }

    public constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(ContextWrapperEdgeEffect(context), attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        var color = ContextCompat.getColor(context, R.color.default_edgeeffect_color)

        if (attrs != null) {
            var a: TypedArray? = null
            try {
                a = context.obtainStyledAttributes(attrs, R.styleable.EdgeEffectView, defStyle, 0)!!
                color = a.getColor(R.styleable.EdgeEffectView_edgeeffect_color, color)
            } finally {
                a?.recycle()
            }
        }

        setEdgeEffectColor(color)
    }

    public fun setEdgeEffectColor(@ColorInt edgeEffectColor: Int) {
        (context as ContextWrapperEdgeEffect).setEdgeEffectColor(edgeEffectColor)
    }

    public fun setEdgeEffectColorRes(@ColorRes edgeEffectColor: Int) {
        setEdgeEffectColor(ContextCompat.getColor(context, edgeEffectColor))
    }
}