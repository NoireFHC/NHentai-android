package moe.feng.nhentai.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import moe.feng.nhentai.R;
import moe.feng.nhentai.api.BookApi;
import moe.feng.nhentai.cache.common.Constants;
import moe.feng.nhentai.cache.file.FileCacheManager;
import moe.feng.nhentai.model.BaseMessage;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.adapter.BookThumbHorizontalRecyclerAdapter;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;
import moe.feng.nhentai.util.AsyncTask;
import moe.feng.nhentai.util.ColorGenerator;
import moe.feng.nhentai.util.TextDrawable;

public class BookDetailsActivity extends AppCompatActivity {

	private ImageView imageView;
	private CollapsingToolbarLayout collapsingToolbar;
	private FloatingActionButton mFAB;
	private RecyclerView mRecyclerView;
	private TextView mOtherText;

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
		collapsingToolbar.setTitle(book.title);

		imageView = (ImageView) findViewById(R.id.app_bar_background);
		ViewCompat.setTransitionName(imageView, TRANSITION_NAME_IMAGE);

		mFAB = (FloatingActionButton) findViewById(R.id.fab);
		mRecyclerView = (RecyclerView) findViewById(R.id.book_thumb_list);
		mOtherText = (TextView) findViewById(R.id.tv_other);
		mFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GalleryActivity.launch(BookDetailsActivity.this, book, 0);
			}
		});

		updateDetailsContent();

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

		new CoverTask().execute(book);
	}

	private void updateDetailsContent() {
		mOtherText.setText(book.other);

		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.HORIZONTAL, false));

		BookThumbHorizontalRecyclerAdapter adapter = new BookThumbHorizontalRecyclerAdapter(mRecyclerView, book);
		adapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder) {
				GalleryActivity.launch(BookDetailsActivity.this, book, position);
			}
		});
		mRecyclerView.setAdapter(adapter);
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
