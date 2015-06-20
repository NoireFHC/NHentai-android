package moe.feng.nhentai.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import moe.feng.nhentai.R;
import moe.feng.nhentai.api.BookApi;
import moe.feng.nhentai.api.common.NHentaiUrl;
import moe.feng.nhentai.cache.common.Constants;
import moe.feng.nhentai.cache.file.FileCacheManager;
import moe.feng.nhentai.model.BaseMessage;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.util.AsyncTask;
import moe.feng.nhentai.util.ColorGenerator;
import moe.feng.nhentai.util.TextDrawable;
import moe.feng.nhentai.view.AutoWrapLayout;

public class BookDetailsActivity extends AppCompatActivity {

	private ImageView imageView;
	private CollapsingToolbarLayout collapsingToolbar;
	private FloatingActionButton mFAB;
	private TextView mTitleText;
	private LinearLayout mTagsLayout;

	private Book book;

	private final static String EXTRA_BOOK_DATA = "book_data";
	private final static String TRANSITION_NAME_IMAGE = "BookDetailsActivity:image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_details);

		Intent intent = getIntent();
		book = new Gson().fromJson(intent.getStringExtra(EXTRA_BOOK_DATA), Book.class);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle("");

		imageView = (ImageView) findViewById(R.id.app_bar_background);
		ViewCompat.setTransitionName(imageView, TRANSITION_NAME_IMAGE);

		mFAB = (FloatingActionButton) findViewById(R.id.fab);
		mTitleText = (TextView) findViewById(R.id.tv_title);
		mTagsLayout = (LinearLayout) findViewById(R.id.book_tags_layout);

		FileCacheManager cm = FileCacheManager.getInstance(getApplicationContext());
		if (cm.cacheExistsUrl(Constants.CACHE_THUMB, book.previewImageUrl)) {
			imageView.setImageBitmap(cm.getBitmapUrl(Constants.CACHE_THUMB, book.previewImageUrl));
		} else {
			int color = ColorGenerator.MATERIAL.getColor(book.title);
			TextDrawable drawable = TextDrawable.builder().buildRect(book.title.substring(0, 1), color);
			imageView.setImageDrawable(drawable);
		}
		if (cm.cacheExistsUrl(Constants.CACHE_COVER, book.bigCoverImageUrl)) {
			imageView.setImageBitmap(cm.getBitmapUrl(Constants.CACHE_COVER, book.bigCoverImageUrl));
		}

		new BookGetTask().execute(book.bookId);
	}

	public static void launch(Activity activity, ImageView imageView, Book book) {
		ActivityOptionsCompat options = ActivityOptionsCompat
				.makeSceneTransitionAnimation(activity, imageView, TRANSITION_NAME_IMAGE);
		Intent intent = new Intent(activity, BookDetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		intent.putExtra(EXTRA_BOOK_DATA, book.toJSONString());
		ActivityCompat.startActivity(activity, intent, options.toBundle());
	}

	private void updateUIContent() {
		collapsingToolbar.setTitle(book.title);
		collapsingToolbar.invalidate();
		findViewById(R.id.toolbar).invalidate();
		findViewById(R.id.appbar).invalidate();

		mFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GalleryActivity.launch(BookDetailsActivity.this, book, 0);
			}
		});

		updateDetailsContent();

		new CoverTask().execute(book);
	}

	private void updateDetailsContent() {
		mTitleText.setText(TextUtils.isEmpty(book.titleJP) ? book.title : book.titleJP);

		updateTagsContent();
	}

	private void updateTagsContent() {
		int x = getResources().getDimensionPixelSize(R.dimen.tag_margin_x);
		int y = getResources().getDimensionPixelSize(R.dimen.tag_margin_y);
		ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.TextTag);

		// Add Parodies Tags
		LinearLayout tagGroupLayout0 = new LinearLayout(this);
		tagGroupLayout0.setOrientation(LinearLayout.HORIZONTAL);
		AutoWrapLayout tagLayout0 = new AutoWrapLayout(this);

		TextView groupNameView0 = new TextView(this);
		groupNameView0.setText(R.string.tag_type_parodies);
		LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp0.setMargins(x, y, x, y);
		tagGroupLayout0.addView(groupNameView0, lp0);

		TextView tagView = new TextView(ctw);
		tagView.setText(book.parodies);
		tagView.setBackgroundResource(R.color.deep_purple_800);
		tagView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CategoryActivity.launch(
						BookDetailsActivity.this,
						NHentaiUrl.getParodyUrl(book.parodies),
						getString(R.string.tag_type_parodies).trim() + " " + book.parodies
				);
			}
		});
		AutoWrapLayout.LayoutParams alp = new AutoWrapLayout.LayoutParams();
		alp.setMargins(x, y, x, y);
		tagLayout0.addView(tagView, alp);
		tagGroupLayout0.addView(tagLayout0);
		mTagsLayout.addView(tagGroupLayout0);

		// Add Tags
		LinearLayout tagGroupLayout1 = new LinearLayout(this);
		tagGroupLayout1.setOrientation(LinearLayout.HORIZONTAL);
		AutoWrapLayout tagLayout1 = new AutoWrapLayout(this);

		TextView groupNameView1 = new TextView(this);
		groupNameView1.setText(R.string.tag_type_tag);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(x, y, x, y);
		tagGroupLayout1.addView(groupNameView1, lp);

		for (final String tag : book.tags) {
			TextView tagView1 = new TextView(ctw);
			tagView1.setText(tag);
			tagView1.setBackgroundResource(R.color.deep_purple_800);
			tagView1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CategoryActivity.launch(
							BookDetailsActivity.this,
							NHentaiUrl.getParodyUrl(tag),
							getString(R.string.tag_type_tag).trim() + " " + tag
					);
				}
			});
			AutoWrapLayout.LayoutParams alp1 = new AutoWrapLayout.LayoutParams();
			alp1.setMargins(x, y, x, y);
			tagLayout1.addView(tagView1, alp1);
		}
		tagGroupLayout1.addView(tagLayout1);
		mTagsLayout.addView(tagGroupLayout1);

		// Add Language Tag
		LinearLayout tagGroupLayout2 = new LinearLayout(this);
		tagGroupLayout2.setOrientation(LinearLayout.HORIZONTAL);
		AutoWrapLayout tagLayout2 = new AutoWrapLayout(this);

		TextView groupNameView2 = new TextView(this);
		groupNameView2.setText(R.string.tag_type_language);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.setMargins(x, y, x, y);
		tagGroupLayout2.addView(groupNameView2, lp2);

		TextView tagView2 = new TextView(ctw);
		tagView2.setText(book.language);
		tagView2.setBackgroundResource(R.color.deep_purple_800);
		tagView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CategoryActivity.launch(
						BookDetailsActivity.this,
						NHentaiUrl.getParodyUrl(book.language),
						getString(R.string.tag_type_language).trim() + " " + book.language
				);
			}
		});
		AutoWrapLayout.LayoutParams alp2 = new AutoWrapLayout.LayoutParams();
		alp.setMargins(x, y, x, y);
		tagLayout2.addView(tagView2, alp2);
		tagGroupLayout2.addView(tagLayout2);
		mTagsLayout.addView(tagGroupLayout2);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class BookGetTask extends AsyncTask<String, Void, BaseMessage> {

		@Override
		protected BaseMessage doInBackground(String... params) {
			return BookApi.getBook(params[0]);
		}

		@Override
		protected void onPostExecute(BaseMessage result) {
			if (result.getCode() == 0) {
				book = result.getData();
				updateUIContent();
			} else {
				Snackbar.make(findViewById(R.id.main_content), R.string.tips_network_error, Snackbar.LENGTH_LONG).show();
			}
		}

	}
	
	private class CoverTask extends AsyncTask<Book, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Book... params) {
			return BookApi.getCover(BookDetailsActivity.this, params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			imageView.setImageBitmap(result);
		}
	}

}
