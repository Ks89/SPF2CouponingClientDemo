package it.polimi.spf.demo.couponing.client;

import it.polimi.spf.lib.services.SPFServiceRegistry;
import it.polimi.spf.shared.model.SPFError;
import lombok.Getter;
import lombok.Setter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements TabFragment.UpdateToolBarListener {

	protected static final String TAG = "MainActivity";
	private TabFragment tabFragment;
	@Getter
	@Setter
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.setupToolBar();

		tabFragment = TabFragment.newInstance();

		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.container_root, tabFragment, "tabfragment")
				.commit();

		this.getSupportFragmentManager().executePendingTransactions();

		//register service
		registerService();

//		ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
//		pager.setAdapter(new PagerConfigurator(this, getSupportFragmentManager()));
//
//
//		PagerTabStrip tabs = (PagerTabStrip) findViewById(R.id.main_tabs);
//		tabs.setTabIndicatorColorResource(R.color.selection);
	}

	private void registerService(){
		SPFServiceRegistry.load(this, new SPFServiceRegistry.Callback() {
			@Override
			public void onServiceReady(SPFServiceRegistry localServiceRegistry) {
				localServiceRegistry.registerService(CouponDeliveryService.class, CouponDeliveryServiceImpl.class);
				localServiceRegistry.disconnect();
			}

			@Override
			public void onError(SPFError spfError) {
				Log.e(TAG, "Could not register service: " + spfError);
			}

			@Override
			public void onDisconnect() {
			}
		});
	}

//
//	private static class PagerConfigurator extends FragmentPagerAdapter {
//
//		private final static int PAGE_COUNT = 2;
//
//		private final String[] mPageTitles;
//
//		private PagerConfigurator(Context c, FragmentManager fm) {
//			super(fm);
//			this.mPageTitles = c.getResources().getStringArray(R.array.main_tabs_titles);
//		}
//
//		@Override
//		public Fragment getItem(int i) {
//			switch (i) {
//				case 0:
//					return CouponManagerFragment.newInstance();
//				case 1:
//					return CategoryFragment.newInstance();
//
//				default:
//					throw new IndexOutOfBoundsException("Requested page " + i + ", total " + PAGE_COUNT);
//			}
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return mPageTitles[position];
//		}
//
//		@Override
//		public int getCount() {
//			return PAGE_COUNT;
//		}
//	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_category_add:
//				TabFragment.getCouponManagerFragment().onCategoryAdd();
				TabFragment.getCategoryFragment().onCategoryAdd();
				break;
		}
		return true;
	}

	/**
	 * Method to setup the {@link android.support.v7.widget.Toolbar}
	 * as supportActionBar in this {@link android.support.v7.app.ActionBarActivity}.
	 */
	private void setupToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			toolbar.setTitle(getResources().getString(R.string.app_name));
			toolbar.setTitleTextColor(Color.WHITE);
			toolbar.inflateMenu(R.menu.menu_category);
			this.setSupportActionBar(toolbar);
		}
	}

	@Override
	public void updateToolbar(int tabPage) {
//		setupToolBar();
//		switch(tabPage) {
//			case 0:
//				toolbar.inflateMenu(R.menu.menu_category);
//				break;
//			case 1:
//				toolbar.inflateMenu(R.menu.menu_category_action_mode);
//				break;
//			default:
//				toolbar.inflateMenu(R.menu.menu_category);
//				break;
//		}
	}
}
