package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.fragment.FragmentListOfDoctors;
import com.unisonpharmaceuticals.fragment.FragmentSummaryOfDoctor;
import com.unisonpharmaceuticals.fragment.FragmentSummaryOfSample;
import com.unisonpharmaceuticals.fragment.FragmentWebsite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private int[] tabsIcon = {R.drawable.tab_entry_selector, R.drawable.tab_product_selector,R.drawable.tab_dr_selector,R.drawable.tab_logo_selector};
    private String[] tabsTitle = {"Doctor's Summary", "Sample's Report","Doctor List","Website"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ButterKnife.bind(this);
        activity = this;
        sessionManager = new SessionManager(activity);
        basicProcesses();
    }


    @Override
    public void initViews() {
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activity.finish();
                finishActivityAnimation(activity);
            }
        });
        tvTitle.setText("DOCTOR'S SUMMARY");
        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setupTabIcons();
    }

    @Override
    public void viewClick() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                if(position==0)
                {
                    tvTitle.setText("DOCTOR'S SUMMARY");
                }
                else if(position==1)
                {
                    tvTitle.setText("SAMPLE'S SUMMARY");
                }
                else if(position==2)
                {
                    tvTitle.setText("DOCTOR'S LIST");
                }
                else if(position==3)
                {
                    tvTitle.setText("UNISON WEBSITE");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void getDataFromServer() {

    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentSummaryOfDoctor());
        adapter.addFragment(new FragmentSummaryOfSample());
        adapter.addFragment(new FragmentListOfDoctors());
        adapter.addFragment(new FragmentWebsite());
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
        private final List<String> mFragmentTitleList = new ArrayList<String>();

        public ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment)
        {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return null;
        }
    }

    private void setupTabIcons()
    {
        try
        {
            final View tabOne = LayoutInflater.from(activity).inflate(R.layout.rowview_main_tablayout, null);
            ImageView ivTabOne = (ImageView) tabOne.findViewById(R.id.ivTab);
            ivTabOne.setImageResource(tabsIcon[0]);
            TextView txtTabOne = (TextView) tabOne.findViewById(R.id.txtTab);
            txtTabOne.setText(tabsTitle[0]);
            tabLayout.getTabAt(0).setCustomView(tabOne);


            View tabTwo = LayoutInflater.from(activity).inflate(R.layout.rowview_main_tablayout, null);
            ImageView ivTabTwo = (ImageView) tabTwo.findViewById(R.id.ivTab);
            ivTabTwo.setImageResource(tabsIcon[1]);
            TextView txtTabTwo = (TextView) tabTwo.findViewById(R.id.txtTab);
            txtTabTwo.setText(tabsTitle[1]);
            tabLayout.getTabAt(1).setCustomView(tabTwo);

            View tabThree = LayoutInflater.from(activity).inflate(R.layout.rowview_main_tablayout, null);
            ImageView ivtabThree = (ImageView) tabThree.findViewById(R.id.ivTab);
            ivtabThree.setImageResource(tabsIcon[2]);
            TextView txttabThree = (TextView) tabThree.findViewById(R.id.txtTab);
            txttabThree.setText(tabsTitle[2]);
            tabLayout.getTabAt(2).setCustomView(tabThree);

            View tabFour = LayoutInflater.from(activity).inflate(R.layout.rowview_main_tablayout, null);
            ImageView ivtabFour = (ImageView) tabFour.findViewById(R.id.ivTab);
            ivtabFour.setImageResource(tabsIcon[3]);
            TextView txttabFour = (TextView) tabFour.findViewById(R.id.txtTab);
            txttabFour.setText(tabsTitle[3]);
            tabLayout.getTabAt(3).setCustomView(tabFour);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            finishActivityAnimation(activity);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy()
    {
        DashboardActivity.isAppRunning = false;
        super.onDestroy();
    }
}
