package by.kirich1409.grsuschedule.utils

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import by.kirich1409.grsuschedule.app.ListItemViewHolder
import kotlin.properties.ReadOnlyProperty
import android.support.v4.app.Fragment as SupportFragment

/**
 * Created by kirillrozov on 9/15/15.
 */

public fun <V : View> View.bindView(id: Int)
        : ReadOnlyProperty<View, V> = required(id, viewFinder)

public fun <V : View> ListItemViewHolder.bindView(id: Int)
        : ReadOnlyProperty<ListItemViewHolder, V> = required(id, viewFinder)

public fun <V : View> Activity.bindView(id: Int)
        : ReadOnlyProperty<Activity, V> = required(id, viewFinder)

public fun <V : View> Dialog.bindView(id: Int)
        : ReadOnlyProperty<Dialog, V> = required(id, viewFinder)

public fun <V : View> SupportFragment.bindView(id: Int)
        : ReadOnlyProperty<SupportFragment, V> = required(id, viewFinder)

public fun <V : View> Fragment.bindView(id: Int)
        : ReadOnlyProperty<Fragment, V> = required(id, viewFinder)

public fun <V : View> RecyclerView.ViewHolder.bindView(id: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, V> = required(id, viewFinder)

public fun <V : View> View.bindOptionalView(id: Int)
        : ReadOnlyProperty<View, V?> = optional(id, viewFinder)

public fun <V : View> Activity.bindOptionalView(id: Int)
        : ReadOnlyProperty<Activity, V?> = optional(id, viewFinder)

public fun <V : View> Dialog.bindOptionalView(id: Int)
        : ReadOnlyProperty<Dialog, V?> = optional(id, viewFinder)

public fun <V : View> SupportFragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<SupportFragment, V?> = optional(id, viewFinder)

public fun <V : View> Fragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<Fragment, V?> = optional(id, viewFinder)

public fun <V : View> RecyclerView.ViewHolder.bindOptionalView(id: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, V?> = optional(id, viewFinder)

private val View.viewFinder: View.(Int) -> View?
    get() = View::findViewById

private val ListItemViewHolder.viewFinder: ListItemViewHolder.(Int) -> View?
    get() = { rootView.findViewById(it) }

private val Activity.viewFinder: Activity.(Int) -> View?
    get() = Activity::findViewById

private val Dialog.viewFinder: Dialog.(Int) -> View?
    get() = Dialog::findViewById

private val RecyclerView.ViewHolder.viewFinder: RecyclerView.ViewHolder.(Int) -> View?
    get() = { itemView.findViewById(it) }

private val SupportFragment.viewFinder: SupportFragment.(Int) -> View?
    get() = { view.findViewById(it) }

private val Fragment.viewFinder: Fragment.(Int) -> View?
    get() = { view.findViewById(it) }

private fun viewNotFound(id: Int, desc: PropertyMetadata) =
        throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun required<T, V : View>(id: Int, finder: T.(Int) -> View?)
        = Lazy { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

@Suppress("UNCHECKED_CAST")
private fun optional<T, V : View>(id: Int, finder: T.(Int) -> View?)
        = Lazy { t: T, desc -> t.finder(id) as V? }

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, V>(private val initializer: (T, PropertyMetadata) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun get(thisRef: T, property: PropertyMetadata): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}
