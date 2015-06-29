package it.polimi.spf.demo.couponing.client.coupons;

import it.polimi.spf.demo.couponing.client.ClientApplication;
import it.polimi.spf.demo.couponing.client.Coupon;
import it.polimi.spf.demo.couponing.client.CouponDeliveryServiceImpl;
import it.polimi.spf.demo.couponing.client.CouponDeliveryServiceImpl.CouponListener;
import it.polimi.spf.demo.couponing.client.R;
import it.polimi.spf.demo.couponing.client.detail.CouponDetailActivity;
import it.polimi.spf.lib.LooperUtils;
import lombok.Getter;

import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;

public class CouponManagerFragment extends Fragment implements
		//OnClickInterface is the interface in the adapter to intercept item's short and long click events.
		CouponAdapter.OnClickInterface {

	private static final int LOADER_COUPON_ID = 0;
	private static final String TAG = "CouponManagerFragment";

	public static CouponManagerFragment newInstance() {
		return new CouponManagerFragment();
	}

	private RecyclerView mRecyclerView;

	@Getter
	private CouponAdapter mAdapter;

	private MultiSelector mMultiSelector = new MultiSelector();

	private LoaderManager.LoaderCallbacks<List<Coupon>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Coupon>>() {

		@Override
		public void onLoaderReset(Loader<List<Coupon>> arg0) {
			// Do nothing
		}

		@Override
		public void onLoadFinished(Loader<List<Coupon>> arg0, List<Coupon> coupons) {
			CouponList.getInstance().getCouponList().clear();
			CouponList.getInstance().getCouponList().addAll(coupons);
			mAdapter.notifyDataSetChanged();
//			mAdapter.clear();
//			if (coupons.size() == 0) {
//				mEmpty.setText(R.string.coupon_list_empty);
//			} else {
//				mAdapter.addAll(coupons);
//			}
		}

		@Override
		public Loader<List<Coupon>> onCreateLoader(int id, Bundle args) {
			return new AsyncTaskLoader<List<Coupon>>(getActivity()) {

				@Override
				public List<Coupon> loadInBackground() {
					return ClientApplication.get().getCouponDatabase().getAllCoupons();
				}
			};
		}
	};

	private CouponListener mCouponListener = LooperUtils.onMainThread(CouponListener.class, new CouponListener() {
		
		@Override
		public void onCouponReceived(Coupon coupon, Context context) {
			if(!isVisible()){
				return;
			}
			
			getLoaderManager().initLoader(LOADER_COUPON_ID, null, mLoaderCallbacks).forceLoad();
		}
	});


	private ModalMultiSelectorCallback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

		@Override
		public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
			super.onCreateActionMode(actionMode, menu);
			getActivity().getMenuInflater().inflate(R.menu.menu_coupon_detail, menu);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
			if (menuItem.getItemId()==  R.id.action_coupon_delete){
				// Need to finish the action mode before doing the following,
				// not after. No idea why, but it cr	ashes. (written by the creator of the Recyclerview Multiselect library)
				actionMode.finish();

				for (int i = CouponList.getInstance().getCouponList().size(); i >= 0; i--) {
					if (mMultiSelector.isSelected(i, 0)) {
						Coupon coupon = CouponList.getInstance().getCouponList().get(i);
						CouponList.getInstance().getCouponList().remove(coupon);
						ClientApplication.get().getCouponDatabase().deleteCoupon(coupon);
						mRecyclerView.getAdapter().notifyItemRemoved(i);
						getLoaderManager().initLoader(LOADER_COUPON_ID, null, mLoaderCallbacks).forceLoad();

					}
				}

				mMultiSelector.clearSelections();
				return true;

			}
			return false;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_coupon_recyclerview, container, false);

		mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(mLayoutManager);

		// allows for optimizations if all item views are of the same size:
		mRecyclerView.setHasFixedSize(true);

		mAdapter = new CouponAdapter(mMultiSelector, this);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		return root;
	}

	/**
	 * Note: since the fragment is retained. the bundle passed in after state is restored is null.
	 * THe only way to pass parcelable objects is through the activities onsavedInstanceState and appropiate startup lifecycle
	 * However after having second thoughts, since the fragment is retained then all the states and instance variables are
	 * retained as well. no need to make the selection states percelable therefore just check for the selectionstate
	 * from the multiselector
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		if (mMultiSelector != null) {
			Bundle bundle = savedInstanceState;
			if (bundle != null) {
				mMultiSelector.restoreSelectionStates(bundle.getBundle(TAG));
			}

			if (mMultiSelector.isSelectable()) {
				if (mDeleteMode != null) {
					mDeleteMode.setClearOnPrepare(false);
					((AppCompatActivity) getActivity()).startSupportActionMode(mDeleteMode);
				}

			}
		}

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().initLoader(LOADER_COUPON_ID, null, mLoaderCallbacks).forceLoad();
		CouponDeliveryServiceImpl.setCouponListener(mCouponListener);
	}

	@Override
	public void onPause() {
		super.onPause();
		CouponDeliveryServiceImpl.removeCouponListener(mCouponListener);
	}


	@Override
	public void longClickOnItem(CouponAdapter.ViewHolder viewHolder) {
		Log.d("CouponManagerFragment", "longClickOnItem coupon: " + viewHolder.getCoupon().toString());
		((AppCompatActivity) getActivity()).startSupportActionMode(mDeleteMode);
		mMultiSelector.setSelected(viewHolder, true);
	}

	@Override
	public void clickOnItem(CouponAdapter.ViewHolder viewHolder) {
		if (!mMultiSelector.tapSelection(viewHolder)) {
			Coupon c = viewHolder.getCoupon();
			Intent i = CouponDetailActivity.newIntent(getActivity(), c.getId());
			startActivity(i);
		}
	}
}
