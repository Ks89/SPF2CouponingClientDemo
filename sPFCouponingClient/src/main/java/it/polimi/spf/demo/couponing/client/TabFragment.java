/*
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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import it.polimi.spf.demo.couponing.client.coupons.CouponManagerFragment;
import lombok.Getter;


/**
 * Class that represents a Fragment with other Fragments as Tabs.
 * <p></p>
 * Created by Stefano Cappa on 05/02/15.
 */
public class TabFragment extends Fragment {

    @Getter
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @Getter
    private ViewPager mViewPager;

    @Getter private static CouponManagerFragment couponManagerFragment;
    @Getter private static CategoryFragment categoryFragment;

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        couponManagerFragment = CouponManagerFragment.newInstance();
        categoryFragment = CategoryFragment.newInstance();
        return fragment;
    }

    /**
     * Default Fragment constructor.
     */
    public TabFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tab, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getFragmentManager());

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);


        // When swiping between different sections, select the corresponding
        // tab.
        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mSectionsPagerAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }


    /**
     * Class that represents the FragmentPagerAdapter of this Fragment, that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private final static int PAGE_COUNT = 2;
        private final String[] mPageTitles;

        public SectionsPagerAdapter(Context c, FragmentManager fm) {
            super(fm);
            this.mPageTitles = c.getResources().getStringArray(R.array.main_tabs_titles);
        }


        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return couponManagerFragment;
                case 1:
                    return categoryFragment;

                default:
                    throw new IndexOutOfBoundsException("Requested page " + i + ", total " + PAGE_COUNT);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPageTitles[position];
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
