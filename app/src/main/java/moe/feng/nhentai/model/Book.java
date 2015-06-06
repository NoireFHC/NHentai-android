package moe.feng.nhentai.model;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Book {

	/** 必须获取到的数据 */
	public String title, other, bookId;

	/** 次要数据 */
	public String previewImageUrl, bigCoverImageUrl, titleJP, galleryId;
	public int pageCount;

	public ArrayList<String> bookImageThumbUrl;
	public ArrayList<String> bookImageOriginUrl;

	public Book() {
		this(null, null, null);
	}

	public Book(String title, String other, String bookId) {
		this.title = title;
		this.other = other;
		this.bookId = bookId;
		this.bookImageThumbUrl = new ArrayList<>();
		this.bookImageOriginUrl = new ArrayList<>();
	}

	public Book(String title, String other, String bookId, String previewImageUrl) {
		this(title, other, bookId);
		this.previewImageUrl = previewImageUrl;
	}

	public String toJSONString() {
		return new Gson().toJson(this);
	}

}
