package moe.feng.nhentai.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.florent37.materialviewpager.MaterialViewPager;

import moe.feng.nhentai.R;
import moe.feng.nhentai.ui.adapter.HomePagerAdapter;

public class MainActivity extends AppCompatActivity {

	private MaterialViewPager mPager;
	private HomePagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPager = (MaterialViewPager) findViewById(R.id.view_pager);
		Toolbar toolbar = mPager.getToolbar();
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}

		mPagerAdapter = new HomePagerAdapter(getApplicationContext(), getFragmentManager());
		mPager.getViewPager().setAdapter(mPagerAdapter);
		mPager.getPagerTitleStrip().setViewPager(mPager.getViewPager());
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
