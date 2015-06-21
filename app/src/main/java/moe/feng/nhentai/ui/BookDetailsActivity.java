package moe.feng.nhentai.ui;

import android.app.Activity;
import android.content.Intent;
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

import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

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
import moe.feng.nhentai.view.WheelProgressView;

public class BookDetailsActivity extends AppCompatActivity {

	private ImageView mImageView;
	private CollapsingToolbarLayout collapsingToolbar;
	private FloatingActionButton mFAB;
	private TextView mTitleText;
	private LinearLayout mTagsLayout;
	private LinearLayout mContentView;
	private WheelProgressView mProgressWheel;

	private Book book;

	private final static String EXTRA_BOOK_DATA = "book_data";
	private final static String TRANSITION_NAME_IMAGE = "BookDetailsActivity:image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_details);

		Intent intent = getIntent();
		book = new Gson().fromJson(intent.getStringExtra(EXTRA_BOOK_DATA), Book.class);

		Toolbar toolbar = $(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		collapsingToolbar = $(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(book.title);

		mImageView = $(R.id.app_bar_background);
		ViewCompat.setTransitionName(mImageView, TRANSITION_NAME_IMAGE);

		mFAB = $(R.id.fab);
		mTitleText = $(R.id.tv_title);
		mTagsLayout = $(R.id.book_tags_layout);
		mContentView = $(R.id.book_content);
		mProgressWheel = $(R.id.wheel_progress);

		FileCacheManager cm = FileCacheManager.getInstance(getApplicationContext());
		if (cm.cacheExistsUrl(Constants.CACHE_THUMB, book.previewImageUrl)) {
			Picasso.with(getApplicationContext())
					.load(cm.getBitmapUrlFile(Constants.CACHE_THUMB, book.previewImageUrl))
					.fit()
					.centerCrop()
					.into(mImageView, new Callback() {
						@Override
						public void onSuccess() {
							MaterialImageLoading.animate(mImageView).setDuration(1500).start();
						}

						@Override
						public void onError() {

						}
					});
		} else {
			int color = ColorGenerator.MATERIAL.getColor(book.title);
			TextDrawable drawable = TextDrawable.builder().buildRect(book.title.substring(0, 1), color);
			mImageView.setImageDrawable(drawable);
		}
		if (cm.cacheExistsUrl(Constants.CACHE_COVER, book.bigCoverImageUrl)) {
			Picasso.with(getApplicationContext())
					.load(cm.getBitmapUrlFile(Constants.CACHE_COVER, book.bigCoverImageUrl))
					.fit()
					.centerCrop()
					.into(mImageView, new Callback() {
						@Override
						public void onSuccess() {
							MaterialImageLoading.animate(mImageView).setDuration(1500).start();
						}

						@Override
						public void onError() {

						}
					});
		} else {
			new CoverTask().execute(book);
		}

		startBookGet();
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
		$(R.id.toolbar).invalidate();
		$(R.id.appbar).invalidate();

		mFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GalleryActivity.launch(BookDetailsActivity.this, book, 0);
			}
		});

		updateDetailsContent();
	}

	private void updateDetailsContent() {
		mProgressWheel.setVisibility(View.GONE);
		mContentView.setVisibility(View.VISIBLE);
		mContentView.animate().alphaBy(0f).alpha(1f).setDuration(1500).start();
		mTitleText.setText(TextUtils.isEmpty(book.titleJP) ? book.title : book.titleJP);

		updateTagsContent();
	}

	private void updateTagsContent() {
		int x = getResources().getDimensionPixelSize(R.dimen.tag_margin_x);
		int y = getResources().getDimensionPixelSize(R.dimen.tag_margin_y);
		int min_width = getResources().getDimensionPixelSize(R.dimen.tag_title_width);
		ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.TextTag);

		// Add Parodies Tags
		if (!TextUtils.isEmpty(book.parodies)) {
			LinearLayout tagGroupLayout = new LinearLayout(this);
			tagGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
			AutoWrapLayout tagLayout = new AutoWrapLayout(this);

			TextView groupNameView = new TextView(this);
			groupNameView.setMinWidth(min_width);
			groupNameView.setText(R.string.tag_type_parodies);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(x, y, x, y);
			lp.width = min_width;
			tagGroupLayout.addView(groupNameView, lp);

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
			tagLayout.addView(tagView, alp);
			tagGroupLayout.addView(tagLayout);
			mTagsLayout.addView(tagGroupLayout);
		}

		// Add Characters
		if (!book.characters.isEmpty()) {
			LinearLayout tagGroupLayout = new LinearLayout(this);
			tagGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
			AutoWrapLayout tagLayout = new AutoWrapLayout(this);

			TextView groupNameView = new TextView(this);
			groupNameView.setMinWidth(min_width);
			groupNameView.setText(R.string.tag_type_characters);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(x, y, x, y);
			lp.width = min_width;
			tagGroupLayout.addView(groupNameView, lp);

			for (final String tag : book.characters) {
				TextView tagView = new TextView(ctw);
				tagView.setText(tag);
				tagView.setBackgroundResource(R.color.deep_purple_800);
				tagView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						CategoryActivity.launch(
								BookDetailsActivity.this,
								NHentaiUrl.getCharacterUrl(tag),
								getString(R.string.tag_type_characters).trim() + " " + tag
						);
					}
				});
				AutoWrapLayout.LayoutParams alp = new AutoWrapLayout.LayoutParams();
				alp.setMargins(x, y, x, y);
				tagLayout.addView(tagView, alp);
			}
			tagGroupLayout.addView(tagLayout);
			mTagsLayout.addView(tagGroupLayout);
		}

		// Add Tags
		if (!book.tags.isEmpty()) {
			LinearLayout tagGroupLayout = new LinearLayout(this);
			tagGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
			AutoWrapLayout tagLayout = new AutoWrapLayout(this);

			TextView groupNameView = new TextView(this);
			groupNameView.setMinWidth(min_width);
			groupNameView.setText(R.string.tag_type_tag);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(x, y, x, y);
			lp.width = min_width;
			tagGroupLayout.addView(groupNameView, lp);

			for (final String tag : book.tags) {
				TextView tagView = new TextView(ctw);
				tagView.setText(tag);
				tagView.setBackgroundResource(R.color.deep_purple_800);
				tagView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						CategoryActivity.launch(
								BookDetailsActivity.this,
								NHentaiUrl.getTagUrl(tag),
								getString(R.string.tag_type_tag).trim() + " " + tag
						);
					}
				});
				AutoWrapLayout.LayoutParams alp1 = new AutoWrapLayout.LayoutParams();
				alp1.setMargins(x, y, x, y);
				tagLayout.addView(tagView, alp1);
			}
			tagGroupLayout.addView(tagLayout);
			mTagsLayout.addView(tagGroupLayout);
		}

		// Add Artist Tag
		if (!TextUtils.isEmpty(book.artist)) {
			LinearLayout tagGroupLayout = new LinearLayout(this);
			tagGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
			AutoWrapLayout tagLayout = new AutoWrapLayout(this);

			TextView groupNameView = new TextView(this);
			groupNameView.setMinWidth(min_width);
			groupNameView.setText(R.string.tag_type_artists);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(x, y, x, y);
			lp.width = min_width;
			tagGroupLayout.addView(groupNameView, lp);

			TextView tagView = new TextView(ctw);
			tagView.setText(book.artist);
			tagView.setBackgroundResource(R.color.deep_purple_800);
			tagView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CategoryActivity.launch(
							BookDetailsActivity.this,
							NHentaiUrl.getArtistUrl(book.artist),
							getString(R.string.tag_type_artists).trim() + " " + book.artist
					);
				}
			});
			AutoWrapLayout.LayoutParams alp = new AutoWrapLayout.LayoutParams();
			alp.setMargins(x, y, x, y);
			tagLayout.addView(tagView, alp);
			tagGroupLayout.addView(tagLayout);
			mTagsLayout.addView(tagGroupLayout);
		}

		// Add Groups Tag
		if (!TextUtils.isEmpty(book.group)) {
			LinearLayout tagGroupLayout = new LinearLayout(this);
			tagGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
			AutoWrapLayout tagLayout = new AutoWrapLayout(this);

			TextView groupNameView = new TextView(this);
			groupNameView.setMinWidth(min_width);
			groupNameView.setText(R.string.tag_type_group);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(x, y, x, y);
			lp.width = min_width;
			tagGroupLayout.addView(groupNameView, lp);

			TextView tagView = new TextView(ctw);
			tagView.setText(book.group);
			tagView.setBackgroundResource(R.color.deep_purple_800);
			tagView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CategoryActivity.launch(
							BookDetailsActivity.this,
							NHentaiUrl.getGroupUrl(book.group),
							getString(R.string.tag_type_group).trim() + " " + book.group
					);
				}
			});
			AutoWrapLayout.LayoutParams alp = new AutoWrapLayout.LayoutParams();
			alp.setMargins(x, y, x, y);
			tagLayout.addView(tagView, alp);
			tagGroupLayout.addView(tagLayout);
			mTagsLayout.addView(tagGroupLayout);
		}

		// Add Language Tag
		if (!TextUtils.isEmpty(book.language)) {
			LinearLayout tagGroupLayout = new LinearLayout(this);
			tagGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
			AutoWrapLayout tagLayout = new AutoWrapLayout(this);

			TextView groupNameView = new TextView(this);
			groupNameView.setText(R.string.tag_type_language);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(x, y, x, y);
			lp.width = min_width;
			tagGroupLayout.addView(groupNameView, lp);

			TextView tagView = new TextView(ctw);
			tagView.setText(book.language);
			tagView.setBackgroundResource(R.color.deep_purple_800);
			tagView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CategoryActivity.launch(
							BookDetailsActivity.this,
							NHentaiUrl.getLanguageUrl(book.language),
							getString(R.string.tag_type_language).trim() + " " + book.language
					);
				}
			});
			AutoWrapLayout.LayoutParams alp = new AutoWrapLayout.LayoutParams();
			alp.setMargins(x, y, x, y);
			tagLayout.addView(tagView, alp);
			tagGroupLayout.addView(tagLayout);
			mTagsLayout.addView(tagGroupLayout);
		}
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

	private void startBookGet() {
		mContentView.setVisibility(View.GONE);
		mProgressWheel.setVisibility(View.VISIBLE);
		mProgressWheel.spin();

		new BookGetTask().execute(book.bookId);
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
				mProgressWheel.setVisibility(View.GONE);

				Snackbar.make(
						$(R.id.main_content),
						R.string.tips_network_error,
						Snackbar.LENGTH_LONG
				).setAction(
						R.string.snack_action_try_again,
						new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								startBookGet();
							}
				}).show();
			}
		}

	}
	
	private class CoverTask extends AsyncTask<Book, Void, File> {

		@Override
		protected File doInBackground(Book... params) {
			return BookApi.getCoverFile(BookDetailsActivity.this, params[0]);
		}

		@Override
		protected void onPostExecute(File result) {
			Picasso.with(getApplicationContext())
					.load(result)
					.into(mImageView, new Callback() {
						@Override
						public void onSuccess() {
							MaterialImageLoading.animate(mImageView).setDuration(1500).start();
						}

						@Override
						public void onError() {

						}
					});
		}
	}

	protected <T extends View> T $(int id) {
		return (T) findViewById(id);
	}

}
