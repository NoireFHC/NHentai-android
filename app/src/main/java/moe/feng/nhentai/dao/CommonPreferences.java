package moe.feng.nhentai.dao;

import android.content.Context;

import java.util.ArrayList;

import sumimakito.android.quickkv.QuickKV;
import sumimakito.android.quickkv.database.KeyValueDatabase;

public class CommonPreferences {

	private QuickKV mQuickKV;
	private KeyValueDatabase mKVDB;
	private String mDBName;

	private static ArrayList<Instance> sInstances = new ArrayList<>();

	public static CommonPreferences getInstance(Context context, String dbName) {
		CommonPreferences sInstance = null;
		for (Instance i : sInstances) {
			if (i.dbName == dbName) {
				sInstance = i.preferences;
				break;
			}
		}
		if (sInstance == null) {
			sInstance = new CommonPreferences(context, dbName);
			sInstances.add(new Instance(sInstance, dbName));
		}
		return sInstance;
	}

	private CommonPreferences(Context context, String dbName) {
		this.mQuickKV = new QuickKV(context);
		this.mDBName = dbName;
		reload();
	}

	public void sync() {
		this.mKVDB.sync(true);
	}

	public void reload() {
		this.mKVDB = this.mQuickKV.getDatabase("prefs_" + mDBName);
	}

	public Editor edit() {
		return new Editor(mKVDB);
	}

	public int getInt(String key, int defValue) {
		return contains(key) ? (int) mKVDB.get(key) : defValue;
	}

	public String getString(String key, String defValue) {
		return contains(key) ? (String) mKVDB.get(key) : defValue;
	}

	public boolean getBoolean(String key, boolean defValue) {
		return contains(key) ? (boolean) mKVDB.get(key) : defValue;
	}

	public long getLong(String key, long defValue) {
		return contains(key) ? (long) mKVDB.get(key) : defValue;
	}

	public float getFloat(String key, float defValue) {
		return contains(key) ? (float) mKVDB.get(key) : defValue;
	}

	public boolean contains(String key) {
		return mKVDB.containsKey(key);
	}

	public class Editor {

		private KeyValueDatabase mKVDB;

		private Editor(KeyValueDatabase kvdb) {
			this.mKVDB = kvdb;
		}

		public Editor putBoolean(String key, boolean value) {
			this.mKVDB.put(key, value);
			return this;
		}

		public Editor putInt(String key, int value) {
			this.mKVDB.put(key, value);
			return this;
		}

		public Editor putString(String key, String value) {
			this.mKVDB.put(key, value);
			return this;
		}

		public Editor putLong(String key, long value) {
			this.mKVDB.put(key, value);
			return this;
		}

		public Editor putFloat(String key, float value) {
			this.mKVDB.put(key, value);
			return this;
		}

		public Editor remove(String key){
			this.mKVDB.remove(key);
			return this;
		}

		public void clear() {
			this.mKVDB.clear();
			this.mKVDB.persist();
		}

		public boolean commit() {
			return this.mKVDB.persist();
		}

	}

	private static class Instance {

		CommonPreferences preferences;
		String dbName;

		public Instance(CommonPreferences preferences, String dbName) {
			this.preferences = preferences;
			this.dbName = dbName;
		}

	}

}
