package by.kirich1409.grsuschedule.preference

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.content.SharedPreferencesCompat
import by.kirich1409.grsuschedule.model.Group
import by.kirich1409.grsuschedule.model.Teacher
import by.kirich1409.grsuschedule.utils.APP_MODE_UNKNOWN

/**
 * Created by kirillrozov on 9/14/15.
 */
public class AppPreference(context: Context) {

    private val preferences: SharedPreferences
            by lazy { context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    var mode: Int
        @AppMode get() = preferences.getInt(KEY_MODE, APP_MODE_UNKNOWN)
        set(@AppMode mode) {
            val editor = preferences.edit()
            editor.putInt(KEY_MODE, mode)
            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor)
        }

    var teacher: Teacher?
        get() {
            val id = preferences.getInt(KEY_TEACHER_ID, -1)
            if (id > 0) {
                val post = preferences.getString(KEY_TEACHER_POST, null)
                val email = preferences.getString(KEY_TEACHER_EMAIL, null)
                val fullName = preferences.getString(KEY_TEACHER_FULL_NAME, null)
                return Teacher(id, post, email, fullName);
            } else {
                return null
            }
        }
        set(teacher) {
            val editor = preferences.edit()
            if (teacher is Teacher) {
                editor.putInt(KEY_TEACHER_ID, teacher.id)
                        .putString(KEY_TEACHER_POST, teacher.post)
                        .putString(KEY_TEACHER_EMAIL, teacher.fullname)
                        .putString(KEY_TEACHER_FULL_NAME, teacher.fullname)
            } else {
                editor.remove(KEY_TEACHER_ID)
                        .remove(KEY_TEACHER_POST)
                        .remove(KEY_TEACHER_EMAIL)
                        .remove(KEY_TEACHER_FULL_NAME)
            }
            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor)
        }

    var group: Group?
        get() {
            val id = preferences.getInt(KEY_GROUP_ID, -1)
            if (id > 0) {
                val title = preferences.getString(KEY_GROUP_TITLE, null)
                return Group(id, title)
            } else {
                return null
            }
        }
        set(group) {
            val editor = preferences.edit()
            if (group != null) {
                editor.putInt(KEY_GROUP_ID, group.id)
                        .putString(KEY_GROUP_TITLE, group.title)
            } else {
                editor.remove(KEY_GROUP_ID)
                        .remove(KEY_GROUP_TITLE)
            }
            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor)
        }


    public fun registerOnPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    public fun unregisterOnPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val PREF_NAME = "appPref"
        private const val KEY_GROUP_ID = "group.id"
        private const val KEY_GROUP_TITLE = "group.title"
        private const val KEY_TEACHER_ID = "teacher.id"
        private const val KEY_TEACHER_POST = "teacher.post"
        private const val KEY_TEACHER_EMAIL = "teacher.email"
        private const val KEY_TEACHER_FULL_NAME = "teacher.fullName"

        const val KEY_MODE = "mode"
    }
}
