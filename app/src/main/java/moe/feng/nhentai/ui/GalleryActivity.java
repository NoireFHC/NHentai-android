package moe.feng.nhentai.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;

import moe.feng.nhentai.R;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.adapter.GalleryPagerAdapter;
import moe.feng.nhentai.ui.common.AbsActivity;

public class GalleryActivity extends AbsActivity {

	private Book book;
	private int page_num;

	private ViewPager mPager;
	private GalleryPagerAdapter mPagerAdpater;
	private View mAppBar;

	private static final String EXTRA_BOOK_DATA = "book_data", EXTRA_FISRT_PAGE = "first_page";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= 19) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			if (Build.VERSION.SDK_INT >= 21) {
				getWindow().setStatusBarColor(Color.TRANSPARENT);
				getWindow().setNavigationBarColor(Color.TRANSPARENT);
			}
		}

		Intent intent = getIntent();
		book = new Gson().fromJson(intent.getStringExtra(EXTRA_BOOK_DATA), Book.class);
		page_num = intent.getIntExtra(EXTRA_FISRT_PAGE, 0);

		setContentView(R.layout.activity_gallery);
	}

	@Override
	protected void setUpViews() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(book.titleJP != null ? book.titleJP : book.title);

		mAppBar = findViewById(R.id.my_app_bar);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdpater = new GalleryPagerAdapter(getFragmentManager(), book);
		mPager.setAdapter(mPagerAdpater);
		mPager.setCurrentItem(page_num, false);
	}

	public static void launch(Activity activity, Book book, int firstPageNum) {
		Intent intent = new Intent(activity, GalleryActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		intent.putExtra(EXTRA_BOOK_DATA, book.toJSONString());
		intent.putExtra(EXTRA_FISRT_PAGE, firstPageNum);
		activity.startActivity(intent);
	}

	public void toggleControlBar() {
		if (mAppBar.getAlpha() != 0f) {
			mAppBar.animate().alpha(0f).start();
		} else if (mAppBar.getAlpha() != 1f) {
			mAppBar.animate().alpha(1f).start();
		}
	}

}
