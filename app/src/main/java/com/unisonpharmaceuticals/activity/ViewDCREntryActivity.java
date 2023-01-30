package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.DCRReportGetSet;
import com.unisonpharmaceuticals.model.ReportDetailsResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDCREntryActivity extends BaseClass
{
	ExpandableListView explistView;
	DialogListAdapter adapter;
	Activity activity;
	SessionManager sessionManager;
	LinearLayout llTop;
	String date = "",id = "";

	LinearLayout llLoading;

	ArrayList<DCRReportGetSet> list = new ArrayList<>();

	ImageView iv_leftDrawer,iv_logout;
	private Toolbar toolbar;
	
	ListView listView;
	TextView txtViewMore,txtName,txtDivName,txtWorkWith,txtWorkArea,txtHQ,txtRemarks,txtAdvise;
	String name = "",divName = "",workWith = "",workArea = "",hq = "",remarks = "",advise = "";

	ApiInterface apiInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandablelistview);
		
		activity = ViewDCREntryActivity.this;
		sessionManager = new SessionManager(activity);

		apiInterface = ApiClient.getClient().create(ApiInterface.class);

		try {
			Intent intent = getIntent();
			date = intent.getStringExtra("date");
			id = intent.getStringExtra("id");
		} catch (Exception e) {
			e.printStackTrace();
		}

		basicProcesses();

		
		txtViewMore.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (sessionManager.isNetworkAvailable())
				{

					String url = ApiClient.DCR_REPORT + "date="+date+"&"+"staff_id="+id+"&session_id="+sessionManager.getUserId();


					Intent intent = new Intent(activity,WebViewActivity.class);
					intent.putExtra("cameFrom",ApiClient.REPORT_DRC);
					intent.putExtra("report_url",url);
					startActivity(intent);


					/*Intent intent = new Intent(activity,DCRWebViewActivity.class);
					intent.putExtra("date", date);
					intent.putExtra("mktCode", mktCode);
					startActivity(intent);*/
				}
				else
				{
					Toast toast = Toast.makeText(activity, "Please check your Internet Connection", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
	}

	@Override
	public void initViews()
	{
		llLoading = (LinearLayout) findViewById(R.id.llLoading);
		explistView = (ExpandableListView) findViewById(R.id.expandableListView1);
		explistView.setVisibility(View.GONE);

		toolbar = (Toolbar) findViewById(R.id.toolbar);

		findViewById(R.id.llLogout).setVisibility(View.GONE);
		findViewById(R.id.llNotification).setVisibility(View.GONE);
		findViewById(R.id.llBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
				finishActivityAnimation(activity);
			}
		});

		TextView txtTitle = findViewById(R.id.txtTitle);
		txtTitle.setText(universalDateConvert(date,"yyyy-MM-dd","dd MMM, yyyy").toUpperCase());

		listView = (ListView) findViewById(R.id.lv_Dialog);
		listView.setVisibility(View.VISIBLE);

		txtViewMore = (TextView) findViewById(R.id.txtViewMore);
		txtViewMore.setVisibility(View.VISIBLE );

		txtDivName = (TextView) findViewById(R.id.txtDivName_ViewDCREntry);
		txtHQ = (TextView) findViewById(R.id.txtHQ_ViewDCREntry);
		txtName= (TextView) findViewById(R.id.txtName_ViewDCREntry);
		txtWorkArea= (TextView) findViewById(R.id.txtWorkArea_ViewDCREntry);
		txtWorkWith = (TextView) findViewById(R.id.txtWorkWith_ViewDCREntry);
		txtRemarks = (TextView) findViewById(R.id.txtRemarks);
		txtAdvise = (TextView) findViewById(R.id.txtAdvise);

		llTop = (LinearLayout) findViewById(R.id.llTop_ExpandableListView);
		llTop.setVisibility(View.VISIBLE);
	}

	@Override
	public void viewClick() {

	}

	@Override
	public void getDataFromServer() {

		if (sessionManager.isNetworkAvailable())
		{
			llLoading.setVisibility(View.VISIBLE);
			Call<ReportDetailsResponse> empCall = apiInterface.getReportDetails(date,id,sessionManager.getUserId());
			empCall.enqueue(new Callback<ReportDetailsResponse>() {
				@Override
				public void onResponse(Call<ReportDetailsResponse> call, Response<ReportDetailsResponse> response)
				{
					try
					{
						Log.e("CALLED", "onResponse: ");

						if(response.body().getSuccess()==1)
						{
							ReportDetailsResponse.ReportBean.StaffSummaryBean staffBean = response.body().getReport().getStaff_summary();

							hq = staffBean.getHead_quarters();
							name = staffBean.getName();
							divName = staffBean.getDivision();
							workArea = staffBean.getWork_area();

							txtDivName.setText(divName);
							txtHQ.setText(hq);
							txtName.setText(name);

							//OverAll

							ReportDetailsResponse.ReportBean.DoctorSummaryBean.OverallBean ovBean = response.body().getReport().getDoctor_summary().getOverall();
							DCRReportGetSet  getSet = new DCRReportGetSet ();

							getSet.setSummaryOf("Overall");

							getSet.setAP(ovBean.getAp());
							getSet.setAvgs(ovBean.getAvg());
							getSet.setCONS(ovBean.getCons());
							getSet.setGP(ovBean.getGp());

							getSet.setImp(ovBean.getImp());
							getSet.setInte(ovBean.getIntX());
							getSet.setNCCons(ovBean.getNc_cons());
							getSet.setNCGp(ovBean.getNc_gp());

							getSet.setNCImp(ovBean.getNc_imp());
							getSet.setNCTotal(ovBean.getNc_total());
							//getSet.setNEWDOCTOR(String.valueOf(ovBean.getNewDoctors()));
							getSet.setPotential(ovBean.getPotential());

							//getSet.setReptAP(String.valueOf(ovBean.getRepeatAp()));
							//getSet.setReptCONS(String.valueOf(ovBean.getRepeatCons()));
							//getSet.setReptGp(String.valueOf(ovBean.getRepeatGp()));
							getSet.setReptPotential(0);

							//getSet.setReptRESI(String.valueOf(ovBean.getRepeatResi()));
							//getSet.setReptTotal(String.valueOf(ovBean.getRepeatTotal()));
							//getSet.setRPTImp(object.getString("RPTImp"));
							getSet.setTotal(ovBean.getTotal());

							getSet.setRESI(ovBean.getResi());

							getSet.setZCRCall(ovBean.getZcr_call());
							getSet.setTCall(ovBean.getTcall());

							//getSet.setSummaryOf(object.getString("SummaryOf"));
							list.add(getSet);


							ReportDetailsResponse.ReportBean.DoctorSummaryBean.PreviousBean pvBean = response.body().getReport().getDoctor_summary().getPrevious();
							getSet = new DCRReportGetSet ();

							getSet.setSummaryOf("Previous");
							getSet.setCONS(pvBean.getCons());
							getSet.setReptCONS(pvBean.getRepeat_cons());
							getSet.setGP(pvBean.getGp());
							getSet.setReptGp(pvBean.getRepeat_gp());
							getSet.setAP(pvBean.getAp());
							getSet.setReptAP(pvBean.getRepeat_ap());
							getSet.setRESI(pvBean.getResi());
							getSet.setReptRESI(pvBean.getRepeat_resi());
							getSet.setTotal(pvBean.getTotal());
							getSet.setReptTotal(pvBean.getRepeat_total());
							getSet.setImp(pvBean.getImp());
							getSet.setRPTImp(pvBean.getRepeat_imp());
							getSet.setPotential(pvBean.getPotential());
							getSet.setReptPotential(pvBean.getRepeat_pot());
							getSet.setNEWDOCTOR(pvBean.getNewX());
							getSet.setNCTotal(pvBean.getNc_total());
							getSet.setNCGp(pvBean.getNc_gp());
							getSet.setNCCons(pvBean.getNc_cons());
							getSet.setNCImp(pvBean.getNc_imp());
							getSet.setTCall(pvBean.getTcall());
							getSet.setZCRCall(pvBean.getZcr_call());
							getSet.setInte(pvBean.getIntX());
							getSet.setAvgs(pvBean.getAvg());
							list.add(getSet);

							ReportDetailsResponse.ReportBean.DoctorSummaryBean.TodayBean tdBean = response.body().getReport().getDoctor_summary().getToday();
							getSet = new DCRReportGetSet ();

							getSet.setSummaryOf("Today");
							getSet.setCONS(tdBean.getCons());
							getSet.setReptCONS(tdBean.getRepeat_cons());
							getSet.setGP(tdBean.getGp());
							getSet.setReptGp(tdBean.getRepeat_gp());
							getSet.setAP(tdBean.getAp());
							getSet.setReptAP(tdBean.getRepeat_ap());
							getSet.setRESI(tdBean.getResi());
							getSet.setReptRESI(tdBean.getRepeat_resi());
							getSet.setTotal(tdBean.getTotal());
							getSet.setReptTotal(tdBean.getRepeat_total());
							getSet.setImp(tdBean.getImp());
							getSet.setRPTImp(tdBean.getRepeat_imp());
							getSet.setPotential(tdBean.getPotential());
							getSet.setReptPotential(tdBean.getRepeat_pot());
							getSet.setNEWDOCTOR(tdBean.getNewX());
							getSet.setNCTotal(tdBean.getNc_total());
							getSet.setNCGp(tdBean.getNc_gp());
							getSet.setNCCons(tdBean.getNc_cons());
							getSet.setNCImp(tdBean.getNc_imp());
							getSet.setTCall(tdBean.getTcall());
							getSet.setZCRCall(tdBean.getZcr_call());
							getSet.setInte(tdBean.getIntX());
							getSet.setAvgs(tdBean.getAvg());
							list.add(getSet);


							ReportDetailsResponse.ReportBean.DoctorSummaryBean.TotalBean ttBean = response.body().getReport().getDoctor_summary().getTotal();
							getSet = new DCRReportGetSet ();

							getSet.setSummaryOf("Total");
							getSet.setCONS(ttBean.getCons());
							getSet.setReptCONS(ttBean.getRepeat_cons());
							getSet.setGP(ttBean.getGp());
							getSet.setReptGp(ttBean.getRepeat_gp());
							getSet.setAP(ttBean.getAp());
							getSet.setReptAP(ttBean.getRepeat_ap());
							getSet.setRESI(ttBean.getResi());
							getSet.setReptRESI(ttBean.getRepeat_resi());
							getSet.setTotal(ttBean.getTotal());
							getSet.setReptTotal(ttBean.getRepeat_total());
							getSet.setImp(ttBean.getImp());
							getSet.setRPTImp(ttBean.getRepeat_imp());
							getSet.setPotential(ttBean.getPotential());
							getSet.setReptPotential(ttBean.getRepeat_pot());
							getSet.setNEWDOCTOR(ttBean.getNewX());
							getSet.setNCTotal(ttBean.getNc_total());
							getSet.setNCGp(ttBean.getNc_gp());
							getSet.setNCCons(ttBean.getNc_cons());
							getSet.setNCImp(ttBean.getNc_imp());
							getSet.setTCall(ttBean.getTcall());
							getSet.setZCRCall(ttBean.getZcr_call());
							getSet.setInte(ttBean.getIntX());
							getSet.setAvgs(ttBean.getAvg());
							list.add(getSet);


							if (workArea.equals(""))
							{
								txtWorkArea.setText("- N.A -");
							}
							else
							{
								txtWorkArea.setText(workArea);
							}

							if (remarks.length() > 0)
							{
								txtRemarks.setVisibility(View.VISIBLE);
								txtRemarks.setText("Remarks:- * " + remarks);
							}
							else
							{
								txtRemarks.setText("Remarks:-");
							}

							if (advise.length() > 0)
							{
								txtAdvise.setVisibility(View.VISIBLE);
								txtAdvise.setText("Advice:- * " + advise);
							}
							else
							{
								txtAdvise.setText("Advice:- ");
							}

							adapter = new DialogListAdapter(activity, list);
							listView.setAdapter(adapter);
						}
						else
						{
							AppUtils.showToast(activity, response.body().getMessage());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					llLoading.setVisibility(View.GONE);
				}
				@Override
				public void onFailure(Call<ReportDetailsResponse> call, Throwable t)
				{
					Log.e("CALLED", "onFailure: "+t.getMessage());
					AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
					llLoading.setVisibility(View.GONE);
				}
			});
		}
		else
		{
			showToast(activity,activity.getString(R.string.network_failed_message));
		}
	}

	private class DialogListAdapter extends BaseAdapter
	{
		private Activity activity;
	    private LayoutInflater inflater=null;
	    ArrayList<DCRReportGetSet> items;

		public DialogListAdapter(Activity a, ArrayList<DCRReportGetSet> item)
		{
	        this.activity = a;
	        this.items = item;
	        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	 
	    public int getCount() 
	    {
	        return items.size();
	    }
	 
	    public Object getItem(int position)
	    {
	        return position;
	    }
	 
	    public long getItemId(int position) 
	    {
	        return position;
	    }
	    
		public View getView(final int position, View convertView, ViewGroup parent)
	    {
			ViewHolder holder = null;
			
	        View rowView = convertView;
	        if(convertView == null)
	        {
	        	try
	    		{
	        		rowView = inflater.inflate(R.layout.rowview_table, null);
	        		
	        		holder = new ViewHolder();
	        		
	        		holder.txtHeaderRowViewTable = (TextView)rowView.findViewById( R.id.txtHeader_RowViewTable );
	        		holder.txtCons = (TextView)rowView.findViewById( R.id.txtCons );
	        		holder.txtReptCons = (TextView)rowView.findViewById( R.id.txtReptCons );
	        		holder.txtGp = (TextView)rowView.findViewById( R.id.txtGp );
	        		holder.txtReptGp = (TextView)rowView.findViewById( R.id.txtReptGp );
	        		holder.txtAP = (TextView)rowView.findViewById( R.id.txtAP );
	        		holder.txtReptAP = (TextView)rowView.findViewById( R.id.txtReptAP );
	        		holder.txtResi = (TextView)rowView.findViewById( R.id.txtResi );
					holder.txtReptResi = (TextView)rowView.findViewById( R.id.txtReptResi );
	        		holder.txtNewDoctor = (TextView)rowView.findViewById( R.id.txtNewDoctor );
	        		holder.txtAVG = (TextView)rowView.findViewById( R.id.txtAVG );
	        		holder.txtTotal = (TextView)rowView.findViewById( R.id.txtTotal );
	        		holder.txtReptTotal = (TextView)rowView.findViewById( R.id.txtReptTotal );
	        		holder.txtImp = (TextView)rowView.findViewById( R.id.txtImp );
	        		holder.txtReptImp = (TextView)rowView.findViewById( R.id.txtReptImp );
	        		holder.txtPotential = (TextView)rowView.findViewById( R.id.txtPotential );
	        		holder.txtReptPotential = (TextView)rowView.findViewById( R.id.txtReptPotential );
	        		holder.txtNCTotal = (TextView)rowView.findViewById( R.id.txtNCTotal );
	        		holder.txtNcGp = (TextView)rowView.findViewById( R.id.txtNcGp );
	        		holder.txtNcCons = (TextView)rowView.findViewById( R.id.txtNcCons );
	        		holder.txtNCImp = (TextView)rowView.findViewById( R.id.txtNCImp );
	        		holder.txtInte = (TextView)rowView.findViewById( R.id.txtInte );
					holder.txtTCall = (TextView)rowView.findViewById( R.id.txtTCall );
					holder.txtZCRCall = (TextView)rowView.findViewById( R.id.txtZCRCall );
	        		
	        		rowView.setTag(holder);
	    		}
	        	catch (Exception e)
	        	{
	        		e.printStackTrace();
				}
	        }
	        else
		    {
		        holder = (ViewHolder) rowView.getTag();
		    }
	        
	        if (items.get(position).getSummaryOf().equals("Today"))
	        {
	        	holder.txtHeaderRowViewTable.setText("Today's Call");
			}
	        else if (items.get(position).getSummaryOf().equals("Total"))
	        {
	        	holder.txtHeaderRowViewTable.setText("Total Call");
			}
	        else if (items.get(position).getSummaryOf().equals("Doctor Total"))
	        {
	        	holder.txtHeaderRowViewTable.setText("Total Doctors List");
			}
	        else if (items.get(position).getSummaryOf().equals("Previous"))
	        {
	        	holder.txtHeaderRowViewTable.setText("Previous Call");
			}
	        else if (items.get(position).getSummaryOf().contains("Average"))
	        {
	        	holder.txtHeaderRowViewTable.setText("Call Average");
			}
	        else
	        {
				holder.txtHeaderRowViewTable.setText(items.get(position).getSummaryOf());
			}
	        
	        holder.txtAP.setText(String.valueOf(items.get(position).getAP()));
	        holder.txtAVG.setText(String.valueOf(items.get(position).getAvgs()));
	        holder.txtCons.setText(String.valueOf(items.get(position).getCONS()));
	        holder.txtGp.setText(String.valueOf(items.get(position).getGP()));
	        
	        holder.txtImp.setText(String.valueOf(items.get(position).getImp()));
	        holder.txtInte.setText(String.valueOf(items.get(position).getInte()));
	        holder.txtNcCons.setText(String.valueOf(items.get(position).getNCCons()));
	        holder.txtNcGp.setText(String.valueOf(items.get(position).getNCGp()));
	        
	        holder.txtNCImp.setText(String.valueOf(items.get(position).getNCImp()));
	        holder.txtNCTotal.setText(String.valueOf(items.get(position).getNCTotal()));
	        holder.txtNewDoctor.setText(String.valueOf(items.get(position).getNEWDOCTOR()));
	        holder.txtPotential.setText(String.valueOf(items.get(position).getPotential()));
	        
	        holder.txtReptAP.setText(String.valueOf(items.get(position).getReptAP()));
	        holder.txtReptCons.setText(String.valueOf(items.get(position).getReptCONS()));
	        holder.txtReptGp.setText(String.valueOf(items.get(position).getReptGp()));
	        holder.txtReptImp.setText(String.valueOf(items.get(position).getRPTImp()));
	        
	        holder.txtReptPotential.setText(String.valueOf(items.get(position).getReptPotential()));
	        holder.txtReptTotal.setText(String.valueOf(items.get(position).getReptTotal()));
	        holder.txtResi.setText(String.valueOf(items.get(position).getRESI()));
	        holder.txtTotal.setText(String.valueOf(items.get(position).getTotal()));

	        holder.txtReptResi.setText(String.valueOf(items.get(position).getReptRESI()));
			holder.txtZCRCall.setText(String.valueOf(items.get(position).getZCRCall()));
			holder.txtTCall.setText(String.valueOf(items.get(position).getTCall()));
			
	        return rowView;
	    }
		
		private class ViewHolder
	    {
			private TextView txtHeaderRowViewTable;
			private TextView txtCons;
			private TextView txtReptCons;
			private TextView txtGp;
			private TextView txtReptGp;
			private TextView txtAP;
			private TextView txtReptAP;
			private TextView txtResi,txtReptResi;
			private TextView txtNewDoctor;
			private TextView txtAVG;
			private TextView txtTotal;
			private TextView txtReptTotal;
			private TextView txtImp;
			private TextView txtReptImp;
			private TextView txtPotential;
			private TextView txtReptPotential;
			private TextView txtNCTotal;
			private TextView txtNcGp;
			private TextView txtNcCons;
			private TextView txtNCImp;
			private TextView txtInte;
			private TextView txtTCall,txtZCRCall;
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
