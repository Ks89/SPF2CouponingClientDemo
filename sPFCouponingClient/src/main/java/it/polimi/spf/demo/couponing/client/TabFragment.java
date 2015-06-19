/*
Copyright 2015 Stefano Cappa, Politecnico di Milano

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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

import java.util.Locale;

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

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
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
                    return CouponManagerFragment.newInstance();
                case 1:
                    return CategoryFragment.newInstance();

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
