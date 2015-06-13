package moe.feng.nhentai.ui.fragment.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
	private StaggeredGridLayoutManager mLayoutManager;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private ArrayList<Book> mBooks;

	private int mNowPage = 1;

	public static final String TAG = HomeFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setHasFixedSize(true);

		mBooks = new ArrayList<>();
		mAdapter = new BookListRecyclerAdapter(mRecyclerView, mBooks);
		setRecyclerViewAdapter(mAdapter);

		mSwipeRefreshLayout.setColorSchemeResources(
				R.color.deep_purple_500, R.color.pink_500, R.color.orange_500, R.color.brown_500,
				R.color.indigo_500, R.color.blue_500, R.color.teal_500, R.color.green_500
		);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (!mSwipeRefreshLayout.isRefreshing()) {
					mSwipeRefreshLayout.setRefreshing(true);
				}

				mBooks = new ArrayList<>();
				mAdapter = new BookListRecyclerAdapter(mRecyclerView, mBooks);
				setRecyclerViewAdapter(mAdapter);
				new PageGetTask().execute(mNowPage = 1);
			}
		});

		new PageGetTask().execute(mNowPage);

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
		adapter.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView rv, int dx, int dy) {
				if (!mSwipeRefreshLayout.isRefreshing() && mLayoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >= mAdapter.getItemCount() - 2) {
					mSwipeRefreshLayout.setRefreshing(true);
					new PageGetTask().execute(++mNowPage);
				}
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
			mSwipeRefreshLayout.setRefreshing(false);
			if (newData != null) {
				if (!newData.isEmpty()) {
					mBooks.addAll(newData);
					mAdapter.notifyDataSetChanged();
				} else if (mNowPage == 1) {
					Snackbar.make(mRecyclerView, R.string.tips_network_error, Snackbar.LENGTH_LONG).show();
				}
			}
		}

	}

}
