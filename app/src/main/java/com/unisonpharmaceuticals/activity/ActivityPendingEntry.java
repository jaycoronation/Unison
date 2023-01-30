package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.NewEntryGetSet;
import com.unisonpharmaceuticals.model.VariationResponse;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityPendingEntry extends BaseClass
{
	public static Activity activity;
	private SessionManager sessionManager;
	private Toolbar toolbar;
	private ImageView iv_leftDrawer,iv_logout;
	private LinearLayout llNoData;
	
	private ListView mainListView;

	public static Handler handler;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pending_entry);
		
		activity = ActivityPendingEntry.this;
		
		sessionManager = new SessionManager(activity);

		DashboardActivity.isAppRunning = true;

		basicProcesses();

		List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
		ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
		for (int i = 0; i < listOffline.size(); i++)
		{
			if(listOffline.get(i).getUser_id().equals(sessionManager.getUserId()))
			{
				listUserEntry.add(listOffline.get(i));
			}
		}

		PendingEntryDialogAdapter adapter = new PendingEntryDialogAdapter(activity, listUserEntry,llNoData,mainListView);
	    mainListView.setAdapter(adapter);

	    handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg)
			{
				if(msg.what==101)
				{
					List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
					ArrayList<NewEntryGetSet> listEntry = (ArrayList<NewEntryGetSet>) listOffline;

					ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
					for (int i = 0; i < listEntry.size(); i++)
					{
						if(listEntry.get(i).getUser_id().equals(sessionManager.getUserId()))
						{
							listUserEntry.add(listEntry.get(i));
						}
					}

					PendingEntryDialogAdapter adapter = new PendingEntryDialogAdapter(activity, listUserEntry,llNoData,mainListView);
					mainListView.setAdapter(adapter);
				}
				return false;
			}
		});
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
	protected void onResume() 
	{
		super.onResume();
	}


	@Override
	public void initViews()
	{
		mainListView = (ListView)findViewById(R.id.lv_Dialog);
		llNoData = (LinearLayout)findViewById(R.id.llNoData);
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setText("View Entry");
		findViewById(R.id.llLogout).setVisibility(View.GONE);
		findViewById(R.id.llNotification).setVisibility(View.GONE);
		findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
				finishActivityAnimation(activity);
			}
		});
	}

	@Override
	public void viewClick() {

	}

	@Override
	public void getDataFromServer() {

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		removeTempProducts();
		activity = null;
		DashboardActivity.isAppRunning = false;
	}

	//Used for unsaved products untill click update button in Temp Entries
	private void removeTempProducts()
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				try {
					List<NewEntryGetSet> listOffline = NewEntryGetSet.listAll(NewEntryGetSet.class);
					ArrayList<NewEntryGetSet> listUserEntry = new ArrayList<>();
					for (int i = 0; i < listOffline.size(); i++)
                    {
                        if(listOffline.get(i).getUser_id().equals(sessionManager.getUserId()))
                        {
                            listUserEntry.add(listOffline.get(i));
                        }
                    }

					for (int i = 0; i < listUserEntry.size(); i++)
                    {
                        ArrayList<VariationResponse.VariationsBean> listProducts = AppUtils.getArrayListFromJsonStringVariation(listUserEntry.get(i).getProducts());
                        ArrayList<VariationResponse.VariationsBean> listProductsForRemove = new ArrayList<>();
                        for (int j = 0; j < listProducts.size(); j++)
                        {
                            if(listProducts.get(j).getName().equalsIgnoreCase("Product") ||
                                    listProducts.get(j).getItem_code().equalsIgnoreCase("Product") ||
									listProducts.get(j).isTemp())
                            {
                                listProductsForRemove.add(listProducts.get(j));
                            }
                        }
                        listProducts.removeAll(listProductsForRemove);
                        NewEntryGetSet newEntryGetSet1 = NewEntryGetSet.findById(NewEntryGetSet.class, listUserEntry.get(i).getId());
                        newEntryGetSet1.setProducts(AppUtils.getStringFromArrayListVariations(listProducts));
                        newEntryGetSet1.save();
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
