package com.syn.viewpaggerindicator;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private ViewpagerIndicator mIndicator;
    private ViewPager mViewpager;
    private List<String> mTitle= Arrays.asList("短信","收藏","推荐","短信","收藏","推荐","短信","收藏","推荐");
    private List<VpsimpleFragment> mContents=new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        initViews();
        initDatas();
        mIndicator.setVisibleTabCount(4);
        mIndicator.setTabItemTitles(mTitle);
        mViewpager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewpager, 0);
        mIndicator.setOnPageChangeListener(new ViewpagerIndicator.PagerOnchangeListner() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initDatas() {
        for(String title:mTitle){
           VpsimpleFragment fragment= VpsimpleFragment.newInstance(title);
            mContents.add(fragment);
        }
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return null;

            }

            @Override
            public int getCount() {
                return mContents.size();
            }
        };
    }

    private void initViews() {
        mViewpager=(ViewPager)findViewById(R.id.id_viewpagger);
        mIndicator=(ViewpagerIndicator)findViewById(R.id.id_indicator);
    }
}
