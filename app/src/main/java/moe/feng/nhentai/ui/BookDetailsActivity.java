package moe.feng.nhentai.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import moe.feng.nhentai.api.BookApi;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.fragment.BookDetailsFragment;
import moe.feng.nhentai.util.AsyncTask;
import moe.feng.nhentai.util.ColorGenerator;
import moe.feng.nhentai.util.TextDrawable;

public class BookDetailsActivity extends AppCompatActivity {

	private ImageView imageView;
	private CollapsingToolbarLayout collapsingToolbar;

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
		//collapsingToolbar.setTitle(book.title);

		imageView = (ImageView) findViewById(R.id.app_bar_background);
		ViewCompat.setTransitionName(imageView, TRANSITION_NAME_IMAGE);

		if (book.previewImageUrl != null) {
			// TODO Image should be loaded here but we are now in development state.
			/*switch (book.previewImageUrl) {
				case "0":
					// 假缓存
					imageView.setImageResource(R.drawable.holder_0);
					break;
				case "1":
					// 假缓存
					imageView.setImageResource(R.drawable.holder_1);
					break;
				case "2":
					// 假缓存
					imageView.setImageResource(R.drawable.holder_2);
					break;
				default:
					// TODO 找不到缓存，从网络中抽取数据
			}*/
		} else {
			int color = ColorGenerator.MATERIAL.getColor(book.title);
			TextDrawable drawable = TextDrawable.builder().buildRect(book.title.substring(0, 1), color);
			imageView.setImageDrawable(drawable);
		}

		getFragmentManager().beginTransaction()
				.replace(R.id.book_details_container, BookDetailsFragment.newInstance(book))
				.commit();

		new BookGetTask().execute("80150");
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class BookGetTask extends AsyncTask<String, Void, Book> {

		@Override
		protected Book doInBackground(String... params) {
			return BookApi.getBook(params[0]);
		}

		@Override
		protected void onPostExecute(Book result) {
			book = result;
			updateUIContent();
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
