package moe.feng.nhentai.ui.adapter;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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

	public static final String TAG = BookListRecyclerAdapter.class.getSimpleName();

	public BookListRecyclerAdapter(ArrayList<Book> data) {
		super();
		this.data = data;
		mColorGenerator = ColorGenerator.MATERIAL;
	}

	@Override
	public ClickableViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		bindContext(viewGroup.getContext());
		View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_book_card, viewGroup, false);
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
			mHolder.mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					Log.i(TAG, "You clicked item no." + i);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public class ViewHolder extends ClickableViewHolder {

		public ImageView mPreviewImageView;
		public TextView mTitleTextView, mOtherTextView;
		public ImageButton mMenuButton;

		public ListPopupWindow mPopupWindow;

		public ViewHolder(View itemView) {
			super(itemView);
			mPreviewImageView = (ImageView) itemView.findViewById(R.id.book_preview);
			mTitleTextView = (TextView) itemView.findViewById(R.id.book_title);
			mOtherTextView = (TextView) itemView.findViewById(R.id.book_other);
			mMenuButton = (ImageButton) itemView.findViewById(R.id.book_menu);

			mPopupWindow = new ListPopupWindow(getContext());
			mPopupWindow.setAdapter(new PopupAdapter(getContext(), new String[] {"Details", "Download", "Add to favorites"}));
			mPopupWindow.setAnchorView(mMenuButton);
			mMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mPopupWindow.show();
				}
			});
		}

		private class PopupAdapter extends BaseAdapter {

			private String[] menuItems;
			private Context mContext;

			public PopupAdapter(Context context, String[] menuItems){
				this.menuItems = menuItems;
				this.mContext = context;
			}

			private class MyViewHolder{
				TextView menuItemView;
			}

			@Override
			public int getCount() {
				return menuItems.length;
			}

			@Override
			public Object getItem(int position) {
				return menuItems[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				MyViewHolder holder;
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_menu_row, parent, false);

					holder = new MyViewHolder();
					holder.menuItemView = (TextView) convertView.findViewById(R.id.menu_text);
					convertView.setTag(holder);
				} else {
					holder = (MyViewHolder) convertView.getTag();
				}
				if (holder.menuItemView != null) {
					holder.menuItemView.setText(menuItems[position - 1]);
				}
				return convertView;
			}
		}

	}

}
