package moe.feng.nhentai.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.feng.nhentai.R;

public class DownloadManagerFragment extends Fragment {

	private RecyclerView mRecyclerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.recylcer_view);

		return view;
	}

}
