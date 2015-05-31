package moe.feng.nhentai.model;

import com.google.gson.Gson;

public class Book {

	/** 必须获取到的数据 */
	public String title, other, bookId;

	/** 次要数据 */
	public String previewImageUrl;

	public Book(String title, String other, String bookId) {
		this.title = title;
		this.other = other;
		this.bookId = bookId;
	}

	public Book(String title, String other, String bookId, String previewImageUrl) {
		this(title, other, bookId);
		this.previewImageUrl = previewImageUrl;
	}

	public String toJSONString() {
		return new Gson().toJson(this);
	}

}
