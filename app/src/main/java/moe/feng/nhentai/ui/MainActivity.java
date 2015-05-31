package moe.feng.nhentai.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import moe.feng.nhentai.R;
import moe.feng.nhentai.ui.adapter.HomePagerAdapter;

public class MainActivity extends AppCompatActivity {

	private ViewPager mPager;
	private HomePagerAdapter mPagerAdapter;
	private TabLayout mTabLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(getResources().getColor(R.color.sexy_purple_800));
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mPager = (ViewPager) findViewById(R.id.viewpager);
		mTabLayout = (TabLayout) findViewById(R.id.tabs);

		mPagerAdapter = new HomePagerAdapter(getApplicationContext(), getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mTabLayout.setupWithViewPager(mPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

}
