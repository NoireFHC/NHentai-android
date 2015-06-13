package moe.feng.nhentai.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.quinny898.library.persistentsearch.SearchBox;

import moe.feng.nhentai.R;
import moe.feng.nhentai.ui.adapter.HomePagerAdapter;

public class MainActivity extends AppCompatActivity {

	private ViewPager mPager;
	private HomePagerAdapter mPagerAdapter;
	private TabLayout mTabLayout;
	private SearchBox mSearchBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(getResources().getColor(R.color.deep_purple_800));
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mPager = (ViewPager) findViewById(R.id.viewpager);
		mTabLayout = (TabLayout) findViewById(R.id.tabs);
		mSearchBox = (SearchBox) findViewById(R.id.search_box);

		mPagerAdapter = new HomePagerAdapter(getApplicationContext(), getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mTabLayout.setupWithViewPager(mPager);
		mSearchBox.setLogoText("");
		mSearchBox.setSearchListener(new SearchBox.SearchListener() {
			@Override
			public void onSearchOpened() {

			}

			@Override
			public void onSearchCleared() {

			}

			@Override
			public void onSearchClosed() {
				closeSearchBox();
			}

			@Override
			public void onSearchTermChanged() {

			}

			@Override
			public void onSearch(String result) {
				SearchResultActivity.launch(MainActivity.this, result);
			}
		});
	}

	private void openSearchBox() {
		mSearchBox.setVisibility(View.VISIBLE);

		mSearchBox.revealFromMenuItem(R.id.action_search, this);
	}

	private void closeSearchBox() {
		mSearchBox.hideCircularly(this);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mSearchBox.setVisibility(View.INVISIBLE);
					}
				});
			}
		}, 250);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_search) {
			openSearchBox();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mSearchBox.isSearchOpened()) {
			mSearchBox.toggleSearch();
		} else {
			super.onBackPressed();
		}
	}

}
