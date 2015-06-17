package moe.feng.nhentai.ui.fragment.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import moe.feng.nhentai.R;
import moe.feng.nhentai.ui.SettingsActivity;
import moe.feng.nhentai.view.pref.Preference;

public class SettingsMain extends PreferenceFragment implements android.preference.Preference.OnPreferenceClickListener {

	private Preference mLicensePref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_main);

		mLicensePref = (Preference) findPreference("license");

		mLicensePref.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(android.preference.Preference pref) {
		if (pref == mLicensePref) {
			SettingsActivity.launchActivity(getActivity(), SettingsActivity.FLAG_LICENSE);
			return true;
		}
		return false;
	}

}
