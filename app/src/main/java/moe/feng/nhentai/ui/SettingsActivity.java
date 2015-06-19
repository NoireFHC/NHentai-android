package moe.feng.nhentai.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;

import moe.feng.nhentai.R;
import moe.feng.nhentai.ui.common.AbsActivity;
import moe.feng.nhentai.ui.fragment.settings.SettingsLicense;
import moe.feng.nhentai.ui.fragment.settings.SettingsMain;

public class SettingsActivity extends AbsActivity {

	private Fragment mFragment;
	private int flag;

	public static final String EXTRA_FLAG = "flag";
	public static final int FLAG_MAIN = 0, FLAG_LICENSE = 1, FLAG_GUI = 2, FLAG_NETWORK = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		flag = intent.getIntExtra(EXTRA_FLAG, FLAG_MAIN);

		setContentView(R.layout.activity_settings);

		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void setUpViews() {
		ViewCompat.setElevation(mToolbar, getResources().getDimension(R.dimen.appbar_elevation));

		switch (flag) {
			case FLAG_MAIN:
				mFragment = new SettingsMain();
				break;
			case FLAG_LICENSE:
				mFragment = new SettingsLicense();
				break;
		}
		getFragmentManager().beginTransaction()
				.replace(R.id.container, mFragment)
				.commit();
	}


	public static void launchActivity(Activity mActivity, int flag) {
		Intent intent = new Intent(mActivity, SettingsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		intent.putExtra(EXTRA_FLAG, flag);
		mActivity.startActivity(intent);
	}

}