package by.kirich1409.grsuschedule.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AnimRes
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import by.kirich1409.grsuschedule.R
import com.octo.android.robospice.exception.NoNetworkException
import com.octo.android.robospice.persistence.exception.SpiceException
import retrofit.RetrofitError
import kotlin.reflect.KClass
import android.support.v4.app.Fragment as SupportFragment

/**
 * Created by kirillrozov on 9/15/15.
 */

public fun <T : Activity> Context.startActivity(activityClass: KClass<T>) {
    startActivity(Intent(this, activityClass.java))
}

public fun Context.isTablet() = resources.getBoolean(R.bool.is_tablet)

public fun Context.isPhone() = resources.getBoolean(R.bool.is_phone)

public fun TextView.setDrawableTop(drawable: Drawable?) {
    val compoundDrawables = compoundDrawables
    setCompoundDrawablesWithIntrinsicBounds(
            compoundDrawables[0],
            drawable,
            compoundDrawables[2],
            compoundDrawables[3]
    )
}

public fun TextView.setDrawableTop(@DrawableRes drawableResId: Int) {
    setDrawableTop(ContextCompat.getDrawable(context, drawableResId))
}

public fun TextView.setTextOrHideIfEmpty(text: CharSequence?) {
    if (text == null) {
        visibility = View.GONE
    } else {
        this.text = text
        visibility = View.VISIBLE
    }
}

public fun AppCompatActivity.setSupportActionBarTitle(@StringRes titleResId: Int) {
    supportActionBar?.setTitle(titleResId)
}

public fun AppCompatActivity.setSupportActionBarSubtitle(subtitle: CharSequence?) {
    supportActionBar?.subtitle = subtitle
}

public fun Context.inflate(
        @LayoutRes layoutResId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View
        = LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

public fun String.startWithIgnoreCase(prefix: CharSequence?): Boolean {
    if (TextUtils.isEmpty(prefix)) {
        return true
    }

    val prefixLength = prefix!!.length
    if (length < prefixLength) {
        return false
    }

    for (i in 0 until prefixLength) {
        if (Character.toLowerCase(prefix[i]) != Character.toLowerCase(this[i])) {
            return false
        }
    }
    return true
}

public fun CharSequence.isDigitsOnly(): Boolean {
    val length = length
    if (length == 0) {
        return false;
    } else {
        for (i in 0 until length) {
            if (!this[i].isDigit()) return false
        }
        return true
    }
}

public fun Exception.getErrorMessage(context: Context): CharSequence {
    if (this is NoNetworkException) {
        return context.getText(R.string.error_no_network)
    } else if (this is SpiceException) {
        val cause = this.cause
        if (cause is RetrofitError) {
            val response = cause.response
            if (response != null) {
                if (response.status / 100 == clientErrorStatuses) {
                    return context.getText(R.string.error_client)
                } else if (response.status / 100 == serverErrorStatuses) {
                    return context.getText(R.string.error_server)
                }
            }
        }
    }
    return context.getText(R.string.error_unknown_error)
}

private const val serverErrorStatuses = 5
private const val clientErrorStatuses = 4

public fun View.fadeIn(animate: Boolean = true) {
    visibility = View.VISIBLE
    if (animate) {
        fade(1F, android.R.anim.fade_in)
    } else {
        ViewCompat.setAlpha(this, 1F)
    }
}

public fun View.fadeOut(animate: Boolean = true) {
    if (animate) {
        fade(0F, android.R.anim.fade_out)
    } else {
        visibility = View.GONE
        ViewCompat.setAlpha(this, 0F)
    }
}

private fun View.fade(alpha: Float, @AnimRes animRes: Int) {
    if (Build.VERSION.SDK_INT >= 14) {
        val mediumAnimTime =
                resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        ViewCompat.animate(this)
                .alpha(alpha)
                .setDuration(mediumAnimTime)
                .start()
    } else {
        val animation = AnimationUtils.loadAnimation(context, animRes)
        animation.setAnimationListener(FadeAnimationListener(this, alpha > 0F))
        startAnimation(animation)
    }
}

private class FadeAnimationListener(val view: View, val visible: Boolean) :
        AnimationListenerAdapter {

    override fun onAnimationEnd(animation: Animation) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}

public fun Collection<*>?.isNotNullOrEmpty() = this != null && isNotEmpty()
