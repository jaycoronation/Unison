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
import com.unisonpharmaceuticals.fragment.FragmentLeaveApplication;
import com.unisonpharmaceuticals.fragment.FragmentLeaveApproval;
import com.unisonpharmaceuticals.fragment.FragmentTourPlanEntry;
import com.unisonpharmaceuticals.fragment.FragmentTourPlanReport;
import com.unisonpharmaceuticals.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TourPlanActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private int[] tabsIcon = {R.drawable.tab_tp_entry_selector, R.drawable.tab_tp_report_selector, R.drawable.tab_leave_selector,R.drawable.tab_leave_selector};
    private String[] tabsTitle = {"T.P Entry", "T.P Report", "Leave","Leave Approval"};

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
            public void onClick(View v) {
                if(FragmentTourPlanEntry.llTopSection!=null)
                {
                    if(FragmentTourPlanEntry.llTopSection.getVisibility()==View.GONE)
                    {
                        FragmentTourPlanEntry.llTopSection.setVisibility(View.VISIBLE);
                        if(FragmentTourPlanEntry.scrollView!=null)
                        {
                            FragmentTourPlanEntry.scrollView.scrollTo(0,tvTitle.getTop());
                        }
                        return;
                    }
                    else
                    {
                        finish();
                        finishActivityAnimation(activity);
                    }
                }
                else
                {
                    finish();
                    finishActivityAnimation(activity);
                }
                /*activity.finish();
                finishActivityAnimation(activity);*/
            }
        });
        tvTitle.setTextColor(getResources().getColor(R.color.light_primary));
        tvTitle.setText("T.P Entry");
        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
        if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR))
        {
            try
            {
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                viewpager.setOffscreenPageLimit(4);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                viewpager.setOffscreenPageLimit(3);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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
                    tvTitle.setText("T.P Entry");
                }
                else if(position==1)
                {
                    tvTitle.setText("T.P Report");
                }
                else if(position==2)
                {
                    tvTitle.setText("LEAVE APPLICATION");
                }
                else if(position==3)
                {
                    tvTitle.setText("LEAVE APPROVAL");
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
        adapter.addFragment(new FragmentTourPlanEntry());
        //adapter.addFragment(new FragmentCalendar());
        adapter.addFragment(new FragmentTourPlanReport());
        adapter.addFragment(new FragmentLeaveApplication());
        
        if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER) || sessionManager.getUSerType().equalsIgnoreCase(ApiClient.ADMIN))
        {
            adapter.addFragment(new FragmentLeaveApproval());
        }
        
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
            ImageView ivTabThree = (ImageView) tabThree.findViewById(R.id.ivTab);
            ivTabThree.setImageResource(tabsIcon[2]);
            TextView txtTabThree = (TextView) tabThree.findViewById(R.id.txtTab);
            txtTabThree.setText(tabsTitle[2]);
            tabLayout.getTabAt(2).setCustomView(tabThree);
            
            if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MANAGER) || sessionManager.getUSerType().equalsIgnoreCase(ApiClient.ADMIN))
            {
                View tabFour = LayoutInflater.from(activity).inflate(R.layout.rowview_main_tablayout, null);
                ImageView ivTabFour = (ImageView) tabFour.findViewById(R.id.ivTab);
                ivTabFour.setImageResource(tabsIcon[3]);
                TextView txtTabFour = (TextView) tabFour.findViewById(R.id.txtTab);
                txtTabFour.setText(tabsTitle[3]);
                tabLayout.getTabAt(3).setCustomView(tabFour);
            }

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
            if(FragmentTourPlanEntry.llTopSection!=null)
            {
                if(FragmentTourPlanEntry.llTopSection.getVisibility()==View.GONE)
                {
                    FragmentTourPlanEntry.llTopSection.setVisibility(View.VISIBLE);
                    if(FragmentTourPlanEntry.scrollView!=null)
                    {
                        FragmentTourPlanEntry.scrollView.scrollTo(0,tvTitle.getTop());
                    }
                    return true;
                }
                else
                {
                    finish();
                    finishActivityAnimation(activity);
                }
            }
            else
            {
                finish();
                finishActivityAnimation(activity);
            }
           /*
            finish();
            finishActivityAnimation(activity);*/
        }
        return super.onKeyDown(keyCode, event);
    }

   /* @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            if(FragmentTourPlanEntry.llTopSection!=null)
            {
                if(FragmentTourPlanEntry.llTopSection.getVisibility()==View.GONE)
                {
                    FragmentTourPlanEntry.llTopSection.setVisibility(View.VISIBLE);
                    return;
                }
                else
                {
                    finish();
                    finishActivityAnimation(activity);
                }
            }
            else
            {
                finish();
                finishActivityAnimation(activity);
            }
        } else {
            getFragmentManager().popBackStack();
        }

    }*/

    @Override
    protected void onDestroy()
    {
        DashboardActivity.isAppRunning = false;
        super.onDestroy();
    }
}
