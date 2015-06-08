package moe.feng.nhentai.ui.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;

import moe.feng.nhentai.R;
import moe.feng.nhentai.api.PageApi;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.GalleryActivity;
import moe.feng.nhentai.util.AsyncTask;
import uk.co.senab.photoview.PhotoViewAttacher;

public class BookPageFragment extends Fragment {

	private Book book;
	private int pageNum;
	private ImageView mImageView;
	private PhotoViewAttacher mPhotoViewAttacher;

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

		mImageView = (ImageView) view.findViewById(R.id.image_view);
		mPhotoViewAttacher = new PhotoViewAttacher(mImageView);
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
				mImageView.setImageBitmap(result);
				mPhotoViewAttacher.update();
				mPhotoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
					@Override
					public void onViewTap(View view, float v, float v1) {
						((GalleryActivity) getActivity()).toggleControlBar();
					}
				});
			}
		}

	}

}
