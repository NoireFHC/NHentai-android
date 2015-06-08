package moe.feng.nhentai.ui.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;

import moe.feng.nhentai.R;
import moe.feng.nhentai.api.PageApi;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.util.AsyncTask;

public class BookPageFragment extends Fragment {

	private Book book;
	private int pageNum;
	private SubsamplingScaleImageView mImageView;

	private static final String ARG_BOOK_DATA = "arg_book_data", ARG_PAGE_NUM = "arg_page_num";

	public static final String TAG = BookPageFragment.class.getSimpleName();

	public static BookPageFragment newInstance(Book book, int pageNum) {
		BookPageFragment fragment = new BookPageFragment();
		Bundle data = new Bundle();
		data.putString(ARG_BOOK_DATA, book.toJSONString());
		data.putInt(ARG_PAGE_NUM, pageNum);
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		book = new Gson().fromJson(data.getString(ARG_BOOK_DATA), Book.class);
		pageNum = data.getInt(ARG_PAGE_NUM);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View view = inflater.inflate(R.layout.fragment_book_page, container, false);

		mImageView = (SubsamplingScaleImageView) view.findViewById(R.id.image_view);
		new DownloadTask().execute();

		return view;
	}

	private class DownloadTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			return PageApi.getPageOriginImage(getActivity().getApplicationContext(), book, pageNum);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			if (result != null) {
				mImageView.setImage(ImageSource.bitmap(result));
				mImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
						if (actionBar.isShowing()) {
							actionBar.hide();
						} else {
							actionBar.show();
						}
					}
				});

				mImageView.setMaxScale(3.0f);

				final Runnable r = new Runnable() {
					@Override
					public void run() {
						float height = mImageView.getHeight();
						float sHeight = mImageView.getSHeight();
						float width = mImageView.getWidth();
						float sWidth = mImageView.getSWidth();
							if (height == 0){
								new Handler().postDelayed(this,500);
								return;
							}

							float hwRate = height / width;
							float shwRate = sHeight / sWidth;

							if ((sHeight > height) && shwRate > hwRate) { // Long image.
								PointF center = new PointF(sWidth / 2 , height / 2);
								float scale = 0.0f;
								if (sWidth * 2 > width){
									scale = width / sWidth; // Zoom in properly.
								}
								mImageView.setScaleAndCenter(scale, center);
							}
						}
				};
				new Handler().postDelayed(r,500);
			}
		}

	}

}
