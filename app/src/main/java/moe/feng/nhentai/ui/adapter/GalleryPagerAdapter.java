package moe.feng.nhentai.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.fragment.BookPageFragment;

public class GalleryPagerAdapter extends FragmentPagerAdapter {

	private Book book;
	private Fragment[] fragments;

	public GalleryPagerAdapter(FragmentManager fm, Book book) {
		super(fm);
		this.book = book;
		this.fragments = new Fragment[book.pageCount];
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments[position] == null) {
			fragments[position] = BookPageFragment.newInstance(book, position + 1);
		}
		return fragments[position];
	}

	@Override
	public int getCount() {
		return book.pageCount;
	}

}
