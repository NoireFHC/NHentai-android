package moe.feng.nhentai.cache.file;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import moe.feng.nhentai.cache.common.Constants;

public class OfflineDocumentManager {

	private FileCacheManager mCacheManager;

	private static OfflineDocumentManager sInstance;

	public static OfflineDocumentManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new OfflineDocumentManager(context);
		}

		return sInstance;
	}

	private OfflineDocumentManager(Context context) {
		mCacheManager = FileCacheManager.getInstance(context);
	}

	public String getOfflineDocument(String url) {
		if (mCacheManager.cacheExistsUrl(Constants.CACHE_DOCUMENT, url)) {
			BufferedReader bf = new BufferedReader(
					new InputStreamReader(mCacheManager.openCacheStreamUrl(Constants.CACHE_DOCUMENT, url))
			);
			StringBuffer buffer = new StringBuffer();
			String line = "";
			try {
				while ((line = bf.readLine()) != null){
					buffer.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return buffer.toString();
		} else {
			return null;
		}
	}

	public boolean hasOfflineCache(String url) {
		return mCacheManager.cacheExistsUrl(Constants.CACHE_DOCUMENT, url);
	}

	public boolean createOfflineCache(String url) {
		return mCacheManager.createCacheFromNetwork(Constants.CACHE_DOCUMENT, url);
	}

}
