package moe.feng.nhentai.ui.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AbsRecyclerViewAdapter extends RecyclerView.Adapter<AbsRecyclerViewAdapter.ClickableViewHolder> {

	private Context context;

	public AbsRecyclerViewAdapter() {
	}

	public interface OnItemClickListener {
		public void onItemClicked(int position);
	}

	public interface OnItemLongClickListener {
		public boolean onItemLongClicked(int position);
	}

	private OnItemClickListener itemClickListener;
	private OnItemLongClickListener itemLongClickListener;

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.itemClickListener = listener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		this.itemLongClickListener = listener;
	}

	public void bindContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return this.context;
	}

	@Override
	public void onBindViewHolder(ClickableViewHolder holder, final int position) {
		holder.getParentView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (itemClickListener != null) {
					itemClickListener.onItemClicked(position);
				}
			}
		});
		holder.getParentView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (itemLongClickListener != null) {
					return itemLongClickListener.onItemLongClicked(position);
				} else {
					return false;
				}
			}
		});
	}

	public class ClickableViewHolder extends RecyclerView.ViewHolder {

		private View parentView;

		public ClickableViewHolder(View itemView) {
			super(itemView);
			this.parentView = itemView;
		}

		public View getParentView() {
			return parentView;
		}

	}

}