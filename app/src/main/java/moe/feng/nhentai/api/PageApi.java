package moe.feng.nhentai.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import moe.feng.nhentai.api.common.NHentaiUrl;
import moe.feng.nhentai.cache.file.FileCacheManager;
import moe.feng.nhentai.model.BaseMessage;
import moe.feng.nhentai.model.Book;

import static moe.feng.nhentai.cache.common.Constants.CACHE_PAGE_IMG;

public class PageApi {

	public static final String TAG = PageApi.class.getSimpleName();

	public static BaseMessage getPageList(String url) {
		BaseMessage result = new BaseMessage();

		Document doc;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			result.setCode(403);
			e.printStackTrace();
			return result;
		}

		Elements container = doc.getElementsByClass("outer-preview-container");

		ArrayList<Book> books = new ArrayList<>();

		for (Element e : container) {
			Book book = new Book();

			Element caption = e.getElementsByClass("caption").get(0);
			Element titleElement = caption.getElementsByTag("a").get(0);
			String bookId = titleElement.attr("href");
			bookId = bookId.substring(0, bookId.lastIndexOf("/"));
			bookId = bookId.substring(bookId.lastIndexOf("/") + 1, bookId.length());
			book.bookId = bookId;
			book.title = titleElement.text();

			Elements imgs = e.getElementsByTag("img");
			for (Element imge : imgs) {
				if (imge.hasAttr("src")) {
					String thumbUrl = imge.attr("src");
					thumbUrl = thumbUrl.substring(0, thumbUrl.lastIndexOf("/"));
					String galleryId = thumbUrl.substring(thumbUrl.lastIndexOf("/") + 1, thumbUrl.length());
					book.galleryId = galleryId;
					book.bigCoverImageUrl = NHentaiUrl.getBigCoverUrl(galleryId);
					book.previewImageUrl = NHentaiUrl.getThumbUrl(galleryId);
					try {
						book.thumbHeight = Integer.valueOf(imge.attr("height"));
						book.thumbWidth = Integer.valueOf(imge.attr("width"));
					} catch (Exception ex) {

					}
				}
			}

			if (book.bookId != null && !book.bookId.isEmpty()) {
				books.add(book);
			}

			Log.i(TAG, "Get book: " + book.toJSONString());
		}

		result.setCode(0);
		result.setData(books);

		return result;
	}

	public static BaseMessage getHomePageList(int number) {
		return getPageList(NHentaiUrl.getHomePageUrl(number));
	}

	public static BaseMessage getSearchPageList(String keyword, int number) {
		return getPageList(NHentaiUrl.getSearchUrl(keyword, number));
	}

	public static Bitmap getPageOriginImage(Context context, Book book, int page_num) {
		String url = NHentaiUrl.getOriginPictureUrl(book.galleryId, String.valueOf(page_num));
		FileCacheManager m = FileCacheManager.getInstance(context);

		if (!m.cacheExistsUrl(CACHE_PAGE_IMG, url) && !m.createCacheFromNetwork(CACHE_PAGE_IMG, url)) {
			return null;
		}

		return m.getBitmapUrl(CACHE_PAGE_IMG, url);
	}

}
