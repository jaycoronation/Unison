package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.fragment.FragmentSalesUpdateNew;
import com.unisonpharmaceuticals.fragment.FragmentSampleUpdateNew;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.DoctorSalesNotifResponse;
import com.unisonpharmaceuticals.model.EmployeeSalesNotifResponse;
import com.unisonpharmaceuticals.model.SalesNotifResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewSampleActivity extends BaseClass implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    @BindView(R.id.llLoading) LinearLayout llLoading;
    @BindView(R.id.edtEmployeeNotif)EditText edtEmployeeNotif;
    @BindView(R.id.edtDoctorNotif)EditText edtDoctorNotif;
    @BindView(R.id.rvItemsNotif)
    RecyclerView rvItemsNotif;
    @BindView(R.id.tvReadNotif)TextView tvReadNotif;

    private Dialog listDialog;

    private NotifAdapter notifAdapter;

    private List<EmployeeSalesNotifResponse.StaffBean> listEmployeeNotif = new ArrayList<>();
    private List<EmployeeSalesNotifResponse.StaffBean> listEmployeeNotifSerach = new ArrayList<>();

    private List<DoctorSalesNotifResponse.StaffBean> listDoctorNotif = new ArrayList<>();
    private List<DoctorSalesNotifResponse.StaffBean> listDoctorNotifSerach = new ArrayList<>();

    private List<SalesNotifResponse.NotificationBean> listNotidications = new ArrayList<>();

    private String selectedStaffIdForNotif="",selectedDrIdFroNotif="";
    private final String NOTIF_EMPLOYEE = "notifEmployee";
    private final String NOTIF_DOCTOR = "notifDoctor";
    private String type = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sample);
        ButterKnife.bind(this);
        activity = ViewSampleActivity.this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        type = getIntent().getStringExtra("type");
        basicProcesses();
    }

    @Override
    public void initViews() {
        rvItemsNotif.setLayoutManager(new LinearLayoutManager(activity));
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                finishActivityAnimation(activity);
            }
        });
        findViewById(R.id.llLogout).setVisibility(View.GONE);
        findViewById(R.id.llNotification).setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.txtTitle);
        if(type.equalsIgnoreCase("sample"))
        {
            tvTitle.setText("Sample Updates");
        }
        else
        {
            tvTitle.setText("Sales Updates");
        }
    }

    @Override
    public void viewClick() {
        edtEmployeeNotif.setOnClickListener(this);
        edtDoctorNotif.setOnClickListener(this);
        tvReadNotif.setOnClickListener(this);
    }

    @Override
    public void getDataFromServer() 
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<EmployeeSalesNotifResponse> empCall = apiService.getEmployeesForNotification(sessionManager.getUserId(),type,sessionManager.getUserId());
        empCall.enqueue(new Callback<EmployeeSalesNotifResponse>() {
            @Override
            public void onResponse(Call<EmployeeSalesNotifResponse> call, Response<EmployeeSalesNotifResponse> response)
            {
                try
                {
                    if(response.body().getSuccess()==1)
                    {
                        listEmployeeNotif = response.body().getStaff();
                    }
                    llLoading.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EmployeeSalesNotifResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.VISIBLE);
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edtEmployeeNotif:
                if(listEmployeeNotif.size()>0)
                {
                    showListDialog(NOTIF_EMPLOYEE);
                }
                else
                {
                    AppUtils.showToast(activity,"No Employees available!");
                }
                break;
            case R.id.edtDoctorNotif:
                if(listDoctorNotif.size()>0)
                {
                    showListDialog(NOTIF_DOCTOR);
                }
                else
                {
                    AppUtils.showToast(activity,"No Doctors available!");
                }
                break;
            case R.id.tvReadNotif:
                if(sessionManager.isNetworkAvailable())
                {
                    if(notifAdapter!=null)
                    {
                        readNotifications(notifAdapter.getNotificationIds());
                    }
                }
                else
                {
                    showToast(activity,activity.getString(R.string.network_failed_message));
                }
                break;
        }
    }


    private void getDoctorsFromEmployeeNotif()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<DoctorSalesNotifResponse> drCall = apiService.getDoctorsForNotification(sessionManager.getUserId(),type,selectedStaffIdForNotif,sessionManager.getUserId());
        drCall.enqueue(new Callback<DoctorSalesNotifResponse>() {
            @Override
            public void onResponse(Call<DoctorSalesNotifResponse> call, Response<DoctorSalesNotifResponse> response)
            {
                try
                {
                    if(response.body().getSuccess()==1)
                    {
                        listDoctorNotif = response.body().getStaff();

                        if(listDoctorNotif.size()==0)
                        {
                            AppUtils.showToast(activity,"No doctor available.");
                            activity.finish();
                            AppUtils.finishActivityAnimation(activity);
                        }
                    }
                    else
                    {
                        activity.finish();
                        AppUtils.finishActivityAnimation(activity);
                    }
                    llLoading.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DoctorSalesNotifResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                activity.finish();
                AppUtils.finishActivityAnimation(activity);
            }
        });
    }

    private void getSalesNotifications()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<SalesNotifResponse> notifCall = apiService.getSalesSampleNotification(sessionManager.getUserId(),type,selectedStaffIdForNotif,selectedDrIdFroNotif,sessionManager.getUserId());
        notifCall.enqueue(new Callback<SalesNotifResponse>() {
            @Override
            public void onResponse(Call<SalesNotifResponse> call, Response<SalesNotifResponse> response)
            {
                try
                {
                    listNotidications = response.body().getNotification();

                    if(listNotidications.size()>0)
                    {
                        rvItemsNotif.setVisibility(View.VISIBLE);
                        tvReadNotif.setVisibility(View.VISIBLE);
                        notifAdapter = new NotifAdapter(listNotidications);
                        rvItemsNotif.setAdapter(notifAdapter);
                    }
                    else
                    {
                        tvReadNotif.setVisibility(View.GONE);
                    }
                    llLoading.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SalesNotifResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });
    }

    private void readNotifications(String ids)
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> readCall = apiService.readSalesSampleNotification(ids,sessionManager.getUserId());
        readCall.enqueue(new Callback<CommonResponse>()
        {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                try
                {
                    if(response.body().getSuccess()==1)
                    {
                        rvItemsNotif.setVisibility(View.GONE);
                        edtDoctorNotif.setText("");
                        selectedDrIdFroNotif = "";

                        if(type.equals("sample"))
                        {
                            if(FragmentSampleUpdateNew.handler!=null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                FragmentSampleUpdateNew.handler.sendMessage(message);
                            }
                        }
                        else
                        {
                            if(FragmentSalesUpdateNew.handler!=null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                FragmentSalesUpdateNew.handler.sendMessage(message);
                            }
                        }

                        getDoctorsFromEmployeeNotif();
                    }

                    llLoading.setVisibility(View.GONE);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
            }
        });
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

                if(isFor.equals(NOTIF_EMPLOYEE))
                {
                    listEmployeeNotifSerach.clear();
                    for (int i = 0; i < listEmployeeNotif.size(); i++)
                    {
                        if (textlength <= listEmployeeNotif.get(i).getEmployee().length())
                        {
                            if (listEmployeeNotif.get(i).getEmployee().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listEmployeeNotifSerach.add(listEmployeeNotif.get(i));
                            }
                        }
                    }
                }
                else if(isFor.equals(NOTIF_DOCTOR))
                {
                    listDoctorNotifSerach.clear();
                    for (int i = 0; i < listDoctorNotif.size(); i++)
                    {
                        if (textlength <= listDoctorNotif.get(i).getDoctor().length())
                        {
                            if (listDoctorNotif.get(i).getDoctor().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()) ||
                                    listDoctorNotif.get(i).getDoctor_id().toLowerCase().contains(edtSearchDialog.getText().toString().toLowerCase().trim()))
                            {
                                listDoctorNotifSerach.add(listDoctorNotif.get(i));
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

    private void AppendList(Dialog dialog, String isFor, boolean isForSearch, RecyclerView rvArea)
    {
        areaAdapter = new DialogListAdapter(dialog,isFor,true,"",rvArea);
        rvArea.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
    }

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>
    {
        String isFor = "";
        Dialog dialog;
        boolean isForSearch = false;
        String searchText = "";

        DialogListAdapter(Dialog dialog,String isFor,boolean isForSearch,String searchText,RecyclerView recyClerView)
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
            if(isFor.equals(NOTIF_EMPLOYEE))
            {
                holder.cb.setVisibility(View.GONE);
                final EmployeeSalesNotifResponse.StaffBean getSet;
                if(isForSearch)
                {
                    getSet = listEmployeeNotifSerach.get(position);
                }
                else
                {
                    getSet = listEmployeeNotif.get(position);
                }
                holder.tvValue.setText(getSet.getEmployee());
                holder.tvId.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        selectedStaffIdForNotif = getSet.getEmployee_id();
                        edtEmployeeNotif.setText(getSet.getEmployee());
                        dialog.dismiss();
                        dialog.cancel();
                        getDoctorsFromEmployeeNotif();
                    }
                });
            }
            else if(isFor.equals(NOTIF_DOCTOR))
            {
                holder.cb.setVisibility(View.GONE);
                final DoctorSalesNotifResponse.StaffBean getSet;
                if(isForSearch)
                {
                    getSet = listDoctorNotifSerach.get(position);
                }
                else
                {
                    getSet = listDoctorNotif.get(position);
                }
                holder.tvValue.setText(getSet.getDoctor());
                holder.tvId.setVisibility(View.VISIBLE);
                holder.tvId.setText(getSet.getDoctor_id());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        selectedDrIdFroNotif = getSet.getDoctor_id();
                        edtDoctorNotif.setText(getSet.getDoctor());
                        dialog.dismiss();
                        dialog.cancel();
                        getSalesNotifications();
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if(isFor.equalsIgnoreCase(NOTIF_EMPLOYEE))
            {
                if(isForSearch)
                {
                    return listEmployeeNotifSerach.size();
                }
                else
                {
                    return listEmployeeNotif.size();
                }
            }
            else if(isFor.equalsIgnoreCase(NOTIF_DOCTOR))
            {
                if(isForSearch)
                {
                    return listDoctorNotifSerach.size();
                }
                else
                {
                    return listDoctorNotif.size();
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

    public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder>
    {

        List<SalesNotifResponse.NotificationBean> listItems;


        NotifAdapter(List<SalesNotifResponse.NotificationBean> list) {
            this.listItems = list;
        }

        @Override
        public NotifAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_sample_notification, viewGroup, false);
            return new NotifAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final NotifAdapter.ViewHolder holder, final int position)
        {
            final SalesNotifResponse.NotificationBean getSet = listItems.get(position);
            holder.tvDate.setText(getSet.getDate());
            if(type.equalsIgnoreCase("sale"))
            {
                holder.llBusinessDetails.setVisibility(View.VISIBLE);
                if(getSet.getNew_business().equals(""))
                {
                    holder.tvNewBusNotif.setText("0");
                }
                else
                {
                    holder.tvNewBusNotif.setText(getSet.getNew_business());
                }
                if(getSet.getOld_business().equals(""))
                {
                    holder.tvOldBusNotif.setText("0");
                }
                else
                {
                    holder.tvOldBusNotif.setText(getSet.getOld_business());
                }
            }
            else
            {
                holder.llBusinessDetails.setVisibility(View.GONE);
            }
            NotifItemAdapter adapter = new NotifItemAdapter(getSet.getSamples());
            holder.rvItems.setAdapter(adapter);
        }

        private String getNotificationIds()
        {
            StringBuilder commaSepValueBuilder = new StringBuilder();
            if(listItems.size()>0)
            {
                for ( int i = 0; i< listItems.size(); i++)
                {
                    //append the value into the builder
                    commaSepValueBuilder.append(listItems.get(i).getNotification_id());

                    //if the value is not the last element of the list
                    //then append the comma(,) as well
                    if ( i != listItems.size()-1)
                    {
                        commaSepValueBuilder.append(",");
                    }
                }

                return commaSepValueBuilder.toString().trim();
            }
            else
            {
                return "";
            }
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.llHeader)
            LinearLayout llHeader;

            @BindView(R.id.llBusinessDetails)
            LinearLayout llBusinessDetails;

            @BindView(R.id.tvDate)
            TextView tvDate;

            @BindView(R.id.tvNewBusNotif)
            TextView tvNewBusNotif;

            @BindView(R.id.tvOldBusNotif)
            TextView tvOldBusNotif;

            @BindView(R.id.rvItems)
            RecyclerView rvItems;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
                rvItems.setLayoutManager(new LinearLayoutManager(activity));
            }
        }
    }

    public class NotifItemAdapter extends RecyclerView.Adapter<NotifItemAdapter.ViewHolder> {

        List<SalesNotifResponse.NotificationBean.SamplesBean> listItems;


        NotifItemAdapter(List<SalesNotifResponse.NotificationBean.SamplesBean> list) {
            this.listItems = list;
        }

        @Override
        public NotifItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_sample_notif_item, viewGroup, false);
            return new NotifItemAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final NotifItemAdapter.ViewHolder holder, final int position)
        {
            final SalesNotifResponse.NotificationBean.SamplesBean getSet = listItems.get(position);
            holder.tvItem.setText(getSet.getItem());
            holder.tvOldQty.setText(getSet.getOld_qty());
            holder.tvNewQty.setText(getSet.getNew_qty());
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tvItem)
            TextView tvItem;

            @BindView(R.id.tvOldQty)
            TextView tvOldQty;

            @BindView(R.id.tvNewQty)
            TextView tvNewQty;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }
    }
}
