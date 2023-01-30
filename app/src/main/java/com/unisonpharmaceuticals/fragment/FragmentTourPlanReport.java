package com.unisonpharmaceuticals.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.unisonpharmaceuticals.activity.WebViewActivity;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.MonthResponse;
import com.unisonpharmaceuticals.model.StaffResponse;
import com.unisonpharmaceuticals.model.YearResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTourPlanReport extends Fragment implements View.OnClickListener
{
    private Activity activity ;
    private SessionManager sessionManager;
    private View rootView ;

    private ApiInterface apiService;

	@BindView(R.id.llLoading) LinearLayout llLoading;
    @BindView(R.id.inputEmployee)
	TextInputLayout inputEmployee;
	@BindView(R.id.edtEmployee) EditText edtEmployee;

	@BindView(R.id.edtMonth)
	EditText edtMonth;
	@BindView(R.id.edtYear)
	EditText edtYear;

	@BindView(R.id.llGenerateReport) LinearLayout llGenerateReport;
	@BindView(R.id.tvCancel)TextView tvCancel;

	private Dialog listDialog;
	private ArrayList<StaffResponse.StaffBean> listEmployee = new ArrayList<>();
	private ArrayList<StaffResponse.StaffBean> listEmployeeSearch = new ArrayList<>();

	private List<YearResponse.YearListBean> listYear = new ArrayList<>();
	private ArrayList<MonthResponse.MonthListBean> listMonth = new ArrayList<>();

	private final String EMPLOYEE = "Employee";
	private final String MONTH = "Month";
	private final String YEAR = "Year";
	private String selectedStaffId = "";

	public static String strDate = "";

	//DatePicker
	private Calendar calendar;
	private String monthNumber = "";
	public static int year = 2000, month = 1 , day = 1;
	public  static boolean flagDate = false ;

	private boolean isLoading = false;

	private long mLastClickTime = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
		rootView = inflater.inflate(R.layout.fragment_tourplan_report, container, false);
		activity = getActivity();
		ButterKnife.bind(this,rootView);
		sessionManager = new SessionManager(activity);
		apiService = ApiClient.getClient().create(ApiInterface.class);

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		initViews();
		getEmployeeList();
		return rootView ;
    }

	private void initViews()
	{
		edtEmployee.setOnClickListener(this);
		llGenerateReport.setOnClickListener(this);
		edtMonth.setOnClickListener(this);
		edtYear.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
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
			case R.id.edtEmployee:
				if(isLoading)
				{
					AppUtils.showLoadingToast(activity);
				}
				else
				{
					showListDialog(EMPLOYEE,edtEmployee,0);
				}
				break;
			case R.id.llGenerateReport:
				if(selectedStaffId.equals(""))
				{
					AppUtils.showToast(activity,"Please select employee");
				}
				else if(edtYear.getText().toString().equals(""))
				{
					AppUtils.showToast(activity,"Please select year");
				}
				else if(monthNumber.equals(""))
				{
					AppUtils.showToast(activity,"Please select month");
				}
				else
				{
					generateReport();
				}
				break;
			case R.id.edtMonth:
				if(isLoading)
				{
					AppUtils.showLoadingToast(activity);
				}
				else
				{
					if(edtYear.getText().toString().equalsIgnoreCase(""))
					{
						AppUtils.showToast(activity,"Please select year");
					}
					else
					{
						showListDialog(MONTH,new EditText(activity),0);
					}
				}
				break;
			case R.id.edtYear:
				if(isLoading)
				{
					AppUtils.showLoadingToast(activity);
				}
				else
				{

					showListDialog(YEAR,new EditText(activity),0);
				}
				break;
			case R.id.tvCancel:
				edtEmployee.setText("");
				edtYear.setText("");
				edtMonth.setText("");
				selectedStaffId = "";
				monthNumber = "";
				break;
		}
	}

	private void generateReport()
	{
		String url = ApiClient.TOURPLANMAIN_REPORT + "staff_id="+selectedStaffId+"&month="+monthNumber+"&"+"year="+edtYear.getText().toString()+"&session_id="+sessionManager.getUserId();
		Intent intent = new Intent(activity,WebViewActivity.class);
		intent.putExtra("cameFrom",ApiClient.REPORT_TP);
		intent.putExtra("report_url",url);
		startActivity(intent);
	}

	private void getEmployeeList()
	{
		isLoading = true;
		llLoading.setVisibility(View.VISIBLE);
		listEmployee = new ArrayList<>();
		Call<StaffResponse> empCall = apiService.getStaffMembers(sessionManager.getUserId(),sessionManager.getUserId());
		empCall.enqueue(new Callback<StaffResponse>() {
			@Override
			public void onResponse(Call<StaffResponse> call, Response<StaffResponse> response)
			{
				try {
					if(response.body().getSuccess()==1)
					{
						listEmployee = (ArrayList<StaffResponse.StaffBean>) response.body().getStaff();

						if(listEmployee.size()==1)
						{
							selectedStaffId = listEmployee.get(0).getStaff_id();
							edtEmployee.setText(listEmployee.get(0).getName());
						}
					}
					else
					{
						AppUtils.showToast(activity, "Could not get employee list.");
						activity.finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				llLoading.setVisibility(View.GONE);
				isLoading = false;
			}

			@Override
			public void onFailure(Call<StaffResponse> call, Throwable t)
			{
				isLoading = false;
				AppUtils.showToast(activity, "Could not get employee list.");
				activity.finish();
			}
		});

		llLoading.setVisibility(View.GONE);

		isLoading = true;
		Call<YearResponse> yearCall = apiService.getLastYearList("",sessionManager.getUserId());
		yearCall.enqueue(new Callback<YearResponse>() {
			@Override
			public void onResponse(Call<YearResponse> call, Response<YearResponse> response)
			{
				if(response.body().getSuccess()==1)
				{
					listYear = response.body().getYearList();
					if(listYear.size()==1)
					{
						edtYear.setText(String.valueOf(listYear.get(0).getYear()));
						getMonthData();
					}
				}
				else
				{
					AppUtils.showToast(activity,"No year data found");
				}
				llLoading.setVisibility(View.GONE);
				isLoading = false;
			}

			@Override
			public void onFailure(Call<YearResponse> call, Throwable t) {
				AppUtils.showToast(activity,"No year data found");
				llLoading.setVisibility(View.GONE);
				isLoading = false;
			}
		});

		llLoading.setVisibility(View.GONE);
	}

	private void getMonthData()
	{
		isLoading = true;
		llLoading.setVisibility(View.VISIBLE);
		Call<MonthResponse> yearCall = apiService.getListMonth("",sessionManager.getUserId());
		yearCall.enqueue(new Callback<MonthResponse>() {
			@Override
			public void onResponse(Call<MonthResponse> call, Response<MonthResponse> response)
			{
				listMonth.clear();
				listMonth = (ArrayList<MonthResponse.MonthListBean>) response.body().getMonthList();
				llLoading.setVisibility(View.GONE);
				isLoading = false;
			}

			@Override
			public void onFailure(Call<MonthResponse> call, Throwable t) {
				AppUtils.showToast(activity,"No year data found");
				llLoading.setVisibility(View.GONE);
				isLoading = false;
			}
		});
	}

	DialogListAdapter listDialogAdapter;
	public void showListDialog(final String isFor, final EditText editText, final int listPostion)
	{
		listDialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

		listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
		listDialog.setContentView(sheetView);

		listDialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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

		btnNo.setVisibility(View.GONE);
		
		final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

		listDialogAdapter = new DialogListAdapter(listDialog, isFor,false,"",editText,listPostion,rvListDialog);
		rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
		rvListDialog.setAdapter(listDialogAdapter);

		btnNo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				listDialog.dismiss();
				listDialog.cancel();
			}
		});

		final EditText edtSearchDialog = (EditText) listDialog.findViewById(R.id.edtSearchDialog);
		final TextInputLayout inputSearch = (TextInputLayout) listDialog.findViewById(R.id.inputSearch);

		if(isFor.equalsIgnoreCase(MONTH))
		{
			inputSearch.setVisibility(View.GONE);
		}
		else
		{
			inputSearch.setVisibility(View.VISIBLE);
		}

		listDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				AppUtils.hideKeyboard(edtEmployee,activity);
			}
		});

		edtSearchDialog.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
			{
				int textlength = edtSearchDialog.getText().length();

				if(isFor.equals(EMPLOYEE))
				{
					listEmployeeSearch.clear();
					for (int i = 0; i < listEmployee.size(); i++)
					{
						if (textlength <= listEmployee.get(i).getName().length())
						{
							if (listEmployee.get(i).getName().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
							{
								listEmployeeSearch.add(listEmployee.get(i));
							}
						}
					}
				}
				AppendListForArea(listDialog,isFor,true,rvListDialog);
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
		EditText editText;
		int listPosition = 0;

		DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,RecyclerView recyclerView)
		{

			this.isFor = isFor;
			this.dialog = dialog;
			this.isForSearch = isForSearch;
			this.searchText = searchText;
		}
		DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,EditText editText,int listPosition,RecyclerView recyclerView)
		{

			this.isFor = isFor;
			this.dialog = dialog;
			this.isForSearch = isForSearch;
			this.searchText = searchText;
			this.editText = editText;
			this.listPosition = listPosition;
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
			if(isFor.equals(EMPLOYEE))
			{
				holder.cb.setVisibility(View.GONE);
				final StaffResponse.StaffBean getSet;
				if(isForSearch)
				{
					getSet = listEmployeeSearch.get(position);
				}
				else
				{
					getSet = listEmployee.get(position);
				}

				holder.tvValue.setText(getSet.getName());
				holder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						edtEmployee.setText(getSet.getName());
						selectedStaffId = getSet.getStaff_id();
						dialog.dismiss();
						dialog.cancel();
					}
				});
			}
			else if(isFor.equalsIgnoreCase(MONTH))
			{
				holder.cb.setVisibility(View.GONE);
				holder.tvValue.setText(listMonth.get(position).getMonth());
				holder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						edtMonth.setText(listMonth.get(position).getMonth());
						dialog.dismiss();
						dialog.cancel();
						monthNumber =listMonth.get(position).getNumber();
					}
				});
			}
			else if(isFor.equalsIgnoreCase(YEAR))
			{
				holder.cb.setVisibility(View.GONE);
				holder.tvValue.setText(String.valueOf(listYear.get(position).getYear()));
				holder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						edtYear.setText(String.valueOf(listYear.get(position).getYear()));
						getMonthData();
						dialog.dismiss();
						dialog.cancel();
					}
				});
			}


		}

		@Override
		public int getItemCount()
		{
			if(isFor.equalsIgnoreCase(EMPLOYEE))
			{
				if(isForSearch)
				{
					return listEmployeeSearch.size();
				}
				else
				{
					return listEmployee.size();
				}
			}
			else if(isFor.equalsIgnoreCase(MONTH))
			{
				return listMonth.size();
			}
			else if(isFor.equalsIgnoreCase(YEAR))
			{
				return listYear.size();
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

	private void AppendListForArea(Dialog dialog,String isFor,boolean isForSearch,RecyclerView rvArea)
	{
		listDialogAdapter = new DialogListAdapter(dialog,isFor,true,"",rvArea);
		rvArea.setAdapter(listDialogAdapter);
		listDialogAdapter.notifyDataSetChanged();
	}
}
