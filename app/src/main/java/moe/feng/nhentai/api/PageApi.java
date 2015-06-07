package moe.feng.nhentai.api;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import moe.feng.nhentai.api.common.NHentaiUrl;
import moe.feng.nhentai.model.Book;

public class PageApi {

	public static final String TAG = PageApi.class.getSimpleName();

	public static ArrayList<Book> getPageList(String url) {
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
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
				}
			}

			if (book.bookId != null && !book.bookId.isEmpty()) {
				books.add(book);
			}

			Log.i(TAG, "Get book: " + book.toJSONString());
		}

		return books;
	}

	public static ArrayList<Book> getHomePageList(int number) {
		return getPageList(NHentaiUrl.getHomePageUrl(number));
	}

}
