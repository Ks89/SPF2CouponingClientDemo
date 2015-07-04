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
        return new CouponDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon_detail, container, false);

        mPhotoView = (ImageView) view.findViewById(R.id.coupon_photo);
        mTitleView = (TextView) view.findViewById(R.id.coupon_title);
        mTextView = (TextView) view.findViewById(R.id.coupon_text);
        mCategoryView = (TextView) view.findViewById(R.id.coupon_category);

        return view;
    }

}
