package moe.feng.nhentai.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import moe.feng.nhentai.ui.fragment.DownloadManagerFragment;
import moe.feng.nhentai.ui.fragment.FavoriteFragment;
import moe.feng.nhentai.ui.fragment.HomeFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

	private HomeFragment homeFragment;
	private DownloadManagerFragment downloadManagerFragment;
	private FavoriteFragment favoriteFragment;

	public HomePagerAdapter(FragmentManager fm) {
		super(fm);
		homeFragment = new HomeFragment();
		downloadManagerFragment = new DownloadManagerFragment();
		favoriteFragment = new FavoriteFragment();
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
			case 0:
				return homeFragment;
			case 1:
				return downloadManagerFragment;
			case 2:
				return favoriteFragment;
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

}
