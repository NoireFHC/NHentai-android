package moe.feng.nhentai.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import moe.feng.nhentai.R;
import moe.feng.nhentai.api.BookApi;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;
import moe.feng.nhentai.util.AsyncTask;

public class BookThumbHorizontalRecyclerAdapter extends AbsRecyclerViewAdapter {

	private Book book;

	public BookThumbHorizontalRecyclerAdapter(RecyclerView recyclerView, Book book) {
		super(recyclerView);
		this.book = book;
	}

	@Override
	public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		bindContext(parent.getContext());
		View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_book_picture_thumb, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ClickableViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);
		if (holder instanceof ViewHolder) {
			ViewHolder mHolder = (ViewHolder) holder;
			mHolder.mNumberText.setText(Integer.toString(position));
			try {
				new ImageDownloader().execute(mHolder.getParentView(), position + 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getItemCount() {
		return book.pageCount;
	}


	private class ImageDownloader extends AsyncTask<Object, Object, Void> {

		@Override
		protected Void doInBackground(Object[] params) {
			View v = (View) params[0];
			ViewHolder h = (ViewHolder) v.getTag();

			if (v != null && !TextUtils.isEmpty(book.previewImageUrl)) {
				ImageView imgView = h.mImageView;

				Bitmap img = BookApi.getPageThumb(getContext(), book, (int) params[1]);

				if (img != null) {
					publishProgress(new Object[]{v, img, imgView});
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Object[] values) {
			super.onProgressUpdate(values);

			View v = (View) values[0];

			if (!(v.getTag() instanceof ViewHolder) || book != null) {
				return;
			}

			Bitmap img = (Bitmap) values[1];
			ImageView iv = (ImageView) values[2];
			iv.setVisibility(View.VISIBLE);
			iv.setImageBitmap(img);
			iv.setTag(false);
		}


	}

	public class ViewHolder extends ClickableViewHolder {

		public TextView mNumberText;
		public ImageView mImageView;

		public ViewHolder(View itemView) {
			super(itemView);
			mNumberText = (TextView) itemView.findViewById(R.id.number_text);
			mImageView = (ImageView) itemView.findViewById(R.id.image_view);

			itemView.setTag(this);
		}

	}

}
