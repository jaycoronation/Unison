package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.fragment.FragmentCalendar;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.ViewTourplanResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.unisonpharmaceuticals.utils.MitsUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTourPlanActivity extends BaseClass implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    @BindView(R.id.llBack) LinearLayout llBack;
    @BindView(R.id.rvTourPlan)
    RecyclerView rvTourPlan;
    @BindView(R.id.llNoData)LinearLayout llNoData;
    @BindView(R.id.llLoading)LinearLayout llLoading;
    @BindView(R.id.tvConfirm)TextView tvConfirm;
    @BindView(R.id.tvApprove)TextView tvAppRove;

    private List<ViewTourplanResponse.DaysBean> listDays = new ArrayList<>();
    private String emp_id="",year = "",month = "";

    private boolean showApprove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tour_plan);
        activity = ViewTourPlanActivity.this;
        sessionManager = new SessionManager(activity);
        emp_id = getIntent().getStringExtra("emp_id");
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        showApprove = getIntent().getBooleanExtra("showApprove",false);
        ButterKnife.bind(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        basicProcesses();
    }

    @Override
    public void initViews()
    {
        tvAppRove.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        if(showApprove)
        {
            tvAppRove.setVisibility(View.VISIBLE);
            tvConfirm.setVisibility(View.GONE);
        }
        else
        {
            tvAppRove.setVisibility(View.GONE);
            tvConfirm.setVisibility(View.VISIBLE);
        }

        rvTourPlan.setLayoutManager(new LinearLayoutManager(activity));
        TextView tvTitle = (TextView) findViewById(R.id.txtTitle);
        tvTitle.setText("Tour Plan for "+month+", "+year);
        findViewById(R.id.llLogout).setVisibility(View.GONE);
        findViewById(R.id.llNotification).setVisibility(View.GONE);
    }

    @Override
    public void viewClick() {
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.llBack:
                activity.finish();
                finishActivityAnimation(activity);
                break;
            case R.id.tvConfirm:
                showConfirmDialog();
                break;
            case R.id.tvApprove:
                approveTP();
                break;

        }
    }

    private void approveTP()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> approveGift = apiService.approveTourPlan(month,year,emp_id,sessionManager.getUserId(),sessionManager.getUserId());
        approveGift.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                AppUtils.showToast(activity,response.body().getMessage());
                llLoading.setVisibility(View.GONE);
                if(response.body().getSuccess()==1)
                {
                    if(FragmentCalendar.handler!=null)
                    {
                        Message message = Message.obtain();
                        message.what = 101;
                        FragmentCalendar.handler.sendMessage(message);
                    }
                    activity.finish();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void showConfirmDialog()
    {
        try
        {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txtHeader = dialog.findViewById(R.id.tvHeader);
            TextView txtConfirmation = dialog.findViewById(R.id.tvDescription);

            TextView btnNo = dialog.findViewById(R.id.tvCancel);
            TextView btnYes = dialog.findViewById(R.id.tvConfirm);

            txtHeader.setText("Submit Form");

            txtConfirmation.setText("Are you sure? Once you confirm your Tour Plan, you will not be able to change it later?");

            btnNo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                    dialog.cancel();
                }
            });

            btnYes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (dialog != null)
                    {
                        dialog.dismiss();
                        dialog.cancel();
                    }

                    if (sessionManager.isNetworkAvailable())
                    {
                        confirmTourPlan(emp_id,
                                year,
                                month);
                    }
                    else
                    {
                        AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                    }
                }
            });
            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void confirmTourPlan(final String employee,
                                 final String year,
                                 final String month)
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
                    hashMap.put("employee",employee);
                    hashMap.put("year",year);
                    hashMap.put("month",month);
                    hashMap.put("isMobile","true");
                    hashMap.put("login_user_id",sessionManager.getUserId());

                    Log.e("CONFIRM Request", "doInBackground: " + hashMap.toString() );

                    String response = "";

                    response = MitsUtils.readJSONServiceUsingPOST(ApiClient.CONFIRM_TOURPLAN,hashMap);

                    Log.e("CONFIRM Response >>> ", "doInBackground: " + response );

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
                if(success ==1)
                {
                    if(FragmentCalendar.handler!=null)
                    {
                        Message message = Message.obtain();
                        message.what = 101;
                        FragmentCalendar.handler.sendMessage(message);
                    }
                    activity.finish();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Void)null);
    }


    @Override
    public void getDataFromServer() {
        llLoading.setVisibility(View.VISIBLE);
        Call<ViewTourplanResponse> call = apiService.viewTourPlanEntries(month,year,emp_id,"true");
        call.enqueue(new Callback<ViewTourplanResponse>() {
            @Override
            public void onResponse(Call<ViewTourplanResponse> call, Response<ViewTourplanResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listDays = response.body().getDays();
                    DaysAdapter daysAdapter = new DaysAdapter(listDays);
                    rvTourPlan.setAdapter(daysAdapter);
                }
                else
                {
                    showToast(activity,response.body().getMessage());
                    activity.finish();
                    finishActivityAnimation(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ViewTourplanResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder>
    {

        List<ViewTourplanResponse.DaysBean> listItems;

        DaysAdapter(List<ViewTourplanResponse.DaysBean> list) {
            this.listItems = list;
        }

        @Override
        public DaysAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_tourplan_viewentries, viewGroup, false);
            return new DaysAdapter.ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final DaysAdapter.ViewHolder holder, final int position)
        {
            final ViewTourplanResponse.DaysBean getSet = listItems.get(position);
            if(position==0)
            {
                holder.llTitle.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.llTitle.setVisibility(View.GONE);
            }
            int newPos = position + 1;
            holder.tvSrNo.setText(String.valueOf(newPos));

            if(getSet.getHoliday().equalsIgnoreCase("true"))
            {
                if(getSet.getDay_name().equalsIgnoreCase("Sun"))
                {
                    holder.tvDetails.setText("S");
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_sunday));
                }
                else
                {
                    holder.tvDetails.setText("H");
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_holiday));
                }
            }
            else if(getSet.getLeave().equalsIgnoreCase("true"))
            {
                holder.tvDetails.setText("");
                holder.itemView.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_leave));
            }
            else
            {
                if(getSet.getDay_type()==1)
                {
                    holder.tvDetails.setText("E");
                }
                else
                {
                    holder.tvDetails.setText("");
                }
                holder.itemView.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
            }


            holder.tvArea.setText(getSet.getArea_name());
            //holder.tvCity.setText(getSet.getCity());
            //holder.tvTPName.setText(getSet.getTp_name());
            //holder.tvHorE.setText(getSet.getH_o_e());
            holder.tvDate.setText(AppUtils.getDateCurrentTimeString(getSet.getDate()));
            holder.tvWorkWith.setText(getWorkWithName(position));
            holder.tvFromCity.setText(getSet.getFrom_city());
            holder.tvToCity.setText(getSet.getTo_city());
            holder.tvRemark.setText(getSet.getRemark());
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public String getWorkWithName(int pos)
        {
            String str = "";
            for (int i = 0; i < listItems.get(pos).getWork_with().size(); i++)
            {
                if(str.length()==0)
                {
                    str = listItems.get(pos).getWork_with().get(i).getStaff();
                }
                else
                {
                    str = str + "," + listItems.get(pos).getWork_with().get(i).getStaff();
                }
            }
            return str;
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.llTitle)LinearLayout llTitle;
            @BindView(R.id.tvDetails)TextView tvDetails;
            @BindView(R.id.tvSrNo)TextView tvSrNo;
            @BindView(R.id.tvDate)TextView tvDate;
            //@BindView(R.id.tvTPName)TextView tvTPName;
            //@BindView(R.id.tvCity)TextView tvCity;
            @BindView(R.id.tvArea)TextView tvArea;
            //@BindView(R.id.tvHorE)TextView tvHorE;
            @BindView(R.id.tvWorkWith)TextView tvWorkWith;
            @BindView(R.id.tvFromCity)TextView tvFromCity;
            @BindView(R.id.tvToCity) TextView tvToCity;
            @BindView(R.id.tvRemark)TextView tvRemark;

            ViewHolder(View convertView) {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }

        }
    }
}
