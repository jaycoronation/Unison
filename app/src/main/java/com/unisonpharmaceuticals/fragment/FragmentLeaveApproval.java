package com.unisonpharmaceuticals.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.AppliedLeavesResponse;
import com.unisonpharmaceuticals.model.AppliedLeavesResponse.LeavesBean;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;
import com.zach.salman.springylib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeaveApproval extends Fragment implements View.OnClickListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    private ApiInterface apiService;


    @BindView(R.id.rvAppliedLeaves)
    RecyclerView rvAppliedLeaves;

    @BindView(R.id.llLoading)
    LinearLayout llLoading;

    @BindView(R.id.llNoData)
    LinearLayout llNoData;

    private List<LeavesBean> listLeaves = new ArrayList<>();

    private LeaveAdapter leaveAdapter;

    public FragmentLeaveApproval() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_leave_approval, container, false);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        ButterKnife.bind(this, rootView);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        initViews();
        if(sessionManager.isNetworkAvailable())
        {
            getLeaveData();
        }
        else
        {
            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
        }
        return rootView;
    }

    private void initViews() {
        rvAppliedLeaves.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void getLeaveData()
    {
        llLoading.setVisibility(View.VISIBLE);
        final retrofit2.Call<AppliedLeavesResponse> leaveCall = apiService.getAppliedLeaves(sessionManager.getUserId(),sessionManager.getUserId());
        leaveCall.enqueue(new Callback<AppliedLeavesResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AppliedLeavesResponse> call, Response<AppliedLeavesResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    listLeaves = new ArrayList<>();
                    listLeaves = response.body().getLeaves();

                    leaveAdapter = new LeaveAdapter(listLeaves);
                    rvAppliedLeaves.setAdapter(leaveAdapter);

                    llNoData.setVisibility(View.GONE);
                }
                else
                {
                    llNoData.setVisibility(View.VISIBLE);
                    AppUtils.showToast(activity,response.body().getMessage());

                }

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(retrofit2.Call<AppliedLeavesResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder>
    {
        List<AppliedLeavesResponse.LeavesBean> listItems;

        private SpringyAdapterAnimator mAnimator;

        LeaveAdapter(List<AppliedLeavesResponse.LeavesBean> list)
        {
            this.listItems = list;
            mAnimator = AppUtils.springLibAnimForRecyclerView(mAnimator,rvAppliedLeaves);
        }

        @Override
        public LeaveAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_applied_leaves, viewGroup, false);
            mAnimator.onSpringItemCreate(v);
            return new LeaveAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final LeaveAdapter.ViewHolder holder, final int position)
        {
            final AppliedLeavesResponse.LeavesBean getSet = listItems.get(position);
            holder.txtLeave.setText(getSet.getLeave());
            holder.txtStatus.setText(getSet.getStatus());
            if(getSet.getStatus().equalsIgnoreCase("Approved"))
            {
                holder.txtStatus.setTextColor(ContextCompat.getColor(activity,R.color.app_green));
                holder.itemView.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
            }
            else if(getSet.getStatus().equalsIgnoreCase("Rejected"))
            {
                holder.txtStatus.setTextColor(ContextCompat.getColor(activity,R.color.app_red));
                holder.itemView.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
            }
            else if(getSet.getStatus().equalsIgnoreCase("Pending"))
            {
                holder.txtStatus.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                holder.itemView.setBackgroundColor(ContextCompat.getColor(activity,R.color.light_primary));
            }
            mAnimator.onSpringItemBind(holder.itemView,position);
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(getSet.getStatus().equalsIgnoreCase("Approved"))
                    {
                        AppUtils.showToast(activity,"Leave already approved!");
                    }
                    else if(getSet.getStatus().equalsIgnoreCase("Rejected"))
                    {
                        AppUtils.showToast(activity,"Leave rejected!");
                    }
                    else if(getSet.getStatus().equalsIgnoreCase("Pending"))
                    {
                        showListDialog(position,getSet);
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.txtLeave)TextView txtLeave;
            @BindView(R.id.txtStatus)TextView txtStatus;
            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }

    }
    BottomSheetDialog listDialog;
    public void showListDialog(final int listPosition, final AppliedLeavesResponse.LeavesBean getSet)
    {
        listDialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);

        listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_leave_accept, null);
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
        tvTitle.setText("Leave Request Action");

        TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);
        btnNo.setVisibility(View.GONE);

        LinearLayout llApprove,llReject;
        llApprove = (LinearLayout) listDialog.findViewById(R.id.llApprove);
        llReject = (LinearLayout) listDialog.findViewById(R.id.llReject);

        llApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
                updateLeaveStatus(listPosition,"approved",getSet.getLeave_id());
            }
        });

        llReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                listDialog.cancel();
                updateLeaveStatus(listPosition,"rejected",getSet.getLeave_id());
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        listDialog.show();
    }

    private void updateLeaveStatus(int pos,String status,String leaveId)
    {
        llLoading.setVisibility(View.VISIBLE);
        final retrofit2.Call<CommonResponse> leaveCall = apiService.updateLeaveStatus(leaveId,status,sessionManager.getUserId(),sessionManager.getUserId());
        leaveCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(retrofit2.Call<CommonResponse> call, Response<CommonResponse> response)
            {
                if(response.body().getSuccess()==1)
                {
                    AppUtils.showToast(activity,response.body().getMessage());
                    getLeaveData();
                }
                else
                {
                    llNoData.setVisibility(View.VISIBLE);
                    AppUtils.showToast(activity,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<CommonResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
            }
        });
        llLoading.setVisibility(View.GONE);
    }
}
