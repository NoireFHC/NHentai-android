package moe.feng.nhentai.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import moe.feng.nhentai.R;
import moe.feng.nhentai.api.PageApi;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.adapter.BookListRecyclerAdapter;
import moe.feng.nhentai.ui.common.AbsActivity;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;
import moe.feng.nhentai.util.AsyncTask;

public class SearchResultActivity extends AbsActivity {

	private RecyclerView mRecyclerView;
	private BookListRecyclerAdapter mAdapter;
	private StaggeredGridLayoutManager mLayoutManager;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private ArrayList<Book> mBooks;

	private int mNowPage = 1;
	private String keyword;

	private static final String EXTRA_KEYWORD = "keyword";

	public static final String TAG = SearchResultActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		keyword = intent.getStringExtra(EXTRA_KEYWORD);

		setContentView(R.layout.activity_search_result);

		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle(String.format(getString(R.string.title_search_result), keyword));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mActionBar.setElevation(getResources().getDimension(R.dimen.appbar_elevation));
		}

		new PageGetTask().execute(mNowPage);
	}

	@Override
	protected void setUpViews() {
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

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
	}


	private void setRecyclerViewAdapter(BookListRecyclerAdapter adapter) {
		adapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder viewHolder) {
				BookListRecyclerAdapter.ViewHolder holder = (BookListRecyclerAdapter.ViewHolder) viewHolder;
				BookDetailsActivity.launch(SearchResultActivity.this, holder.mPreviewImageView, holder.book);
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
			return PageApi.getSearchPageList(keyword, params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<Book> newData) {
			mSwipeRefreshLayout.setRefreshing(false);
			if (newData != null) {
				if (!newData.isEmpty()) {
					mBooks.addAll(newData);
					mAdapter.notifyDataSetChanged();
				} else if (mNowPage == 1) {
					Snackbar.make(mRecyclerView, R.string.tips_no_result, Snackbar.LENGTH_LONG).show();
				}
			}
		}

	}

	public static void launch(AppCompatActivity activity, String keyword) {
		Intent intent = new Intent(activity, SearchResultActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_KEYWORD, keyword);
		activity.startActivity(intent);
	}

}
