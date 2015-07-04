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


package it.polimi.spf.demo.couponing.client.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import it.polimi.spf.demo.couponing.client.ClientApplication;
import it.polimi.spf.demo.couponing.client.Coupon;
import it.polimi.spf.demo.couponing.client.R;
import it.polimi.spf.lib.notification.SPFNotification;
import it.polimi.spf.shared.model.SPFError;
import lombok.Getter;
import lombok.Setter;

public class CouponDetailActivity extends AppCompatActivity {

	private static final String EXTRA_COUPON_ID = "couponId";
	private static final int COUPON_LOADER_ID = 0;
	private static final String TAG = "CouponDetailActivity";

	public static Intent newIntent(Context context, long couponId) {
		Intent i = new Intent(context, CouponDetailActivity.class);
		i.putExtra(EXTRA_COUPON_ID, couponId);
		return i;
	}

	private long mCouponId;
	private Coupon mCoupon;
	
	private SPFNotification mNotificationService;


	private CouponDetailFragment couponDetailFragment;

	@Getter
	@Setter
	private Toolbar toolbar;

	private final LoaderManager.LoaderCallbacks<Coupon> mCouponLoaderCallbacks = new LoaderManager.LoaderCallbacks<Coupon>() {
		
		@Override
		public void onLoaderReset(Loader<Coupon> arg0) {
			// Do nothing
		}
		
		@Override
		public void onLoadFinished(Loader<Coupon> arg0, Coupon coupon) {
			Log.d(TAG, "Loaded coupon: " + coupon);
			mCoupon = coupon;
			Bitmap photo = BitmapFactory.decodeByteArray(coupon.getPhoto(), 0, coupon.getPhoto().length);

			couponDetailFragment.setPhotoAndCoupon(photo, mCoupon);

		}
		
		@Override
		public Loader<Coupon> onCreateLoader(int id, Bundle args) {
			return new AsyncTaskLoader<Coupon>(CouponDetailActivity.this) {

				@Override
				public Coupon loadInBackground() {
					return ClientApplication.get().getCouponDatabase().getCouponById(mCouponId);
				}
				
			};
		}
	};
	
	private final SPFNotification.Callback mNotificationCallback = new SPFNotification.Callback() {
		
		@Override
		public void onServiceReady(SPFNotification componentInstance) {
			mNotificationService = componentInstance;
			Log.d(TAG, "SPFNotification.Callback: onServiceReady");
		}
		
		@Override
		public void onError(SPFError err) {
			Log.e(TAG, "Error in notification service: " + err);
			mNotificationService = null;
			Log.e(TAG, "SPFNotification.Callback: onError " + err);
		}
		
		@Override
		public void onDisconnect() {
			mNotificationService = null;
			Log.d(TAG, "SPFNotification.Callback: onDisconnect");
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		this.setupToolBar();

		Bundle source = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		if(source == null || !source.containsKey(EXTRA_COUPON_ID)){
			throw new IllegalStateException("Missing coupon ID");
		}

		mCouponId = source.getLong(EXTRA_COUPON_ID);

		this.couponDetailFragment = CouponDetailFragment.newInstance();

		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.container_detail_root, this.couponDetailFragment , "couponCreationFragment")
				.commit();

		this.getSupportFragmentManager().executePendingTransactions();

		getSupportLoaderManager().initLoader(COUPON_LOADER_ID, null, mCouponLoaderCallbacks).forceLoad();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SPFNotification.load(this, mNotificationCallback);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mNotificationService != null){
			mNotificationService.disconnect();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_coupon_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_coupon_delete:
			onDeleteRequest();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(EXTRA_COUPON_ID, mCouponId);
	}

	private void onDeleteRequest(){
		new AlertDialog.Builder(this)
			.setTitle(R.string.coupon_deletion_confirm_title)
			.setMessage(R.string.coupon_deletion_confirm_message)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					onDelete();
				}
			}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
	}

	private void onDelete(){
		if(mNotificationService == null){
			toast(R.string.error_notification_service_unavailable);
			return;
		}
		
		if(!mNotificationService.deleteTrigger(mCoupon.getId())){
			toast(R.string.error_trigger_not_deleted);
		}
		
		ClientApplication.get().getCouponDatabase().deleteCoupon(mCoupon);
		finish();
	}
	
	private void toast(int resId){
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
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
			toolbar.inflateMenu(R.menu.menu_coupon_detail);
			this.setSupportActionBar(toolbar);
		}
	}
}
