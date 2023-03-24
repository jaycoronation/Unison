package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.fragment.FragmentDCR;
import com.unisonpharmaceuticals.fragment.FragmentMakeEntry;
import com.unisonpharmaceuticals.fragment.FragmentPlannerEntry;
import com.unisonpharmaceuticals.fragment.FragmentPlannerReport;
import com.unisonpharmaceuticals.fragment.FragmentSalesUpdateNew;
import com.unisonpharmaceuticals.fragment.FragmentSampleUpdateNew;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityDailyCallReport extends FragmentActivity
{
	private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    private TextView tvDate;
    
    public static String ncrDoctorName="";
    public static String ncrDoctorId="";

    public static ImageView ivAddArea;
    
    SessionManager sessionManager;

    Activity activity;

	/*private int[] tabsIcon = {R.drawable.tab_entry_selector, R.drawable.tab_dcr_report_selector, R.drawable.tab_sample_update_selector, R.drawable.tab_sales_update_selector,R.drawable.tab_planner_entry_selector,R.drawable.tab_planner_report_selector,R.drawable.tab_planner_report_selector};
	private String[] tabsTitle = {"Make Entry", "DCR Report","Sample Update" ,"Sale Update","Planner Entry","Planner Report"};*/

	private int[] tabsIcon;
	private String[] tabsTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
		DashboardActivity.isAppRunning = true;
        activity = ActivityDailyCallReport.this;
        
        sessionManager = new SessionManager(activity);

        initViews();
		viewClick();
    }

	public void initViews()
	{
		toolbar = (Toolbar)findViewById(R.id.toolbar);


		ivAddArea = (ImageView) findViewById(R.id.ivAddArea);

		findViewById(R.id.ivBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				AppUtils.hideKeyboard(toolbar,activity);
				activity.finish();
				AppUtils.finishActivityAnimation(activity);
			}
		});

		tvDate = (TextView) findViewById(R.id.tvTitle);
		try
		{
			Date d=new Date();
			String currentdate = new SimpleDateFormat("dd MMM, yyyy").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(d.toString()));
			tvDate.setText(currentdate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		setupViewPager(viewPager);

		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);

		if(sessionManager.isManagerLoggedIN())
        {
			tabsIcon = new int[]{R.drawable.tab_dcr_report_selector};
			tabsTitle = new String[]{"DCR Report"};
            findViewById(R.id.frameTab).setVisibility(View.GONE);
        }
        else
		{
			if (sessionManager.getSamplePermission().equalsIgnoreCase("1") &&
					sessionManager.getSalesPermission().equalsIgnoreCase("0"))
			{
				tabsIcon = new int[] {R.drawable.tab_entry_selector, R.drawable.tab_dcr_report_selector, R.drawable.tab_sample_update_selector,R.drawable.tab_planner_entry_selector,R.drawable.tab_planner_report_selector,R.drawable.tab_planner_report_selector};
				tabsTitle = new String[] {"Make Entry", "DCR Report","Sample Update" ,"Planner Entry","Planner Report"};
				try
				{
					viewPager.setOffscreenPageLimit(5);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(sessionManager.getSamplePermission().equalsIgnoreCase("0") &&
					sessionManager.getSalesPermission().equalsIgnoreCase("1"))
			{
				tabsIcon = new int[] {R.drawable.tab_entry_selector, R.drawable.tab_dcr_report_selector, R.drawable.tab_sales_update_selector,R.drawable.tab_planner_entry_selector,R.drawable.tab_planner_report_selector,R.drawable.tab_planner_report_selector};
				tabsTitle = new String[] {"Make Entry", "DCR Report","Sale Update","Planner Entry","Planner Report"};
				try
				{
					viewPager.setOffscreenPageLimit(5);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(sessionManager.getSamplePermission().equalsIgnoreCase("0") &&
					sessionManager.getSalesPermission().equalsIgnoreCase("0"))
			{
				tabsIcon = new int[] {R.drawable.tab_entry_selector, R.drawable.tab_dcr_report_selector,R.drawable.tab_planner_entry_selector,R.drawable.tab_planner_report_selector,R.drawable.tab_planner_report_selector};
				tabsTitle = new String[] {"Make Entry", "DCR Report","Planner Entry","Planner Report"};
				try
				{
					viewPager.setOffscreenPageLimit(4);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				tabsIcon = new int[] {R.drawable.tab_entry_selector, R.drawable.tab_dcr_report_selector, R.drawable.tab_sample_update_selector, R.drawable.tab_sales_update_selector,R.drawable.tab_planner_entry_selector,R.drawable.tab_planner_report_selector,R.drawable.tab_planner_report_selector};
				tabsTitle = new String[] {"Make Entry", "DCR Report","Sample Update" ,"Sale Update","Planner Entry","Planner Report"};
				try
				{
					viewPager.setOffscreenPageLimit(6);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			setupTabIcons();

			try
			{
				tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}


		ivAddArea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(viewPager.getCurrentItem()==0)
				{
					if(FragmentMakeEntry.areaHandler!=null)
					{
						Message message = Message.obtain();
						message.what = 113;
						FragmentMakeEntry.areaHandler.sendMessage(message);
					}
				}
				else
				{
					if(FragmentPlannerEntry.handler!=null)
					{
						Message message = Message.obtain();
						message.what = 122;
						FragmentPlannerEntry.handler.sendMessage(message);
					}
				}
			}
		});

		try
		{
			ncrDoctorName = getIntent().getStringExtra("DoctorName");
			ncrDoctorId = getIntent().getStringExtra("DoctorId");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void viewClick()
	{
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
            	if(sessionManager.isManagerLoggedIN())
				{
					if(position==0)
					{
						ivAddArea.setVisibility(View.GONE);
						tvDate.setText("DCR REPORTS");
					}
				}
				else
				{

					if(position==0)
					{
						if(sessionManager.getUSerType().equalsIgnoreCase(ApiClient.MR))
						{
							if(sessionManager.getOffDayOrAdminDay().equalsIgnoreCase("1"))
							{
								ivAddArea.setVisibility(View.GONE);
							}
							else
							{
								ivAddArea.setVisibility(View.VISIBLE);
							}
						}
						else
						{
							ivAddArea.setVisibility(View.VISIBLE);
						}
						try
						{
							Date d=new Date();
							String currentdate = new SimpleDateFormat("dd MMM, yyyy").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(d.toString()));
							tvDate.setText(currentdate);
						}
						catch (ParseException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						//ivAddArea.setVisibility(View.GONE);
						tvDate.setText(tabsTitle[position].toUpperCase());

						//New AddArea display for planner Entry
						if (sessionManager.getSamplePermission().equalsIgnoreCase("1")
								&& sessionManager.getSalesPermission().equalsIgnoreCase("0"))
						{
							if(position==3)
							{
								ivAddArea.setVisibility(View.VISIBLE);
							}
							else
							{
								ivAddArea.setVisibility(View.GONE);
							}
						}
						else if(sessionManager.getSamplePermission().equalsIgnoreCase("0")
								&& sessionManager.getSalesPermission().equalsIgnoreCase("1"))
						{
							if(position==3)
							{
								ivAddArea.setVisibility(View.VISIBLE);
							}
							else
							{
								ivAddArea.setVisibility(View.GONE);
							}
						}
						else if(sessionManager.getSamplePermission().equalsIgnoreCase("0")
								&& sessionManager.getSalesPermission().equalsIgnoreCase("0"))
						{
							if(position==2)
							{
								ivAddArea.setVisibility(View.VISIBLE);
							}
							else
							{
								ivAddArea.setVisibility(View.GONE);
							}
						}
						else
						{
							if(position==4)
							{
								ivAddArea.setVisibility(View.VISIBLE);
							}
							else
							{
								ivAddArea.setVisibility(View.GONE);
							}
						}

					}


					/*if(position==0)
					{
						ivAddArea.setVisibility(View.VISIBLE);
						try
						{
							Date d=new Date();
							String currentdate = new SimpleDateFormat("dd MMM, yyyy").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(d.toString()));
							tvDate.setText(currentdate);
						}
						catch (ParseException e)
						{
							e.printStackTrace();
						}
					}
					else if(position==1)
					{
						ivAddArea.setVisibility(View.GONE);
						tvDate.setText("DCR REPORTS");
					}
					else if(position==2)
					{
						ivAddArea.setVisibility(View.GONE);
						tvDate.setText("SAMPLE UPDATE");
					}
					else if(position==3)
					{
						ivAddArea.setVisibility(View.GONE);
						tvDate.setText("SALES UPDATE");
					}
					else if(position==4)
					{
						ivAddArea.setVisibility(View.GONE);
						tvDate.setText("NEXT DAY PLAN");
					}
					else if(position==5)
					{
						ivAddArea.setVisibility(View.GONE);
						tvDate.setText("PLANNER REPORT");
					}
					else
					{
						ivAddArea.setVisibility(View.GONE);
						tvDate.setText("UNISON");
					}*/
				}

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
	}

	public void getDataFromServer()
	{

	}

	private void setupViewPager(ViewPager viewPager)
	{
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		if(sessionManager.isManagerLoggedIN())
		{
			adapter.addFragment(new FragmentDCR());
		}
		else
		{
			if (sessionManager.getSamplePermission().equalsIgnoreCase("1")
					&& sessionManager.getSalesPermission().equalsIgnoreCase("0"))
			{
				adapter.addFragment(new FragmentMakeEntry());
				adapter.addFragment(new FragmentDCR());
				adapter.addFragment(new FragmentSampleUpdateNew());
				adapter.addFragment(new FragmentPlannerEntry());
				adapter.addFragment(new FragmentPlannerReport());
			}
			else if(sessionManager.getSamplePermission().equalsIgnoreCase("0")
					&& sessionManager.getSalesPermission().equalsIgnoreCase("1"))
			{
				adapter.addFragment(new FragmentMakeEntry());
				adapter.addFragment(new FragmentDCR());
				adapter.addFragment(new FragmentSalesUpdateNew());
				adapter.addFragment(new FragmentPlannerEntry());
				adapter.addFragment(new FragmentPlannerReport());
			}
			else if(sessionManager.getSamplePermission().equalsIgnoreCase("0")
					&& sessionManager.getSalesPermission().equalsIgnoreCase("0"))
			{
				adapter.addFragment(new FragmentMakeEntry());
				adapter.addFragment(new FragmentDCR());
				adapter.addFragment(new FragmentPlannerEntry());
				adapter.addFragment(new FragmentPlannerReport());
			}
			else
			{
				adapter.addFragment(new FragmentMakeEntry());
				adapter.addFragment(new FragmentDCR());
				adapter.addFragment(new FragmentSampleUpdateNew());
				adapter.addFragment(new FragmentSalesUpdateNew());
				adapter.addFragment(new FragmentPlannerEntry());
				adapter.addFragment(new FragmentPlannerReport());
			}
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

			for (int i = 0; i < tabsTitle.length; i++)
			{
				final View tabOne = LayoutInflater.from(activity).inflate(R.layout.rowview_main_tablayout, null);
				ImageView ivTabOne = (ImageView) tabOne.findViewById(R.id.ivTab);
				ivTabOne.setImageResource(tabsIcon[i]);
				TextView txtTabOne = (TextView) tabOne.findViewById(R.id.txtTab);
				txtTabOne.setText(tabsTitle[i]);
				tabLayout.getTabAt(i).setCustomView(tabOne);
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
			finish();
			AppUtils.finishActivityAnimation(activity);
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
