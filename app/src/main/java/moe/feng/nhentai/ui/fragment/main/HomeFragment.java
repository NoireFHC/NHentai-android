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
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.BookDetailsActivity;
import moe.feng.nhentai.ui.adapter.BookListRecyclerAdapter;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;

public class HomeFragment extends Fragment {

	private RecyclerView mRecyclerView;
	private BookListRecyclerAdapter mAdapter;

	public static final String TAG = HomeFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.recylcer_view);
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		mRecyclerView.setHasFixedSize(false);

		/** 添加测试数据 */
		ArrayList<Book> books = new ArrayList<>();
		Book sampleBook = new Book("Test", "other", null, "0");
		sampleBook.bookImageThumbUrl.add("0");
		sampleBook.bookImageThumbUrl.add("1");
		sampleBook.bookImageThumbUrl.add("2");
		sampleBook.bookImageThumbUrl.add("0");
		sampleBook.bookImageThumbUrl.add("1");
		sampleBook.bookImageThumbUrl.add("1");
		books.add(sampleBook);
		books.add(new Book("Hello, world", "good", null, "1"));
		books.add(new Book("Are you ok?", "3Q very match", null, "2"));
		books.add(new Book("Well", "Test", null, "1"));
		books.add(new Book("1231241241", "Good", null, "2"));
		books.add(new Book("Hentai", "Yes", null, "0"));
		books.add(new Book("Loli is good!", "You are right.", null, "1"));

		mAdapter = new BookListRecyclerAdapter(books);
		setRecyclerViewAdapter(mAdapter);

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

}
