package it.polimi.spf.demo.couponing.client.detail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.polimi.spf.demo.couponing.client.Coupon;
import it.polimi.spf.demo.couponing.client.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponDetailFragment extends Fragment {

    private ImageView mPhotoView;
    private TextView mTitleView, mTextView, mCategoryView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CouponCreateFragment.
     */
    public static CouponDetailFragment newInstance() {
        CouponDetailFragment fragment = new CouponDetailFragment();
        return fragment;
    }

    public CouponDetailFragment() {
        // Required empty public constructor
    }

    public void setPhotoAndCoupon(Bitmap photo, Coupon coupon) {

        mPhotoView.setImageBitmap(photo);
        mTitleView.setText(coupon.getTitle());
        mTextView.setText(coupon.getText());
        mCategoryView.setText(coupon.getCategory());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon_detail, container, false);

        mPhotoView = (ImageView) view.findViewById(R.id.coupon_photo);
        mTitleView = (TextView) view.findViewById(R.id.coupon_title);
        mTextView = (TextView) view.findViewById(R.id.coupon_text);
        mCategoryView = (TextView) view.findViewById(R.id.coupon_category);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
}
