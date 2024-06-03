package com.unisonpharmaceuticals.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unisonpharmaceuticals.R;
import com.unisonpharmaceuticals.classes.HidingScrollListener;
import com.unisonpharmaceuticals.classes.SessionManager;
import com.unisonpharmaceuticals.model.CommonResponse;
import com.unisonpharmaceuticals.model.NotificationResponse;
import com.unisonpharmaceuticals.model.PushNotificationGetSet;
import com.unisonpharmaceuticals.network.ApiClient;
import com.unisonpharmaceuticals.network.ApiInterface;
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

public class NotificationActivity extends BaseClass
{
    private Activity activity;
    private SessionManager sessionManager;
    private String type = "";

    private ApiInterface apiService;

    @BindView(R.id.llLoading)LinearLayout llLoading;
    @BindView(R.id.llNoData)LinearLayout llNoData;
    @BindView(R.id.rvNotification)RecyclerView rvNotification;

    private List<NotificationResponse.NotificationsBean> listNotification = new ArrayList<>();
    private NotificationAdapter notificationAdapter;

    //Paging
    private boolean isLoading = false;
    private int pageIndex = 1;
    private boolean isLastPage = false;
    @BindView(R.id.llLoadMore)LinearLayout llLoadMore;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        try
        {
            type = getIntent().getStringExtra("type");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        basicProcesses();
    }

    @Override
    public void initViews()
    {
        findViewById(R.id.llNotification).setVisibility(View.GONE);
        findViewById(R.id.llLogout).setVisibility(View.GONE);
        findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                finishActivityAnimation(activity);
            }
        });
        TextView txtTitle = findViewById(R.id.txtTitle);

        if( type.equalsIgnoreCase(ApiClient.NOTIF_TP))
        {
            txtTitle.setText("Tour Plan");
        }
        else if(type.equalsIgnoreCase(ApiClient.NOTIF_DCR))
        {
            txtTitle.setText("DCR");
        }
        else if(type.equalsIgnoreCase(ApiClient.NOTIF_GIFT))
        {
            txtTitle.setText("Gift");
        }
        else if(type.equalsIgnoreCase(ApiClient.NOTIF_TA))
        {
            txtTitle.setText("Travelling Allowance");
        }
        else if(type.equalsIgnoreCase(ApiClient.NOTIF_NS))
        {
            txtTitle.setText("Not Seen");
        }
        else if(type.equalsIgnoreCase(ApiClient.NOTIF_OTHER))
        {
            txtTitle.setText("Other");
        }
        else
        {
            txtTitle.setText("Notification");
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        rvNotification.setLayoutManager(linearLayoutManager);

        rvNotification.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide()
            {
                llLoadMore.setVisibility(View.GONE);
            }
            @Override
            public void onShow()
            {
                llLoadMore.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);


                if (dy > 0) {
                    System.out.println("Scrolled Downwards");
                } else if (dy < 0) {
                    System.out.println("Scrolled Upwards");
                    llLoadMore.setVisibility(View.GONE);
                } else {
                    System.out.println("No Vertical Scrolled");
                }

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage)
                {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            loadAllNotifications(false);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void viewClick() {

    }

    @Override
    public void getDataFromServer()
    {
       loadAllNotifications(true);
    }

    private void loadAllNotifications(final boolean isFirstTime)
    {
        if(isFirstTime)
        {
            llLoading.setVisibility(View.VISIBLE);
            llLoadMore.setVisibility(View.GONE);
        }
        else
        {
            llLoadMore.setVisibility(View.VISIBLE);
        }
        isLoading = true;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                if(isFirstTime)
                {
                    listNotification = new ArrayList<>();
                }

                Call<NotificationResponse> notifResponse = apiService.getAllNotifications(sessionManager.getUserId(),String.valueOf(20),String.valueOf(pageIndex),"false","true",sessionManager.getUserId(),type);
                notifResponse.enqueue(new Callback<NotificationResponse>() {
                    @Override
                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response)
                    {
                        if(response.body().getSuccess()==1)
                        {
                            List<NotificationResponse.NotificationsBean> list = response.body().getNotifications();

                            for (int i = 0; i < list.size(); i++)
                            {
                                NotificationResponse.NotificationsBean bean = list.get(i);
                                listNotification.add(bean);
                            }

                            pageIndex = pageIndex + 1;
                            if (list.size() == 0 || list.size() % 20 != 0) {
                                isLastPage = true;
                            }
                        }

                        isLoading = false;
                        activity.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                llLoading.setVisibility(View.GONE);
                                llLoadMore.setVisibility(View.GONE);
                                if(isFirstTime)
                                {
                                    if(listNotification.size()>0)
                                    {
                                        llNoData.setVisibility(View.GONE);
                                        notificationAdapter = new NotificationAdapter();
                                        rvNotification.setAdapter(notificationAdapter);
                                    }
                                    else
                                    {
                                        llNoData.setVisibility(View.VISIBLE);
                                    }
                                }
                                else
                                {
                                    notificationAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<NotificationResponse> call, Throwable t)
                    {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                isLoading = false;
                                llNoData.setVisibility(View.VISIBLE);
                                llLoading.setVisibility(View.GONE);
                                llLoadMore.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        }).start();
    }


    private void updateReadStatus(final NotificationResponse.NotificationsBean getSet, final int pos)
    {
        new Thread(() -> {
            Call<CommonResponse> notifResponse = apiService.readNotifStatusUpdate(sessionManager.getUserId(), getSet.getNotification_id(),sessionManager.getUserId());
            notifResponse.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                {
                    if(response.body().getSuccess()==1)
                    {
                        try
                        {
                            getSet.setIs_read("true");
                            listNotification.set(pos,getSet);
                            notificationAdapter.notifyItemChanged(pos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        showToast(activity,response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t)
                {
                    showToast(activity,"Something went wrong!");
                }
            });
        }).start();
    }

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
    {
        @Override
        public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_notification, viewGroup, false);
            return new NotificationAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final NotificationAdapter.ViewHolder viewHolder, final int i)
        {
            final NotificationResponse.NotificationsBean getSet = listNotification.get(i);

            viewHolder.txtNotificationType.setText(getSet.getTitle());
            viewHolder.txtNotificationDetails.setText(getSet.getMessage());
            viewHolder.txtDate.setText(getSet.getTime());

            if (listNotification.get(i).getIs_read().equals("true"))
            {
                viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            }
            else
            {
                viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_primary));
            }

            /*if(getSet.getImage().equalsIgnoreCase(""))
            {
                viewHolder.ivNotification.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.ivNotification.setVisibility(View.VISIBLE);
            }*/

            viewHolder.itemView.setOnClickListener(v -> {
                if (sessionManager.isNetworkAvailable())
                {
                    if (getSet.getIs_read().equals("false"))
                    {
                        updateReadStatus(getSet,i);
                    }
                }
                else
                {
                    Toast.makeText(activity, "Please check your internet connection.", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listNotification.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtNotificationType,txtNotificationDetails,txtDate;
            /*ImageView ivNotification;*/

            ViewHolder(View v)
            {
                super(v);

                txtNotificationType = (TextView) v.findViewById(R.id.txtTitle);
                txtNotificationDetails = (TextView) v.findViewById(R.id.txtNotificationDetails);
                txtDate = (TextView) v.findViewById(R.id.txtDate);
                /*ivNotification = (ImageView) v.findViewById(R.id.ivNotification);*/
            }
        }










        private void updateNotification(final String notifId, final PushNotificationGetSet getset, final int position)
        {
            (new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected void onPreExecute()
                {
                    llLoading.setVisibility(View.VISIBLE);
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        HashMap<String, String> data = new HashMap<String, String>();

                        data.put("mktCode", sessionManager.getMktCode());
                        data.put("NotificationId", notifId);

                        Log.e("API PARAMS", data + "");

                        String serverResponse = MitsUtils.readJSONServiceUsingPOST(ApiClient.BASE_URL, data);
                        Log.e("ServerResponse", serverResponse+" abcd");

                        try
                        {
                            if (serverResponse != null)
                            {
                                try
                                {
                                    JSONObject jsonObject = new JSONObject(serverResponse);

                                    //strStatus = jsonObject.getString("status");
                                }
                                catch(JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    super.onPostExecute(result);

                    try
                    {
                        llLoading.setVisibility(View.GONE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activity.finish();
        finishActivityAnimation(activity);
    }
}
