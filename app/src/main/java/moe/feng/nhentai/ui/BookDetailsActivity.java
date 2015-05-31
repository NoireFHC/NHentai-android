package moe.feng.nhentai.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;

import moe.feng.nhentai.R;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.util.ColorGenerator;
import moe.feng.nhentai.util.TextDrawable;

public class BookDetailsActivity extends AppCompatActivity {

	private ImageView imageView;

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

		CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(book.title);

		imageView = (ImageView) findViewById(R.id.app_bar_background);
		ViewCompat.setTransitionName(imageView, TRANSITION_NAME_IMAGE);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (book.previewImageUrl != null) {
							// TODO 显示本子的预览图
							switch (book.previewImageUrl) {
								case "0":
									imageView.setImageResource(R.drawable.holder_0);
									break;
								case "1":
									imageView.setImageResource(R.drawable.holder_1);
									break;
								case "2":
									imageView.setImageResource(R.drawable.holder_2);
									break;
							}
						} else {
							int color = ColorGenerator.MATERIAL.getColor(book.title);
							TextDrawable drawable = TextDrawable.builder().buildRect(book.title.substring(0, 1), color);
							imageView.setImageDrawable(drawable);
						}
					}
				});
			}
		}, 1000);
	}

	public static void launch(Activity activity, ImageView imageView, Book book) {
		ActivityOptionsCompat options = ActivityOptionsCompat
				.makeSceneTransitionAnimation(activity, imageView, TRANSITION_NAME_IMAGE);
		Intent intent = new Intent(activity, BookDetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		intent.putExtra(EXTRA_BOOK_DATA, book.toJSONString());
		ActivityCompat.startActivity(activity, intent, options.toBundle());
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

}
