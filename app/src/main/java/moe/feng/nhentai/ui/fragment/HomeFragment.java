package moe.feng.nhentai.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;

import moe.feng.nhentai.R;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.adapter.BookListRecyclerAdapter;

public class HomeFragment extends Fragment {

	private RecyclerView mRecyclerView;
	private BookListRecyclerAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.recylcer_view);

		/** 添加测试数据 */
		ArrayList<Book> books = new ArrayList<>();
		books.add(new Book("Test", "other", null));
		books.add(new Book("Hello, world", "good", null));
		books.add(new Book("Are you ok?", "3Q very match", null));

		mAdapter = new BookListRecyclerAdapter(books);
		setRecyclerViewAdapter(mAdapter);

		return view;
	}

	private void setRecyclerViewAdapter(BookListRecyclerAdapter adapter) {
		RecyclerViewMaterialAdapter materialAdapter = new RecyclerViewMaterialAdapter(adapter);
		mRecyclerView.setAdapter(materialAdapter);
		MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
	}

}
