package by.kirich1409.grsuschedule.utils

import android.view.animation.Animation

/**
 * Created by kirillrozov on 10/6/15.
 */
interface AnimationListenerAdapter : Animation.AnimationListener {

    override fun onAnimationRepeat(animation: Animation) {
    }

    override fun onAnimationEnd(animation: Animation) {
    }

    override fun onAnimationStart(animation: Animation) {
    }
}