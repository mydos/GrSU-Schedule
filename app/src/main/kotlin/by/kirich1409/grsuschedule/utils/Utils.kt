package by.kirich1409.grsuschedule.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AnimRes
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import com.octo.android.robospice.exception.NoNetworkException
import com.octo.android.robospice.persistence.exception.SpiceException
import retrofit.RetrofitError
import android.support.v4.app.Fragment as SupportFragment

/**
 * Created by kirillrozov on 9/15/15.
 */

public fun TextView.setDrawableTop(drawable: Drawable?) {
    val compoundDrawables = compoundDrawables
    setCompoundDrawablesWithIntrinsicBounds(
            compoundDrawables[0],
            drawable,
            compoundDrawables[2],
            compoundDrawables[3]
    )
}

public fun View.setPadding2(
        paddingRight: Int = this.paddingRight,
        paddingTop: Int = this.paddingTop,
        paddingLeft: Int = this.paddingLeft,
        paddingBottom: Int = this.paddingBottom) {
    setPadding(paddingRight, paddingTop, paddingLeft, paddingBottom)
}

public fun TextView.setDrawableTop(@DrawableRes drawableResId: Int) {
    setDrawableTop(ContextCompat.getDrawable(context, drawableResId))
}

public fun Any.debugLog(tag: String, message: String) {
    if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
        Log.d(tag, message)
    }
}

public fun Any.debugLog(message: String) {
    debugLog(this.javaClass.simpleName, message)
}

public fun TextView.setTextOrHideIfEmpty(text: CharSequence?) {
    if (text == null) {
        visibility = View.GONE
    } else {
        this.text = text
        visibility = View.VISIBLE
    }
}

public fun AppCompatActivity.setSupportActionBarTitle(title: CharSequence?) {
    supportActionBar?.title = title
}

public fun AppCompatActivity.setSupportActionBarTitle(@StringRes titleResId: Int) {
    supportActionBar?.setTitle(titleResId)
}

public fun AppCompatActivity.setSupportActionBarSubtitle(subtitle: CharSequence?) {
    supportActionBar?.subtitle = subtitle
}

public fun AppCompatActivity.setSupportActionBarSubtitle(@StringRes subtitleResId: Int) {
    supportActionBar?.setSubtitle(subtitleResId)
}

public fun Context.inflate(
        @LayoutRes layoutResId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View
        = LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

@Suppress("UNCHECKED_CAST")
public fun <F : SupportFragment> FragmentActivity.findSupportFragmentByTag(tag: String): F
        = supportFragmentManager.findFragmentByTag(tag) as F

@Suppress("UNCHECKED_CAST")
public fun <F : SupportFragment>  FragmentActivity.findSupportFragmentById(id: Int): F
        = supportFragmentManager.findFragmentById(id) as F

public fun String.startWithIgnoreCase(prefix: CharSequence?): Boolean {
    if (TextUtils.isEmpty(prefix)) {
        return true
    }

    val prefixLength = prefix!!.length()
    if (length() < prefixLength) {
        return false
    }

    for (i in 0 until prefixLength) {
        if (Character.toLowerCase(prefix.charAt(i)) != Character.toLowerCase(charAt(i))) {
            return false
        }
    }
    return true
}

public fun CharSequence.isDigitsOnly(): Boolean {
    val length = length()
    if (length == 0) {
        return false;
    } else {
        for (i in 0 until length) {
            if (!charAt(i).isDigit()) return false
        }
        return true
    }
}

public fun Exception.getErrorMessage(context: Context): CharSequence {
    return when (this) {
        is NoNetworkException -> context.getText(R.string.error_no_network)
        is SpiceException -> {
            val cause = getCause()
            when (cause) {
                is RetrofitError -> {
                    when (cause.response.status / 100) {
                        clientErrorStatuses -> context.getText(R.string.error_client)
                        serverErrorStatuses -> context.getText(R.string.error_server)
                        else -> context.getText(R.string.error_unknown_error)
                    }
                }
                else -> context.getText(R.string.error_unknown_error)
            }
        }
        else -> context.getText(R.string.error_unknown_error)
    }
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

public fun Collection<*>?.isNullOrEmpty() = this == null || isEmpty()

public fun Collection<*>?.isNotNullOrEmpty() = this != null && isNotEmpty()

public fun Intent.canHandle(packageManager: PackageManager) =
        packageManager.queryIntentActivities(this, 0).isNotNullOrEmpty()
