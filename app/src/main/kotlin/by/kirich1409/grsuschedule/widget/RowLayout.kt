package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import by.kirich1409.grsuschedule.R

/**
 * Created by kirillrozov on 10/5/15.
 */
public class RowLayout : ViewGroup {

    var singleLine = false
        private set

    var minHorizontalSpace = 0
        set(space) {
            field = space
            invalidate()
        }

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RowLayout, defStyleAttr, 0)
        minHorizontalSpace = attributes.getDimensionPixelSize(R.styleable.RowLayout_minHorizontalSpace, 0)
        attributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount != 2) {
            throw IllegalStateException(
                    "RowLayout can has only two child views. Current count=$childCount")
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var width = paddingLeft + paddingRight + minHorizontalSpace

        val rhsView = getChildAt(0)
        if (rhsView.visibility != View.GONE) {
            measureChild(rhsView, widthMeasureSpec, heightMeasureSpec);
            width += rhsView.measuredWidth
        }

        val lhsView = getChildAt(1)
        if (lhsView.visibility != View.GONE) {
            measureChild(lhsView, widthMeasureSpec, heightMeasureSpec);
            width += lhsView.measuredWidth
        }

        var height: Int = paddingBottom + paddingTop
        singleLine = width <= measuredWidth

        if (singleLine) {
            height += Math.max(rhsView.measuredHeight, lhsView.measuredHeight)
        } else {
            height += rhsView.measuredHeight + lhsView.measuredHeight
        }

        width = Math.max(width, suggestedMinimumWidth)
        height = Math.max(height, suggestedMinimumHeight)

        setMeasuredDimension(
                ViewCompat.resolveSizeAndState(width, widthMeasureSpec, 0),
                ViewCompat.resolveSizeAndState(height, heightMeasureSpec, 0)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        if (singleLine) {
            val lhsView = getChildAt(0)
            val rhsView = getChildAt(1)
            lhsView.layout(paddingLeft,
                    paddingTop,
                    paddingLeft + lhsView.measuredWidth,
                    paddingTop + lhsView.measuredHeight)

            val rhsLeft = measuredWidth - paddingRight - rhsView.measuredWidth
            rhsView.layout(rhsLeft,
                    paddingTop,
                    rhsLeft + rhsView.measuredWidth,
                    paddingTop + rhsView.measuredHeight)
        } else {
            val topView = getChildAt(0)
            topView.layout(paddingLeft,
                    paddingTop,
                    paddingLeft + topView.measuredWidth,
                    paddingTop + topView.measuredHeight)

            val bottomView = getChildAt(1)
            bottomView.layout(paddingLeft,
                    topView.bottom,
                    paddingLeft + bottomView.measuredWidth,
                    topView.bottom + bottomView.measuredHeight)
        }
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?) = LayoutParams(p)

    override fun shouldDelayChildPressedState() = false

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?) = p is LayoutParams

    override fun generateLayoutParams(attrs: AttributeSet?) = LayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    public class LayoutParams : ViewGroup.LayoutParams {
        public constructor(c: Context, attrs: AttributeSet?) : super(c, attrs)

        public constructor(lp: ViewGroup.LayoutParams?) : super(lp)

        public constructor(width: Int, height: Int) : super(width, height)
    }
}