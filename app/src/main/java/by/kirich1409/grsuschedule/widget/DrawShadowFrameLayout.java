
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

package by.kirich1409.grsuschedule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import by.kirich1409.grsuschedule.R;

public class DrawShadowFrameLayout extends FrameLayout {
    private Drawable mShadowDrawable;
    private boolean mShadowVisible;
    private int mWidth, mHeight;

    public DrawShadowFrameLayout(Context context) {
        this(context, null, 0);
    }

    public DrawShadowFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawShadowFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DrawShadowFrameLayout, 0, 0);

        mShadowDrawable = a.getDrawable(R.styleable.DrawShadowFrameLayout_shadowDrawable);
        if (mShadowDrawable != null) {
            mShadowDrawable.setCallback(this);
        }

        mShadowVisible = a.getBoolean(R.styleable.DrawShadowFrameLayout_shadowVisible, true);
        setWillNotDraw(!mShadowVisible || mShadowDrawable == null);

        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        updateShadowBounds();
    }

    private void updateShadowBounds() {
        if (mShadowDrawable != null) {
            mShadowDrawable.setBounds(0, 0, mWidth, mHeight);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mShadowDrawable != null && mShadowVisible) {
            mShadowDrawable.draw(canvas);
        }
    }
}
