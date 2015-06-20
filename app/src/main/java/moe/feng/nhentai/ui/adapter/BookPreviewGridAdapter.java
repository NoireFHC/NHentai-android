package moe.feng.nhentai.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import moe.feng.nhentai.R;
import moe.feng.nhentai.api.BookApi;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.util.AsyncTask;

public class BookPreviewGridAdapter extends BaseAdapter {

	private Book book;
	private Context mContext;

	public BookPreviewGridAdapter(Context context, Book book) {
		super();
		this.mContext = context;
		this.book = book;
	}

	@Override
	public int getCount() {
		return book.pageCount;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = View.inflate(mContext, R.layout.list_item_book_picture_thumb, null);

			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.mImageView.setLayoutParams(new GridView.LayoutParams(300, 100));
		holder.mNumberText.setText(position);

		new ImageDownloader().execute(holder.mImageView, position);

		return view;
	}

	private class ViewHolder {

		View mParentView;
		ImageView mImageView;
		TextView mNumberText;

		public ViewHolder(View itemView) {
			this.mParentView = itemView;
			this.mImageView = (ImageView) itemView.findViewById(R.id.image_view);
			this.mNumberText = (TextView) itemView.findViewById(R.id.number_text);
		}

	}

	private class ImageDownloader extends AsyncTask<Object, Object, Void> {

		@Override
		protected Void doInBackground(Object[] params) {
			View v = (View) params[0];
			ViewHolder h = (ViewHolder) v.getTag();

			if (v != null && !TextUtils.isEmpty(book.previewImageUrl)) {
				ImageView imgView = h.mImageView;

				Bitmap img = BookApi.getPageThumb(mContext, book, (int) params[1]);

				if (img != null) {
					publishProgress(new Object[]{v, img, imgView, book});
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Object[] values) {
			super.onProgressUpdate(values);

			View v = (View) values[0];

			if (!(v.getTag() instanceof ViewHolder)) {
				return;
			}

			Bitmap img = (Bitmap) values[1];
			ImageView iv = (ImageView) values[2];
			iv.setVisibility(View.VISIBLE);
			iv.setImageBitmap(img);
			iv.setTag(false);
		}


	}

}
