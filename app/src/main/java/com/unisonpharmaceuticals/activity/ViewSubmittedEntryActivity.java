package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.SubmittedResponse;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
import com.unisonpharmaceuticals.utils.AppUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewSubmittedEntryActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvPending)
    RecyclerView rvPending;

    @BindView(R.id.llLoading)
    LinearLayout llLoading;

    @BindView(R.id.llNoData)
    LinearLayout llNoData;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    @BindView(R.id.llRemarks)
    LinearLayout llRemarks;

    @BindView(R.id.tvRemarks)
    TextView tvRemarks;

    @BindView(R.id.llAdvice)
    LinearLayout llAdvice;

    @BindView(R.id.tvAdvice)
    TextView tvAdvice;
    
    private ArrayList<SubmittedResponse.ReportBean.DataBean> listData = new ArrayList<>();

    private EntryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_submited_entry);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        ButterKnife.bind(this);
        basicProcesses();
    }

    @Override
    public void initViews()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("View Submitted Entry");
        findViewById(R.id.llLogout).setVisibility(View.GONE);
        findViewById(R.id.llNotification).setVisibility(View.GONE);
        findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                finishActivityAnimation(activity);
            }
        });
        tvNoData.setText("No Submitted entry found for today!");

        rvPending = (RecyclerView) findViewById(R.id.rvPending);
        rvPending.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void viewClick() {

    }

    @Override
    public void getDataFromServer()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<SubmittedResponse> dataCall = apiService.getSubmittedEntry(AppUtils.currentDateForApi(), sessionManager.getUserId(),sessionManager.getUserId());
        dataCall.enqueue(new Callback<SubmittedResponse>() {
            @Override
            public void onResponse(Call<SubmittedResponse> call, Response<SubmittedResponse> response) 
            {
                try
                {
                    if (response.body().getSuccess() == 1) 
                    {
                        listData = (ArrayList<SubmittedResponse.ReportBean.DataBean>) response.body().getReport().getData();

                        if(listData.size()>0)
                        {
                            llNoData.setVisibility(View.GONE);
                            adapter = new EntryAdapter(listData);
                            rvPending.setAdapter(adapter);
                        }

                        if(response.body().getReport().getRemark()!=null && response.body().getReport().getRemark().size()>0)
                        {
                            llRemarks.setVisibility(View.VISIBLE);
                            String remark = "";
                            int pos = 0;
                            for (int i = 0; i < response.body().getReport().getRemark().size(); i++)
                            {
                                pos = pos + 1;
                                if(remark.equalsIgnoreCase(""))
                                {
                                    remark = pos+". " +response.body().getReport().getRemark().get(i).getRemark() + "\n";
                                }
                                else
                                {
                                    remark = remark + pos +". " +response.body().getReport().getRemark().get(i).getRemark() + "\n";
                                }
                            }

                            Log.e("STRING >>> ", "onResponse: "+remark );

                            tvRemarks.setText(remark);
                        }
                        else
                        {
                            llRemarks.setVisibility(View.GONE);
                        }

                        if(response.body().getReport().getAdvice()!=null && response.body().getReport().getAdvice().size()>0)
                        {
                            llAdvice.setVisibility(View.VISIBLE);
                            String remark = "";
                            int pos = 0;
                            for (int i = 0; i < response.body().getReport().getAdvice().size(); i++)
                            {
                                pos = pos + 1;
                                if(remark.equalsIgnoreCase(""))
                                {
                                    remark = pos+". " +response.body().getReport().getAdvice().get(i).getRemark() + "\n";
                                }
                                else
                                {
                                    remark = remark + pos +". " +response.body().getReport().getAdvice().get(i).getRemark() + "\n";
                                }
                            }

                            Log.e("STRING >>> ", "onResponse: "+remark );

                            tvAdvice.setText(remark);
                        }
                        else
                        {
                            llAdvice.setVisibility(View.GONE);
                        }

                        if(listData.size()==0)
                        {
                            if(response.body().getReport().getRemark()!=null &&
                                    response.body().getReport().getRemark().size()>0 ||
                                    response.body().getReport().getAdvice()!=null &&
                                            response.body().getReport().getAdvice().size()>0
                                    )
                            {
                                llNoData.setVisibility(View.GONE);
                            }
                            else
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        }

                    } else
                    {
                        llNoData.setVisibility(View.VISIBLE);

                        AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                    }

                    llLoading.setVisibility(View.GONE);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SubmittedResponse> call, Throwable t) {
                AppUtils.showToast(activity, activity.getString(R.string.api_failed_message));
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder>
    {
        ArrayList<SubmittedResponse.ReportBean.DataBean> listItems;

        EntryAdapter(ArrayList<SubmittedResponse.ReportBean.DataBean> list)
        {
            this.listItems = list;
        }

        @Override
        public EntryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_view_entry, viewGroup, false);

            return new EntryAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final EntryAdapter.ViewHolder holder, final int position)
        {
            try {
                final SubmittedResponse.ReportBean.DataBean bean = listItems.get(position);

                if(bean.getStatus().equalsIgnoreCase("1"))
                {
                    holder.tvVerify.setVisibility(View.GONE);
                }
                else
                {
                    holder.tvVerify.setVisibility(View.VISIBLE);
                }


                holder.txtDBC.setText(bean.getReport_type());
                if (bean.getReport_type().equals("DCR") ||
                        bean.getReport_type().equals("LCR") ||
                        bean.getReport_type().equals("ACR") ||
                        bean.getReport_type().equals("JCR") ||
                        bean.getReport_type().equals("TNS") ||
                        bean.getReport_type().equals("SRD") ||
                        bean.getReport_type().equals("NCR") ||
                        bean.getReport_type().equals("ROR") ||
                        bean.getReport_type().equals("ROA") ||
                        bean.getReport_type().equals("ZCR")||
                        bean.getReport_type().equals("XCR"))
                {
                    holder.txtWorkWith.setVisibility(View.VISIBLE);
                    holder.txtWorkWith.setText(bean.getWw());
                    holder.txtDoctor.setVisibility(View.VISIBLE);
                }
                else if(bean.getReport_type().equals("INT"))
                {
                    holder.txtWorkWith.setVisibility(View.VISIBLE);
                    holder.txtWorkWith.setText("Internee : "+bean.getNo_of_internee());
                    holder.txtDoctor.setVisibility(View.VISIBLE);
                }
                else if(bean.getReport_type().equals("RMK"))
                {
                    holder.txtWorkWith.setVisibility(View.GONE);
                    holder.txtDoctor.setVisibility(View.GONE);
                }
                else if(bean.getReport_type().equals("ADV"))
                {
                    holder.txtWorkWith.setVisibility(View.GONE);
                }
                else if(bean.getReport_type().equals("STK"))
                {
                    holder.txtWorkWith.setVisibility(View.GONE);
                    holder.txtDoctor.setVisibility(View.GONE);

                }

                //holder.txtDoctor.setText(bean.getDoctor_name()+" ("+bean.getDegree()+")");

                if(bean.getDoctor_id().length() >0)
                {
                    holder.txtDoctor.setText(bean.getDoctor_name()+ " - "+bean.getDoctor_id());
                }
                else
                {
                    holder.txtDoctor.setText(bean.getDoctor_name());
                }


                ProductAdapter adapter = new ProductAdapter((ArrayList<SubmittedResponse.ReportBean.DataBean.ProductsBean>) bean.getProducts());
                holder.rvProducts.setAdapter(adapter);


                if(bean.getFocus_products()!=null && bean.getFocus_products().size()>0)
                {
                    holder.llFocusFor.setVisibility(View.VISIBLE);
                    FocusAdapter focusAdapter = new FocusAdapter((ArrayList<SubmittedResponse.ReportBean.DataBean.FocusProductsBean>) bean.getFocus_products());
                    holder.rvFocus.setAdapter(focusAdapter);
                }
                else
                {
                    holder.llFocusFor.setVisibility(View.GONE);
                }

                holder.tvVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sessionManager.isNetworkAvailable())
                        {
                            showVerifyDialog(bean.getReport_id(),position);
                        }
                        else
                        {
                            AppUtils.showToast(activity,activity.getString(R.string.network_failed_message));
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtDBC,txtDoctor,txtWorkWith,tvVerify;
            private RecyclerView rvProducts,rvFocus;
            private LinearLayout llFocusFor;

            ViewHolder(View convertView)
            {
                super(convertView);

                txtDBC = (TextView) convertView.findViewById(R.id.txtDBC);
                txtDoctor = (TextView) convertView.findViewById(R.id.txtDoctor);
                txtWorkWith = (TextView) convertView.findViewById(R.id.txtWorkWith);
                tvVerify = (TextView) convertView.findViewById(R.id.tvVerify);

                rvProducts = (RecyclerView) convertView.findViewById(R.id.rvProducts);
                rvProducts.setLayoutManager(new LinearLayoutManager(activity));
                rvFocus = (RecyclerView) convertView.findViewById(R.id.rvFocus);
                rvFocus.setLayoutManager(new LinearLayoutManager(activity));
                llFocusFor = (LinearLayout) convertView.findViewById(R.id.llFocusFor);
            }
        }
    }

    private void showVerifyDialog(final String id,final int pos)
    {
        try
        {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity ,R.style.BottomSheetDialogThemeLogout);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View sheetView = activity.getLayoutInflater().inflate(R.layout.bttom_layout_logout, null);
            dialog.setContentView(sheetView);

            TextView txt_Dialog_Delete = (TextView)dialog.findViewById(R.id.tvDescription);
            TextView txtHeader = (TextView)dialog.findViewById(R.id.tvHeader);
            TextView btnNo = (TextView) dialog.findViewById(R.id.tvCancel);
            TextView btnYes = (TextView) dialog.findViewById(R.id.tvConfirm);
            btnYes.setText("Verify");


            txt_Dialog_Delete.setText("Are you sure you want to verify this entry?");
            txtHeader.setText("Verify Entry");


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
                    dialog.dismiss();
                    dialog.cancel();
                    Call<CommonResponse> call = apiService.verifyEntry(id);
                    call.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if(response.isSuccessful())
                            {
                                if(response.body().getSuccess()==1)
                                {
                                    AppUtils.showToast(activity,"Entry Verified!");

                                    SubmittedResponse.ReportBean.DataBean bean = listData.get(pos);
                                    bean.setStatus("1");
                                    listData.set(pos,bean);
                                    adapter.notifyItemChanged(pos);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            AppUtils.showToast(activity,activity.getString(R.string.api_failed_message));
                        }
                    });


                }
            });
            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>
    {
        ArrayList<SubmittedResponse.ReportBean.DataBean.ProductsBean> listItems;

        ProductAdapter(ArrayList<SubmittedResponse.ReportBean.DataBean.ProductsBean> list)
        {
            this.listItems = list;
        }

        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_pendingentry_child, viewGroup, false);

            return new ProductAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ProductAdapter.ViewHolder holder, final int position)
        {
            SubmittedResponse.ReportBean.DataBean.ProductsBean bean = listItems.get(position);
            holder.edtProduct.setText(bean.getItem_id());

            if(bean.getQuantity().equals(""))
            {
                holder.edtQty.setText("0");
            }
            else
            {
                holder.edtQty.setText(bean.getQuantity());
            }
            holder.edtReason.setText(bean.getReason_code());
            if(position==0)
            {
                holder.llTitle.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.llTitle.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private LinearLayout llTitle;
            private View view1;
            private ImageView img_delete;
            private EditText edtProduct,edtQty,edtReason;
            ViewHolder(View convertView)
            {
                super(convertView);

                llTitle = (LinearLayout) convertView.findViewById(R.id.llTitle);
                view1 = convertView.findViewById(R.id.view1);
                view1.setVisibility(View.GONE);
                img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
                img_delete.setVisibility(View.GONE);

                edtProduct = (EditText) convertView.findViewById(R.id.edtProduct_Rowview_Child);
                edtQty = (EditText) convertView.findViewById(R.id.edtQuality_Rowview_Child);
                edtReason = (EditText) convertView.findViewById(R.id.edtReason_Rowview_Child);

            }
        }

    }

    private class FocusAdapter extends RecyclerView.Adapter<FocusAdapter.ViewHolder>
    {
        ArrayList<SubmittedResponse.ReportBean.DataBean.FocusProductsBean> listItems;

        FocusAdapter(ArrayList<SubmittedResponse.ReportBean.DataBean.FocusProductsBean> list)
        {
            this.listItems = list;
        }

        @Override
        public FocusAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_focus, viewGroup, false);

            return new FocusAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final FocusAdapter.ViewHolder holder, final int position)
        {
            SubmittedResponse.ReportBean.DataBean.FocusProductsBean bean = listItems.get(position);
            holder.tvProduct.setText(bean.getFocus_generic());
            holder.tvReason.setText(bean.getFocus_type());
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView tvProduct,tvReason;
            ViewHolder(View convertView)
            {
                super(convertView);

                tvProduct = (TextView) convertView.findViewById(R.id.tvProduct);
                tvReason = (TextView) convertView.findViewById(R.id.tvReason);
            }
        }

    }

    @Override
    protected void onDestroy()
    {
        DashboardActivity.isAppRunning = false;
        super.onDestroy();
    }
}
