package moe.feng.nhentai.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import moe.feng.nhentai.api.common.NHentaiUrl;
import moe.feng.nhentai.cache.file.FileCacheManager;
import moe.feng.nhentai.model.BaseMessage;
import moe.feng.nhentai.model.Book;
import static moe.feng.nhentai.cache.common.Constants.CACHE_COVER;
import static moe.feng.nhentai.cache.common.Constants.CACHE_THUMB;

public class BookApi {

	public static final String TAG = BookApi.class.getSimpleName();

	public static BaseMessage getBook(String id) {
		BaseMessage result = new BaseMessage();

		String url = NHentaiUrl.getBookDetailsUrl(id);

		Document doc;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			result.setCode(403);
			e.printStackTrace();
			return result;
		}

		Book book = new Book();

		Elements info = doc.getElementsByAttributeValue("id", "info");
		Element element = info.get(0);

		/** Get basic info */
		book.title = element.getElementsByTag("h1").get(0).text();
		try {
			book.titleJP = element.getElementsByTag("h2").get(0).text();
		} catch (Exception e) {
			Log.v(TAG, "This book hasn\'t japanese name.");
		}
		book.bookId = id;

		/** Get page count */
		String htmlSrc = element.html();
		try {
			int position = htmlSrc.indexOf("pages");
			String s = htmlSrc.substring(0, position);
			System.out.println(s);
			s = s.substring(s.lastIndexOf("<div>") + "<div>".length(), s.length()).trim();
			System.out.println(s);
			book.pageCount = Integer.valueOf(s);
		} catch (Exception e) {

		}

		/** Get gallery id and preview image url */
		Element coverDiv = doc.getElementById("cover").getElementsByTag("a").get(0);
		for (Element e : coverDiv.getElementsByTag("img")) {
			try {
				Log.i(TAG, coverDiv.html());
				String coverUrl = e.attr("src");
				Log.i(TAG, coverUrl);
				coverUrl = coverUrl.substring(0, coverUrl.lastIndexOf("/"));
				String galleryId = coverUrl.substring(coverUrl.lastIndexOf("/") + 1, coverUrl.length());
				book.galleryId = galleryId;
				book.previewImageUrl = NHentaiUrl.getThumbUrl(galleryId);
				book.bigCoverImageUrl = NHentaiUrl.getBigCoverUrl(galleryId);
				break;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		Log.i(TAG, book.toJSONString());

		result.setCode(0);
		result.setData(book);

		return result;
	}
	
	public static Bitmap getCover(Context context, Book book) {
		String url = book.bigCoverImageUrl;
		FileCacheManager m = FileCacheManager.getInstance(context);
		
		if (!m.cacheExistsUrl(CACHE_COVER, url) && !m.createCacheFromNetwork(CACHE_COVER, url)) {
			return null;
		}
		
		return m.getBitmapUrl(CACHE_COVER, url);
	}

	public static Bitmap getThumb(Context context, Book book) {
		String url = book.previewImageUrl;
		FileCacheManager m = FileCacheManager.getInstance(context);

		if (!m.cacheExistsUrl(CACHE_THUMB, url) && !m.createCacheFromNetwork(CACHE_THUMB, url)) {
			return null;
		}

		return m.getBitmapUrl(CACHE_THUMB, url);
	}

}
