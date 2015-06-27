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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class CouponManagerFragment extends Fragment implements
//		//ItemClickListener is the interface in the adapter to intercept item's click events.
//		//I use this to call itemClicked(v) in this class from CouponAdapter.
		CouponAdapter.ItemClickListener {

	private static final int LOADER_COUPON_ID = 0;

	public static CouponManagerFragment newInstance() {
		return new CouponManagerFragment();
	}

	private TextView mEmpty;
	private ActionMode mActionMode;
	private RecyclerView mRecyclerView;

	@Getter
	private CouponAdapter mAdapter;

	private final OnItemClickListener mCouponClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
			Coupon c = (Coupon) parent.getItemAtPosition(position);
			Intent i = CouponDetailActivity.newIntent(getActivity(), c.getId());
			startActivity(i);
		}
	};

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
	
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		if(mActionMode != null && !isVisibleToUser){
			mActionMode.finish();
		}
	};
	
//	private AbsListView.MultiChoiceModeListener mChoiceListener = new AbsListView.MultiChoiceModeListener() {
//
//        private Set<Integer> mSelectedPositions;
//
//        @Override
//        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//            if(checked){
//                mSelectedPositions.add(position);
//            } else {
//                mSelectedPositions.remove(position);
//            }
//        }
//
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.menu_coupon_detail, menu);
//            mActionMode = mode;
//            mSelectedPositions = new HashSet<>();
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            if(item.getItemId() == R.id.action_coupon_delete){
//                for(int i : mSelectedPositions){
//                    Coupon c = (Coupon) mCouponList.getItemAtPosition(i);
//                    ClientApplication.get().getCouponDatabase().deleteCoupon(c);
//                    getLoaderManager().initLoader(LOADER_COUPON_ID, null, mLoaderCallbacks).forceLoad();
//                }
//                mActionMode.finish();
//            }
//
//            return false;
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            mSelectedPositions = null;
//            mActionMode = null;
//        }
//    };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_coupon_recyclerview, container, false);

		mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(mLayoutManager);

		// allows for optimizations if all item views are of the same size:
		mRecyclerView.setHasFixedSize(true);

		mAdapter = new CouponAdapter(this);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		return root;



//		View root = inflater.inflate(R.layout.fragment_coupon_list, container, false);
//		mCouponList = (ListView) root.findViewById(R.id.coupon_list);
//		mEmpty = (TextView) root.findViewById(R.id.coupon_list_empty);
//		mCouponList.setEmptyView(mEmpty);
//		mCouponList.setOnItemClickListener(mCouponClickListener );
//
//		mCouponList.setMultiChoiceModeListener(mChoiceListener);
//		mCouponList.setChoiceMode(ListViewCompat.CHOICE_MODE_MULTIPLE_MODAL);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
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

	/**
	 * Method called by {@link it.polimi.spf.demo.couponing.client.coupons.CouponAdapter}
	 * with the {@link it.polimi.spf.demo.couponing.client.coupons.CouponAdapter.ItemClickListener}
	 * interface, when the user click on an element of the {@link android.support.v7.widget.RecyclerView}.
	 * @param view The clicked view.
	 */
	@Override
	public void itemClicked(View view) {
		int clickedPosition = mRecyclerView.getChildLayoutPosition(view);

		if(clickedPosition>=0) { //a little check :)
			Coupon c = CouponList.getInstance().getCouponList().get(clickedPosition);
			Intent i = CouponDetailActivity.newIntent(getActivity(), c.getId());
			startActivity(i);
		}
	}
}
