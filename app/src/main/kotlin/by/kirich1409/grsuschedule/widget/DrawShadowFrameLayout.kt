/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package by.kirich1409.grsuschedule.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import by.kirich1409.grsuschedule.R

class DrawShadowFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val mShadowDrawable: Drawable?
    private val mShadowVisible: Boolean
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.DrawShadowFrameLayout, 0, 0)

        mShadowDrawable = a.getDrawable(R.styleable.DrawShadowFrameLayout_shadowDrawable)
        if (mShadowDrawable != null) {
            mShadowDrawable.callback = this
        }

        mShadowVisible = a.getBoolean(R.styleable.DrawShadowFrameLayout_shadowVisible, true)
        setWillNotDraw(!mShadowVisible || mShadowDrawable == null)

        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        updateShadowBounds()
    }

    private fun updateShadowBounds() {
        mShadowDrawable?.setBounds(0, 0, mWidth, mHeight)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (mShadowDrawable != null && mShadowVisible) {
            mShadowDrawable.draw(canvas)
        }
    }
}
