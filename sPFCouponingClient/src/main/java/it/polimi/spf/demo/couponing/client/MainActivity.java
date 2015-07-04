/*
 * Copyright 2014 Jacopo Aliprandi, Dario Archetti
 * Copyright 2015 Stefano Cappa
 *
 * This file is part of SPF.
 *
 * SPF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * SPF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SPF.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


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

public class MainActivity extends AppCompatActivity {

	protected static final String TAG = "MainActivity";

	@Getter
	@Setter
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.setupToolBar();

		TabFragment tabFragment = TabFragment.newInstance();

		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.container_root, tabFragment, "tabfragment")
				.commit();

		this.getSupportFragmentManager().executePendingTransactions();

		registerService();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_category_add:
				TabFragment.getCategoryFragment().onCategoryAdd();
				break;
		}
		return true;
	}

	/**
	 * Method to setup the {@link android.support.v7.widget.Toolbar}
	 * as supportActionBar in this {@link android.support.v7.app.AppCompatActivity}.
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
}
