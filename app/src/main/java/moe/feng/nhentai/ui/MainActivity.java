package moe.feng.nhentai.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.quinny898.library.persistentsearch.SearchBox;

import moe.feng.nhentai.R;
import moe.feng.nhentai.dao.SearchHistoryManager;
import moe.feng.nhentai.ui.adapter.HomePagerAdapter;
import moe.feng.nhentai.ui.common.AbsActivity;

public class MainActivity extends AbsActivity implements NavigationView.OnNavigationItemSelectedListener {

	private ViewPager mPager;
	private HomePagerAdapter mPagerAdapter;
	private TabLayout mTabLayout;
	private SearchBox mSearchBox;
	private DrawerLayout mDrawerLayout;
	private NavigationView mNavigationView;
	private ActionBarDrawerToggle mDrawerToggle;

	private SearchHistoryManager mSearchHistoryManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(getResources().getColor(R.color.deep_purple_800));
		}

		setContentView(R.layout.activity_main);

		mActionBar.setDisplayHomeAsUpEnabled(true);

		mSearchHistoryManager = SearchHistoryManager.getInstance(getApplicationContext(), "all");
	}

	@Override
	protected void setUpViews() {
		mPager = $(R.id.viewpager);
		mTabLayout = $(R.id.tabs);
		mSearchBox = $(R.id.search_box);

		mDrawerLayout = $(R.id.drawer_layout);
		mNavigationView = $(R.id.navigation_view);
		mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerOpened(View drawerView) {
				mDrawerToggle.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				mDrawerToggle.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				mDrawerToggle.onDrawerStateChanged(newState);
			}
		});
		mNavigationView.setNavigationItemSelectedListener(this);

		mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout,
				R.string.abc_action_bar_home_description,
				R.string.abc_action_bar_home_description
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);

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
				mSearchHistoryManager.add(result);
				SearchResultActivity.launch(MainActivity.this, result);
			}
		});
	}

	private void openSearchBox() {
		mSearchBox.setVisibility(View.VISIBLE);
		mSearchBox.setSearchables(mSearchHistoryManager.getSearchResults());
		mSearchBox.setSearchString("");

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
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		int id = item.getItemId();
		if (id == R.id.action_search) {
			openSearchBox();
			return true;
		}
		if (id == R.id.action_settings) {
			SettingsActivity.launchActivity(this, SettingsActivity.FLAG_MAIN);
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

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		mDrawerLayout.closeDrawer(mNavigationView);
		switch (menuItem.getItemId()) {
			// TODO Update page
			case R.id.navigation_item_home:
				menuItem.setChecked(true);
				return true;
			case R.id.navigation_item_tag:
				return true;
			case R.id.navigation_item_character:
				return true;
		}
		return false;
	}
}
