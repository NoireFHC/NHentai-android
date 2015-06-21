package moe.feng.nhentai.ui.fragment.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import moe.feng.nhentai.R;
import moe.feng.nhentai.ui.SettingsActivity;
import moe.feng.nhentai.view.pref.Preference;

public class SettingsMain extends PreferenceFragment implements Preference.OnPreferenceClickListener {

	private Preference mVersionPref;
	private Preference mLicensePref;
	private Preference mWeiboPref;
	private Preference mGooglePlusPref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_main);

		mVersionPref = (Preference) findPreference("version");
		mLicensePref = (Preference) findPreference("license");
		mWeiboPref = (Preference) findPreference("weibo");
		mGooglePlusPref = (Preference) findPreference("google_plus");

		String version = "Unknown";
		try {
			version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
			version += " (" + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode + ")";
		} catch (Exception e) {

		}
		mVersionPref.setSummary(version);

		mLicensePref.setOnPreferenceClickListener(this);
		mWeiboPref.setOnPreferenceClickListener(this);
		mGooglePlusPref.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(android.preference.Preference pref) {
		if (pref == mLicensePref) {
			SettingsActivity.launchActivity(getActivity(), SettingsActivity.FLAG_LICENSE);
			return true;
		}
		if (pref == mWeiboPref) {
			openWebUrl("http://weibo.com/fython");
			return true;
		}
		if (pref == mGooglePlusPref) {
			openWebUrl("https://plus.google.com/+FungJichun");
			return true;
		}
		return false;
	}

	private void openWebUrl(String url) {
		Uri uri = Uri.parse(url);
		startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}

}
