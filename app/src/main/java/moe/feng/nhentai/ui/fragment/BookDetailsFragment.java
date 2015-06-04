package moe.feng.nhentai.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import moe.feng.nhentai.R;
import moe.feng.nhentai.model.Book;
import moe.feng.nhentai.ui.GalleryActivity;
import moe.feng.nhentai.ui.adapter.BookThumbHorizontalRecyclerAdapter;
import moe.feng.nhentai.ui.common.AbsRecyclerViewAdapter;

public class BookDetailsFragment extends Fragment {

	private Context mContext;

	private Book book;

	private TextView mOtherText, mArticleText;
	private AppCompatRatingBar mRatingBar;
	private RecyclerView mRecyclerView;

	private static final String ARG_BOOK_DATA = "book_data";

	public static BookDetailsFragment newInstance(Book book) {
		BookDetailsFragment fragment = new BookDetailsFragment();
		Bundle data = new Bundle();
		data.putString(ARG_BOOK_DATA, book.toJSONString());
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {
		View view = inflater.inflate(R.layout.fragment_book_details, group, false);

		mContext = getActivity().getApplicationContext();

		book = new Gson().fromJson(getArguments().getString(ARG_BOOK_DATA), Book.class);

		mOtherText = (TextView) view.findViewById(R.id.tv_other);
		mArticleText = (TextView) view.findViewById(R.id.tv_article);
		mRatingBar = (AppCompatRatingBar) view.findViewById(R.id.rating_bar);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.book_thumb_list);

		mOtherText.setText(book.other);

		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.HORIZONTAL, false));

		BookThumbHorizontalRecyclerAdapter adapter = new BookThumbHorizontalRecyclerAdapter(book);
		adapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder) {
				GalleryActivity.launch(getActivity(), book, position);
			}
		});
		mRecyclerView.setAdapter(adapter);


		return view;
	}

}
