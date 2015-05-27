package moe.feng.nhentai.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import moe.feng.nhentai.R;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;
import moe.feng.nhentai.util.ColorGenerator;
import moe.feng.nhentai.util.TextDrawable;

public class BookListRecyclerAdapter extends AbsRecyclerViewAdapter {

	private ArrayList<Book> data;

	private ColorGenerator mColorGenerator;

	public BookListRecyclerAdapter(ArrayList<Book> data) {
		super();
		this.data = data;
		mColorGenerator = ColorGenerator.MATERIAL;
	}

	@Override
	public ClickableViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.list_item_book_card, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ClickableViewHolder holder, int position) {
		if (holder instanceof ViewHolder) {
			ViewHolder mHolder = (ViewHolder) holder;
			mHolder.mTitleTextView.setText(data.get(position).title);
			mHolder.mOtherTextView.setText(data.get(position).other);
			String previewImageUrl = data.get(position).previewImageUrl;
			if (previewImageUrl != null) {
				// TODO 显示本子的预览图
			} else {
				int color = mColorGenerator.getColor(data.get(position).title);
				TextDrawable drawable = TextDrawable.builder().buildRound(data.get(position).title.substring(0, 1), color);
				mHolder.mPreviewImageView.setImageDrawable(drawable);
			}
		}
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public class ViewHolder extends ClickableViewHolder {

		public ImageView mPreviewImageView;
		public TextView mTitleTextView, mOtherTextView;

		public ViewHolder(View itemView) {
			super(itemView);
			mPreviewImageView = (ImageView) itemView.findViewById(R.id.book_preview);
			mTitleTextView = (TextView) itemView.findViewById(R.id.book_title);
			mOtherTextView = (TextView) itemView.findViewById(R.id.book_other);
		}

	}

}
