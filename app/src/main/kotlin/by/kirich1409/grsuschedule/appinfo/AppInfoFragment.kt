package by.kirich1409.grsuschedule.appinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.R
import by.kirich1409.grsuschedule.utils.isNotNullOrEmpty

/**
 * Created by kirillrozov on 11/4/15.
 */
class AppInfoFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_app_info)

        initPreferenceWIthLink(getString(R.string.pref_google_rate_app_key),
                getString(R.string.app_market_link))

        initPreferenceWIthLink(getString(R.string.pref_google_plus_community_key),
                getString(R.string.pref_google_plus_community_link))

        val versionNamePref = findPreference(getString(R.string.pref_app_version_key))
        versionNamePref.summary = BuildConfig.VERSION_NAME

        val versionBuildPref = findPreference(getString(R.string.pref_app_build_key))
        versionBuildPref.summary = BuildConfig.VERSION_CODE.toString()
    }

    private fun initPreferenceWIthLink(prefKey: String, link: String) {
        val preference = findPreference(prefKey)
        if (preference != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            if (BuildConfig.DEBUG) {
                preference.isEnabled =
                        activity.packageManager.queryIntentActivities(intent, 0).isNotNullOrEmpty()
            } else {
                preference.isVisible =
                        activity.packageManager.queryIntentActivities(intent, 0).isNotNullOrEmpty()
            }
        }
    }

}