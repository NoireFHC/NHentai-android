package moe.feng.nhentai.api.common;

public class NHentaiUrl {

	public static final String NHENTAI_HOME = "http://nhentai.net";
	public static final String NHENTAI_I = "http://i.nhentai.net";

	public static String getSearchUrl(String content) {
		String targetContent = content;
		if (targetContent.contains(" ")) {
			targetContent = targetContent.replaceAll(" ", "+");
		}
		return NHENTAI_HOME + "/search/?q=" + targetContent;
	}

	public static String getBookDetailsUrl(String book_id) {
		return NHENTAI_HOME + "/g/" + book_id;
	}

	public static String getBookPageUrl(String book_id, int page_num) {
		return getBookDetailsUrl(book_id) + "/" + page_num;
	}

	public static String getGalleryUrl(String g_id) {
		return NHENTAI_I + "/galleries/" + g_id;
	}

	public static String getOriginPictureUrl(String g_id, String page_num) {
		return getPictureUrl(g_id, page_num, "jpg");
	}

	public static String getThumbPictureUrl(String g_id, String page_num) {
		return getPictureUrl(g_id, page_num + "t", "jpg");
	}

	public static String getThumbUrl(String g_id) {
		return getPictureUrl(g_id, "thumb", "jpg");
	}

	public static String getBigCoverUrl(String g_id) {
		// TODO Not all covers are jpgs
		return getPictureUrl(g_id, "cover", "jpg");
	}

	public static String getPictureUrl(String g_id, String page_num, String file_type) {
		return getGalleryUrl(g_id) + "/" + page_num + "." + file_type;
	}

	public static String getParodyUrl(String name) {
		String targetName = name;
		if (targetName.contains(" ")) {
			targetName = targetName.replaceAll(" ", "+");
		}
		return NHENTAI_HOME + "/parody/" + targetName;
	}

	public static String getCharacterUrl(String name) {
		String targetName = name;
		if (targetName.contains(" ")) {
			targetName = targetName.replaceAll(" ", "+");
		}
		return NHENTAI_HOME + "/character/" + targetName;
	}

	public static String getTagUrl(String tag) {
		String targetTag = tag;
		if (targetTag.contains(" ")) {
			targetTag = targetTag.replaceAll(" ", "+");
		}
		return NHENTAI_HOME + "/tagged/" + targetTag;
	}

	public static String getArtistUrl(String name) {
		String targetName = name;
		if (targetName.contains(" ")) {
			targetName = targetName.replaceAll(" ", "+");
		}
		return NHENTAI_HOME + "/artist/" + targetName;
	}

	public static String getGroupUrl(String name) {
		String targetName = name;
		if (targetName.contains(" ")) {
			targetName = targetName.replaceAll(" ", "+");
		}
		return NHENTAI_HOME + "/group/" + targetName;
	}

	public static String getLanguageUrl(String name) {
		String targetName = name;
		if (targetName.contains(" ")) {
			targetName = targetName.replaceAll(" ", "+");
		}
		return NHENTAI_HOME + "/language/" + targetName;
	}

	public static String getHomePageUrl(int page) {
		return NHENTAI_HOME + "/?page=" + page;
	}

}
