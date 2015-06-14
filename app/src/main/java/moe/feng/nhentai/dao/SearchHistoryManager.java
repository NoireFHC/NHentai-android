package moe.feng.nhentai.dao;

import android.content.Context;

import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

import moe.feng.nhentai.R;
import sumimakito.android.quickkv.QuickKV;
import sumimakito.android.quickkv.database.KeyValueDatabase;

public class SearchHistoryManager {

	private QuickKV mQuickKV;
	private KeyValueDatabase mDB;

	private static SearchHistoryManager sInstance;

	private static final String DATABASE_NAME = "search_history";

	public static SearchHistoryManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SearchHistoryManager(context);
		}
		return sInstance;
	}

	public SearchHistoryManager(Context context) {
		mQuickKV = new QuickKV(context);
		reloadDatabase();
	}

	public void reloadDatabase() {
		mDB = mQuickKV.getDatabase(DATABASE_NAME);
	}

	public void add(String keyword) {
		int pos = find(keyword);
		if (pos < 0) {
			pos = 9;
		}
		moveArrayToNext(pos - 1);
		mDB.put("history_0", keyword);
		mDB.persist();
	}

	public String get(int pos) {
		return (String) mDB.get("history_" + pos);
	}

	public int find(String keyword) {
		for (int i = 9; i >= 0; i--) {
			if (mDB.containsKey("history_" + i)) {
				if (mDB.get("history_" + i).equals(keyword)){
					return i;
				}
			}
		}
		return -1;
	}

	private void moveArrayToNext(int end) {
		for (int i = end; i >= 0; i--) {
			if (mDB.containsKey("history_" + i)) {
				mDB.put("history_" + (i + 1), mDB.get("history_" + i));
			}
		}
	}

	public void cleanAll() {
		mDB.clear();
		mDB.persist();
	}

	public String[] getAll() {
		String[] histories = new String[10];
		for (int i = 0; i < 10; i++) {
			histories[i] = (String) mDB.get("history_" + i);
		}
		return histories;
	}

	public ArrayList<SearchResult> getSearchResults() {
		ArrayList<SearchResult> results = new ArrayList<>();
		for (String history : getAll()) {
			if (history == null) continue;
			results.add(new SearchResult(history, R.drawable.ic_history));
		}
		return results;
	}

}
