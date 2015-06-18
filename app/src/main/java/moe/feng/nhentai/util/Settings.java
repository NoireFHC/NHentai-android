package moe.feng.nhentai.util;

import android.content.Context;

import moe.feng.nhentai.dao.CommonPreferences;

public class Settings {

	public static final String PREFERENCES_NAME = "settings";

	private static Settings sInstance;

	private CommonPreferences mPrefs;

	public static Settings getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new Settings(context);
		}
		return sInstance;
	}

	private Settings(Context context) {
		mPrefs = CommonPreferences.getInstance(context, PREFERENCES_NAME);
	}

	public Settings putBoolean(String key, boolean value) {
		mPrefs.edit().putBoolean(key, value).commit();
		return this;
	}

	public boolean getBoolean(String key, boolean def) {
		return mPrefs.getBoolean(key, def);
	}

	public Settings putInt(String key, int value) {
		mPrefs.edit().putInt(key, value).commit();
		return this;
	}

	public int getInt(String key, int defValue) {
		return mPrefs.getInt(key, defValue);
	}


	public Settings putString(String key, String value) {
		mPrefs.edit().putString(key, value).commit();
		return this;
	}

	public String getString(String key, String defValue) {
		return mPrefs.getString(key, defValue);
	}

}
