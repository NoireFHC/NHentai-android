package moe.feng.nhentai.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import moe.feng.nhentai.R;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;

public class BookThumbHorizontalRecyclerAdapter extends AbsRecyclerViewAdapter {

	private Book book;

	public BookThumbHorizontalRecyclerAdapter(Book book) {
		super();
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
			mHolder.mNumberText.setText(position);
			try {
				String url = book.bookImageThumbUrl.get(position);
				// TODO Show preview
				switch (url) {
					case "0":
						mHolder.mImageView.setImageResource(R.drawable.holder_0);
						break;
					case "1":
						mHolder.mImageView.setImageResource(R.drawable.holder_1);
						break;
					case "2":
						mHolder.mImageView.setImageResource(R.drawable.holder_2);
						break;
					default:
						// TODO Cannot find cache
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getItemCount() {
		return book.bookImageOriginUrl.size();
	}

	public class ViewHolder extends ClickableViewHolder {

		public TextView mNumberText;
		public ImageView mImageView;

		public ViewHolder(View itemView) {
			super(itemView);
			mNumberText = (TextView) itemView.findViewById(R.id.number_text);
			mImageView = (ImageView) itemView.findViewById(R.id.image_view);
		}

	}

}
