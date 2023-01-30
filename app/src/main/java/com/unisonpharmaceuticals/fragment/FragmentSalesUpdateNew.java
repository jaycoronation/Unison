package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.activity.ViewSampleActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.DrBusinessResponse;
import com.unisonpharmaceuticals.model.DrFromMrResponse;
import com.unisonpharmaceuticals.model.EmployeeSalesNotifResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("unused")
public class FragmentSalesUpdateNew extends Fragment implements View.OnClickListener
{
	private Activity activity;
	private SessionManager sessionManager;
	private View rootView;
	private ApiInterface apiService;

	@BindView(R.id.llLoading)
	LinearLayout llLoading;

	@BindView(R.id.llEdit)
	LinearLayout llEdit;
	@BindView(R.id.tvSave)
	TextView tvSave;
	@BindView(R.id.tvReset)
	TextView tvReset;

	@BindView(R.id.inputDoctor)
	TextInputLayout inputDoctor;
	@BindView(R.id.edtDoctor)
	EditText edtDoctor;
	@BindView(R.id.rvStock)
	RecyclerView rvStock;

	@BindView(R.id.llBusiness)
	LinearLayout llBusiness;
	@BindView(R.id.edtOldBusiness)
	EditText edtOldBusiness;
	@BindView(R.id.edtNewBusiness)
	EditText edtNewBusiness;
	@BindView(R.id.llMain)
	LinearLayout llMain;
	@BindView(R.id.llNoData)
	LinearLayout llNoData;
	@BindView(R.id.tvNoData)
	TextView tvNoData;

	//For Notification
	@BindView(R.id.tvShowNotification)TextView tvShowNotification;

	private Dialog listDialog;

	private List<DrFromMrResponse.DoctorBean> listDoctors = new ArrayList<>();
	private List<DrFromMrResponse.DoctorBean> listDoctorSearch = new ArrayList<>();
	private List<DrBusinessResponse.ItmesBean> listSalesItems = new ArrayList<>();
	private List<DrBusinessResponse.ItmesBean> listTempSalesItems = new ArrayList<>();

	private ItemAdapter itemAdapter;

	private String selectedDrId = "";
	private final String DOCTOR = "doctor";

	int sumTemp = 0 , sumMain = 0;

	private String apiNewBusiness = "";

	public static Handler handler;

	private boolean isLoading = false;

	private long mLastClickTime = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_sample_update, container, false);
		apiService = ApiClient.getClient().create(ApiInterface.class);
		ButterKnife.bind(this,rootView);
		activity = getActivity();
		sessionManager = new SessionManager(activity);
		initViews();
		getDoctorsFromMR();
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg)
			{
				if(msg.what==100)
				{
					if(sessionManager.isNetworkAvailable())
					{
						llLoading.setVisibility(View.VISIBLE);
						Call<EmployeeSalesNotifResponse> empCall = apiService.getEmployeesForNotification(sessionManager.getUserId(),"sale",sessionManager.getUserId());
						empCall.enqueue(new Callback<EmployeeSalesNotifResponse>() {
							@Override
							public void onResponse(Call<EmployeeSalesNotifResponse> call, Response<EmployeeSalesNotifResponse> response)
							{
								try
								{
									if(response.body().getSuccess()==1)
									{
										if(response.body().getStaff().size()>0)
										{
											tvShowNotification.setVisibility(View.VISIBLE);
										}
										else
										{
											tvShowNotification.setVisibility(View.GONE);
										}
									}
									else
									{
										tvShowNotification.setVisibility(View.GONE);
									}
									llLoading.setVisibility(View.GONE);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(Call<EmployeeSalesNotifResponse> call, Throwable t)
							{
								tvShowNotification.setVisibility(View.GONE);
								llLoading.setVisibility(View.VISIBLE);
								AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
							}
						});
					}
					else
					{
						AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
					}
					//tvShowNotification.setVisibility(View.GONE);
				}
				return false;
			}
		});
		return rootView ;
	}

	private void getDoctorsFromMR()
	{
		isLoading = true;
		llLoading.setVisibility(View.VISIBLE);
		Call<DrFromMrResponse> drCall = apiService.getDoctorsFromMR(sessionManager.getUserId(),sessionManager.getUserId());
		drCall.enqueue(new Callback<DrFromMrResponse>() {
			@Override
			public void onResponse(Call<DrFromMrResponse> call, Response<DrFromMrResponse> response)
			{
				try {
					if(response.body().getSuccess()==1)
					{
						listDoctors = response.body().getDoctor();

						if(listDoctors.size()>0)
						{
							llMain.setVisibility(View.VISIBLE);
							llNoData.setVisibility(View.GONE);
						}
						else
						{
							llMain.setVisibility(View.GONE);
							llNoData.setVisibility(View.VISIBLE);
						}
					}
					else
					{
						llMain.setVisibility(View.GONE);
						llNoData.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				isLoading = false;
			}

			@Override
			public void onFailure(Call<DrFromMrResponse> call, Throwable t)
			{
				llMain.setVisibility(View.GONE);
				llNoData.setVisibility(View.VISIBLE);
				AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
				isLoading = false;
			}
		});

		isLoading = true;
		Call<EmployeeSalesNotifResponse> empCall = apiService.getEmployeesForNotification(sessionManager.getUserId(),"sale",sessionManager.getUserId());
		empCall.enqueue(new Callback<EmployeeSalesNotifResponse>() {
			@Override
			public void onResponse(Call<EmployeeSalesNotifResponse> call, Response<EmployeeSalesNotifResponse> response)
			{
				try
				{
					if(response.body().getSuccess()==1)
					{
						if(response.body().getStaff().size()>0)
						{
							tvShowNotification.setVisibility(View.VISIBLE);
						}
						else
						{
							tvShowNotification.setVisibility(View.GONE);
						}
					}
					else
					{
						tvShowNotification.setVisibility(View.GONE);
					}
					llLoading.setVisibility(View.GONE);
					isLoading = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<EmployeeSalesNotifResponse> call, Throwable t)
			{
				isLoading = false;
				tvShowNotification.setVisibility(View.GONE);
				llLoading.setVisibility(View.VISIBLE);
				AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
			}
		});

		llLoading.setVisibility(View.GONE);
	}

	private void getBusinessFromDoctor()
	{
		llLoading.setVisibility(View.VISIBLE);
		listSalesItems = new ArrayList<>();
		listTempSalesItems = new ArrayList<>();
		Call<DrBusinessResponse> drCall = apiService.getBusinessFromDr(sessionManager.getUserId(),selectedDrId,sessionManager.getUserId());
		drCall.enqueue(new Callback<DrBusinessResponse>() {
			@Override
			public void onResponse(Call<DrBusinessResponse> call, Response<DrBusinessResponse> response)
			{
				try
				{
					if(response.body().getSuccess()==1)
					{

						listSalesItems = response.body().getItmes();

						for (int i = 0; i < listSalesItems.size(); i++)
						{
							sumTemp += listSalesItems.get(i).getQty();
							listTempSalesItems.add(listSalesItems.get(i));
						}

						edtOldBusiness.setText(response.body().getDoctor_business().getOld_business());
						edtOldBusiness.setSelection(edtOldBusiness.getText().length());
						apiNewBusiness = String.valueOf(response.body().getDoctor_business().getPts_business());
						/*edtNewBusiness.setText(response.body().getDoctor_business().getBusiness());
						edtNewBusiness.setSelection(edtNewBusiness.getText().length());*/

						rvStock.setVisibility(View.VISIBLE);
						itemAdapter = new ItemAdapter(listSalesItems,rvStock);
						rvStock.setAdapter(itemAdapter);

						edtNewBusiness.setText(AppUtils.getRoundupLastTwoDigits(response.body().getDoctor_business().getPts_business()));
						edtNewBusiness.setSelection(AppUtils.getRoundupLastTwoDigits(response.body().getDoctor_business().getPts_business()).length());

					}
					else
					{
						AppUtils.showToast(activity,"No Items for sales available!");
						activity.finish();
					}
					llLoading.setVisibility(View.GONE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<DrBusinessResponse> call, Throwable t)
			{
				llLoading.setVisibility(View.GONE);
				AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
			}
		});
	}

	private void initViews()
	{
		tvNoData.setText("No doctors availabe");
		rvStock.setLayoutManager(new LinearLayoutManager(activity));
		edtDoctor.setOnClickListener(this);
		tvReset.setOnClickListener(this);
		tvSave.setOnClickListener(this);
		tvShowNotification.setOnClickListener(this);
		tvShowNotification.setText("View Sale Updates");
		edtNewBusiness.setEnabled(false);
	}

	@Override
	public void onClick(View v)
	{
		if (SystemClock.elapsedRealtime() - mLastClickTime < ApiClient.CLICK_THRESHOLD) {
			return;
		}
		mLastClickTime = SystemClock.elapsedRealtime();
		switch (v.getId())
		{
			case R.id.edtDoctor:
				if(isLoading)
				{
					AppUtils.showLoadingToast(activity);
				}
				else
				{
					if(listDoctors.size()>0)
					{
						showListDialog(DOCTOR);
					}
					else
					{
						AppUtils.showToast(activity,"No Doctors available!");
					}
				}
				break;
			case R.id.tvSave:
				try
				{

					AppUtils.hideKeyboard(edtDoctor,activity);

					if(edtNewBusiness.getText().toString().equalsIgnoreCase(""))
					{
						AppUtils.showToast(activity,"Please enter new business.");
						return;
					}
					else if(apiNewBusiness.equalsIgnoreCase(edtNewBusiness.getText().toString().trim()))
					{
						AppUtils.showToast(activity,"Please update new business.");
						return;
					}
					else
					{
						for(int i = 0; i < listSalesItems.size(); i++)
						{
							sumMain += listSalesItems.get(i).getQty();
						}
						if(sumMain!=sumTemp)
						{
							JSONObject jsonObject = new JSONObject();
							for (int i = 0; i < listSalesItems.size(); i++)
							{
								jsonObject.put(listSalesItems.get(i).getVariation_id(),listSalesItems.get(i).getQty());
							}
							salesUpdate(jsonObject.toString(),edtNewBusiness.getText().toString().trim(),edtOldBusiness.getText().toString().trim());
						}
						else
						{
							AppUtils.showToast(activity,"Please update the quantity");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.tvReset:
				selectedDrId = "";
				edtDoctor.setText("");
				llBusiness.setVisibility(View.GONE);
				llEdit.setVisibility(View.GONE);
				rvStock.setVisibility(View.GONE);
				break;
			case R.id.tvShowNotification:
				Intent intent = new Intent(activity, ViewSampleActivity.class);
				intent.putExtra("type","sale");
				startActivity(intent);
				AppUtils.startActivityAnimation(activity);
				break;

		}
	}

	private boolean checkIsStockChanges()
	{
		boolean isChanged = false;

		Log.e("Sizes >> ", "checkIsStockChanges: " +listTempSalesItems.size()  + "   " + listSalesItems.size() );

		for (int i = 0; i < listSalesItems.size(); i++)
		{

			Log.d("Diff STOCK >   ", "checkIsStockChanges: " +listTempSalesItems.get(i).getQty() + "   " + listSalesItems.get(i).getQty()  );

			/*if(listTempSalesItems.get(i).getQty() == listSalesItems.get(i).getQty())
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				break;
			}*/
		}

		return isChanged;
	}

	private void salesUpdate(final String qtyToPass,final String newBusiness,final String oldBusiness)
	{
		new AsyncTask<Void,Void,Void>()
		{
			int success = 0;
			String message = "";
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				llLoading.setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... voids)
			{
				try
				{
					HashMap<String,String> hashMap = new HashMap<>();
					hashMap.put("doctor_id",selectedDrId);
					hashMap.put("new_business",newBusiness);
					hashMap.put("old_business",oldBusiness);
					hashMap.put("employee",sessionManager.getUserId());
					hashMap.put("isMobile","true");
					hashMap.put("qty",qtyToPass);
					hashMap.put("login_user_id",sessionManager.getUserId());
					Log.e("Request", "doInBackground: " + hashMap.toString() );
					String response = "";
					response = MitsUtils.readJSONServiceUsingPOST(ApiClient.SALES_UPDATE,hashMap);
					Log.e("Response >>> ", "doInBackground: " + response );
					JSONObject jsonObject = new JSONObject(response);
					success = AppUtils.getValidAPIIntegerResponse(jsonObject.getString("success"));
					message = AppUtils.getValidAPIStringResponse(jsonObject.getString("message"));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				llLoading.setVisibility(View.GONE);
				AppUtils.showToast(activity,message);
				if(success==1)
				{
					selectedDrId = "";
					edtDoctor.setText("");
					llBusiness.setVisibility(View.GONE);
					llEdit.setVisibility(View.GONE);
					rvStock.setVisibility(View.GONE);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void)null);
	}

	DialogListAdapter areaAdapter;
	public void showListDialog(final String isFor)
	{

		listDialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

		listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
		listDialog.setContentView(sheetView);

		listDialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				AppUtils.hideKeyboard(sheetView,activity);
				listDialog.dismiss();
				listDialog.cancel();
			}
		});
		listDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				AppUtils.hideKeyboard(sheetView,activity);
			}
		});
		LinearLayout btnNo = (LinearLayout) listDialog.findViewById(R.id.btnNo);

		TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
		tvTitle.setText("Select "+isFor);

		TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

		tvDone.setVisibility(View.GONE);

		final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

		areaAdapter = new DialogListAdapter(listDialog, isFor,false,"",rvListDialog);
		rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
		rvListDialog.setAdapter(areaAdapter);

		tvDone.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				listDialog.dismiss();
				listDialog.cancel();
			}
		});

		btnNo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AppUtils.hideKeyboard(sheetView,activity);
				listDialog.dismiss();
				listDialog.cancel();
			}
		});

		final EditText edtSearchDialog = (EditText) listDialog.findViewById(R.id.edtSearchDialog);
		final TextInputLayout inputSearch = (TextInputLayout) listDialog.findViewById(R.id.inputSearch);

		edtSearchDialog.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
			{
				int textlength = edtSearchDialog.getText().length();

				if(isFor.equals(DOCTOR))
				{
					listDoctorSearch.clear();
					for (int i = 0; i < listDoctors.size(); i++)
					{
						if (textlength <= listDoctors.get(i).getDoctor_name().length())
						{
							if (listDoctors.get(i).getDoctor_name().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
									listDoctors.get(i).getDoctor_id().contains(edtSearchDialog.getText().toString().trim()))
							{
								listDoctorSearch.add(listDoctors.get(i));
							}
						}
					}
				}
				AppendList(listDialog,isFor,true,rvListDialog);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		listDialog.show();
	}

	private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>
	{
		String isFor = "";
		Dialog dialog;
		boolean isForSearch = false;
		String searchText = "";

		DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,RecyclerView recyclerView)
		{

			this.isFor = isFor;
			this.dialog = dialog;
			this.isForSearch = isForSearch;
			this.searchText = searchText;
		}

		@Override
		public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
			return new DialogListAdapter.ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(DialogListAdapter.ViewHolder holder, final int position)
		{
			if(position == getItemCount()-1)
			{
				holder.viewLine.setVisibility(View.GONE);
			}
			else
			{
				holder.viewLine.setVisibility(View.VISIBLE);
			}
			if(isFor.equals(DOCTOR))
			{
				holder.cb.setVisibility(View.GONE);
				final DrFromMrResponse.DoctorBean getSet;
				if(isForSearch)
				{
					getSet = listDoctorSearch.get(position);
				}
				else
				{
					getSet = listDoctors.get(position);
				}
				holder.tvValue.setText(getSet.getDoctor_name());
				holder.tvId.setText(getSet.getDoctor_id());
				holder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						llBusiness.setVisibility(View.VISIBLE);
						llEdit.setVisibility(View.VISIBLE);
						selectedDrId = getSet.getDoctor_id();
						edtDoctor.setText(getSet.getDoctor_name());
						dialog.dismiss();
						dialog.cancel();
						getBusinessFromDoctor();
					}
				});
			}
		}

		@Override
		public int getItemCount()
		{
			if(isFor.equalsIgnoreCase(DOCTOR))
			{
				if(isForSearch)
				{
					return listDoctorSearch.size();
				}
				else
				{
					return listDoctors.size();
				}
			}
			else {
				return 0;
			}
		}

		public class ViewHolder extends RecyclerView.ViewHolder
		{
			private TextView tvValue,tvId;
			private CheckBox cb;
			private View viewLine;
			public ViewHolder(View itemView)
			{
				super(itemView);
				tvValue = (TextView) itemView.findViewById(R.id.tvValue);
				tvId = (TextView) itemView.findViewById(R.id.tvId);
				viewLine = itemView.findViewById(R.id.viewLine);
				cb = (CheckBox) itemView.findViewById(R.id.cb);
				cb.setTypeface(AppUtils.getTypefaceRegular(activity));
			}
		}
	}

	private void AppendList(Dialog dialog, String isFor, boolean isForSearch, RecyclerView rvArea)
	{
		areaAdapter = new DialogListAdapter(dialog,isFor,true,"",rvArea);
		rvArea.setAdapter(areaAdapter);
		areaAdapter.notifyDataSetChanged();
	}

	public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

		List<DrBusinessResponse.ItmesBean> listItems;
		ItemAdapter(List<DrBusinessResponse.ItmesBean> list,RecyclerView recyclerView)
		{
			this.listItems = list;
		}

		@Override
		public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_sales_update, viewGroup, false);
			return new ItemAdapter.ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(final ItemAdapter.ViewHolder holder, final int position)
		{
			final DrBusinessResponse.ItmesBean getSet = listItems.get(position);

			if(position==0)
			{
				holder.llHeader.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.llHeader.setVisibility(View.GONE);
			}
			int no = 0;
			no = position + 1;
			holder.tvNumber.setText(String.valueOf(no));
			holder.tvItem.setText(getSet.getName() + " - "+getSet.getItem_id_code());
			holder.edtQty.setText(String.valueOf(getSet.getQty()));
			holder.edtQty.setSelection(holder.edtQty.getText().length());
		}

		private double getSumOfAllFieldsWithoutSpecificPosition(int position)
		{
			double sum = 0;
			for (int i = 0; i < listItems.size(); i++) {
				if(i!=position)
				{
					//multiply of qty and price
					double total = listItems.get(i).getQty()*Double.parseDouble(listItems.get(i).getPts_price());
					sum += total;
				}
			}
			return sum;
		}
		public double getSumOfAllFields()
		{
			double sum = 0;
			for (int i = 0; i < listItems.size(); i++) {
				double total = listItems.get(i).getQty()*Double.parseDouble(listItems.get(i).getPts_price());
				sum += total;
			}
			return sum;
		}

		@Override
		public int getItemCount() {
			return listItems.size();
		}

		@SuppressWarnings("unused")
		public class ViewHolder extends RecyclerView.ViewHolder {

			@BindView(R.id.llHeader)
			LinearLayout llHeader;

			@BindView(R.id.tvNumber)
			TextView tvNumber;

			@BindView(R.id.tvItem)
			TextView tvItem;

			@BindView(R.id.edtQty)
			EditText edtQty;

			ViewHolder(View convertView) {
				super(convertView);
				ButterKnife.bind(this, convertView);
				edtQty.addTextChangedListener(new TextWatcher()
				{
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after)
					{

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count)
					{

					}

					@Override
					public void afterTextChanged(Editable s)
					{
						/*if(!s.toString().equals("")||s.toString().equals("0"))
						{
							listItems.get(getAdapterPosition()).setQty(Integer.parseInt(edtQty.getText().toString()));

							double currentTotal = listItems.get(getAdapterPosition()).getQty() * Double.parseDouble(listItems.get(getAdapterPosition()).getPts_price());
							double sumWithoutCurrent = getSumOfAllFieldsWithoutSpecificPosition(getAdapterPosition());
							Log.e("Current Sum ", "afterTextChanged: "+currentTotal );
							Log.e("Without Current Sum ", "afterTextChanged: "+sumWithoutCurrent );
							double grandTotal = currentTotal+sumWithoutCurrent;
							Log.e("Total Sum ", "afterTextChanged: "+grandTotal );
							edtNewBusiness.setText(String.valueOf(grandTotal));
						}
						else
						{
							listItems.get(getAdapterPosition()).setQty(0);
						}
						listSalesItems.set(getAdapterPosition(),listItems.get(getAdapterPosition()));*/


						if(!s.toString().equals(""))
						{
							listItems.get(getAdapterPosition()).setQty(Integer.parseInt(edtQty.getText().toString()));
						}
						else if(!s.toString().equals("") && Integer.parseInt(s.toString())==0)
						{
							listItems.get(getAdapterPosition()).setQty(0);
						}
						else
						{
							listItems.get(getAdapterPosition()).setQty(0);
						}
						listSalesItems.set(getAdapterPosition(),listItems.get(getAdapterPosition()));
						try {
							double currentTotal = listItems.get(getAdapterPosition()).getQty() * Double.parseDouble(listItems.get(getAdapterPosition()).getPts_price());
							double sumWithoutCurrent = getSumOfAllFieldsWithoutSpecificPosition(getAdapterPosition());
							Log.e("Current Sum ", "afterTextChanged: "+currentTotal );
							Log.e("Without Current Sum ", "afterTextChanged: "+sumWithoutCurrent );
							double grandTotal = currentTotal+sumWithoutCurrent;
							Log.e("Total Sum ", "afterTextChanged: "+grandTotal );
							edtNewBusiness.setText(AppUtils.getRoundupLastTwoDigits(grandTotal));
						}
						catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}
	
	
}
