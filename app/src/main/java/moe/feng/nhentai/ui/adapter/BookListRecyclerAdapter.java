package moe.feng.nhentai.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
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
import moe.feng.nhentai.api.BookApi;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;
import moe.feng.nhentai.util.AsyncTask;
import moe.feng.nhentai.util.ColorGenerator;
import moe.feng.nhentai.util.TextDrawable;

public class BookListRecyclerAdapter extends AbsRecyclerViewAdapter {

	private ArrayList<Book> data;

	private ColorGenerator mColorGenerator;

	private boolean mScrolling = false;

	public static final String TAG = BookListRecyclerAdapter.class.getSimpleName();

	public BookListRecyclerAdapter(RecyclerView recyclerView, ArrayList<Book> data) {
		super(recyclerView);
		this.data = data;
		mColorGenerator = ColorGenerator.MATERIAL;
		this.addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView v, int state) {
				mScrolling = state != RecyclerView.SCROLL_STATE_IDLE;

				if (!mScrolling) {
					RecyclerView.LayoutManager manager = v.getLayoutManager();

					int from = -1;
					int to = -1;
					if (manager instanceof LinearLayoutManager) {
						LinearLayoutManager lm = (LinearLayoutManager) manager;
						from = lm.findFirstVisibleItemPosition();
						to = lm.findLastVisibleItemPosition();
					} else if (manager instanceof StaggeredGridLayoutManager) {
						StaggeredGridLayoutManager sgm = (StaggeredGridLayoutManager) manager;
						from = sgm.findFirstVisibleItemPositions(new int[2])[0];
						to = sgm.findLastVisibleItemPositions(new int[2])[1];
					}

					if (from > -1 && to > -1) {
						for (int i = from; i <= to; i++) {
							new ImageDownloader().execute(v.getChildAt(i - from));
						}
					}
				}
			}

			@Override
			public void onScrolled(RecyclerView p1, int p2, int p3) {

			}
		});
	}

	@Override
	public ClickableViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		bindContext(viewGroup.getContext());
		View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_book_card, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onViewRecycled(ClickableViewHolder holder) {
		super.onViewRecycled(holder);
		
		if (holder instanceof ViewHolder) {
			ViewHolder h = (ViewHolder) holder;
			h.mPreviewImageView.setImageBitmap(null);
			h.mPreviewImageView.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public void onBindViewHolder(ClickableViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);
		if (holder instanceof ViewHolder) {
			ViewHolder mHolder = (ViewHolder) holder;
			mHolder.mTitleTextView.setText(data.get(position).title);
			String previewImageUrl = data.get(position).previewImageUrl;
			if (previewImageUrl != null) {
				// TODO 显示本子的预览图
				switch (previewImageUrl) {
					case "0":
						mHolder.mPreviewImageView.setImageResource(R.drawable.holder_0);
						break;
					case "1":
						mHolder.mPreviewImageView.setImageResource(R.drawable.holder_1);
						break;
					case "2":
						mHolder.mPreviewImageView.setImageResource(R.drawable.holder_2);
						break;
					default:
						// TODO 找不到缓存，从网络中抽取数据
				}
			} else {
				int color = mColorGenerator.getColor(data.get(position).title);
				TextDrawable drawable = TextDrawable.builder().buildRect(data.get(position).title.substring(0, 1), color);
				mHolder.mPreviewImageView.setImageDrawable(drawable);
			}
			mHolder.mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					Log.i(TAG, "You clicked item no." + i);
				}
			});
			mHolder.book = data.get(position);
		}
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	private boolean waitUntilNotScrolling(ViewHolder h, Book book) {
		while (mScrolling) {
			if (h.book != book) {
				return false;
			}

			try {
				Thread.sleep(200);
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	private class ImageDownloader extends AsyncTask<Object, Object, Void> {

		@Override
		protected Void doInBackground(Object[] params) {
			View v = (View) params[0];
			ViewHolder h = (ViewHolder) v.getTag();
			Book book = h.book;

			if (v != null && !TextUtils.isEmpty(book.previewImageUrl)) {
				//if (!waitUntilNotScrolling(h, book)) return null;

				ImageView imgView = h.mPreviewImageView;

				Bitmap img = BookApi.getThumb(getContext(), book);

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

			if (!(v.getTag() instanceof ViewHolder) || (((ViewHolder) v.getTag()).book != null &&
					((ViewHolder) v.getTag()).book.bookId != ((Book) values[3]).bookId)) {
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

		public ImageView mPreviewImageView;
		public TextView mTitleTextView;
		public ImageButton mMenuButton;

		public ListPopupWindow mPopupWindow;

		public Book book;

		public ViewHolder(View itemView) {
			super(itemView);
			mPreviewImageView = (ImageView) itemView.findViewById(R.id.book_preview);
			mTitleTextView = (TextView) itemView.findViewById(R.id.book_title);
			mMenuButton = (ImageButton) itemView.findViewById(R.id.book_menu);

			mPopupWindow = new ListPopupWindow(getContext());
			mPopupWindow.setAdapter(new PopupAdapter(getContext(), new String[] {"Details", "Download", "Add to favorites"}));
			mPopupWindow.setAnchorView(mMenuButton);
			mPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
			mPopupWindow.setContentWidth(ListPopupWindow.WRAP_CONTENT);
			mMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mPopupWindow.show();
				}
			});

			itemView.setTag(this);
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
			public View getView(int position, View convertView, ViewGroup parent) {
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
					holder.menuItemView.setText(menuItems[position]);
				}
				return convertView;
			}
		}

	}

}
