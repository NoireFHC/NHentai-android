package moe.feng.nhentai.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;

import moe.feng.nhentai.R;
import moe.feng.nhentai.model.Book;
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
		books.add(new Book("Test", "other", null));
		books.add(new Book("Hello, world", "good", null));
		books.add(new Book("Are you ok?", "3Q very match", null));
		books.add(new Book("Well", "Test", null));
		books.add(new Book("1231241241", "Good", null));
		books.add(new Book("Hentai", "Yes", null));
		books.add(new Book("Loli is good!", "You are right.", null));

		mAdapter = new BookListRecyclerAdapter(books);
		mAdapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder viewHolder) {
				if (viewHolder instanceof BookListRecyclerAdapter.ViewHolder) {
					BookListRecyclerAdapter.ViewHolder holder = (BookListRecyclerAdapter.ViewHolder) viewHolder;
					Log.i(TAG, "You clicked position no." + position + " item, " +
							"its name is " + holder.mTitleTextView.getText().toString());
				}
			}
		});
		setRecyclerViewAdapter(mAdapter);

		return view;
	}

	private void setRecyclerViewAdapter(BookListRecyclerAdapter adapter) {
		RecyclerViewMaterialAdapter materialAdapter = new RecyclerViewMaterialAdapter(adapter);
		mRecyclerView.setAdapter(materialAdapter);
		MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
	}

}
