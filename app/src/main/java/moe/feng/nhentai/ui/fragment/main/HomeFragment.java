package moe.feng.nhentai.ui.fragment.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import moe.feng.nhentai.R;
import moe.feng.nhentai.api.PageApi;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.BookDetailsActivity;
import moe.feng.nhentai.ui.adapter.BookListRecyclerAdapter;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;
import moe.feng.nhentai.util.AsyncTask;

public class HomeFragment extends Fragment {

	private RecyclerView mRecyclerView;
	private BookListRecyclerAdapter mAdapter;

	private ArrayList<Book> mBooks;

	public static final String TAG = HomeFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.recylcer_view);
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		mRecyclerView.setHasFixedSize(false);

		mBooks = new ArrayList<>();
		mAdapter = new BookListRecyclerAdapter(mRecyclerView, mBooks);
		setRecyclerViewAdapter(mAdapter);

		new PageGetTask().execute(1);

		return view;
	}

	private void setRecyclerViewAdapter(BookListRecyclerAdapter adapter) {
		adapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder viewHolder) {
				BookListRecyclerAdapter.ViewHolder holder = (BookListRecyclerAdapter.ViewHolder) viewHolder;
				Log.i(TAG, "You clicked position no." + position + " item, " +
						"its name is " + holder.mTitleTextView.getText().toString());
				BookDetailsActivity.launch(getActivity(), holder.mPreviewImageView, holder.book);
			}
		});
		mRecyclerView.setAdapter(adapter);
	}

	private class PageGetTask extends AsyncTask<Integer, Void, ArrayList<Book>> {

		@Override
		protected ArrayList<Book> doInBackground(Integer... params) {
			return PageApi.getHomePageList(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<Book> newData) {
			if (newData != null) {
				if (!newData.isEmpty()) {
					mBooks.addAll(newData);
					mAdapter.notifyDataSetChanged();
				} else {

				}
			}
		}

	}

}
