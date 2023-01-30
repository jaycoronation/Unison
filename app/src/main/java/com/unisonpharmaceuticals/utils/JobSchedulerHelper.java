package com.unisonpharmaceuticals.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * Created by Kiran Patel on 08-Oct-18.
 */
public class JobSchedulerHelper
{
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void startJobForPeriodicTask(Context context, Class<?> serviceClass, int JOB_ID, long DURATION)
    {
        try {
            ComponentName componentName = new ComponentName(context, serviceClass);
            JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                    .setPeriodic(DURATION)
                    .setPersisted(true)
                    .build();
            JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            int resultCode = 0;
            if (jobScheduler != null) {
                resultCode = jobScheduler.schedule(jobInfo);
            }
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.e("JOB >>> "+JOB_ID, "Job scheduled!");
            } else {
                Log.e("JOB >>> "+JOB_ID, "Job not scheduled");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isJobServiceRunning(Context context, int JOB_ID )
    {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;
        boolean hasBeenScheduled = false ;
        if (scheduler != null) {
            for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
                if ( jobInfo.getId() == JOB_ID ) {
                    hasBeenScheduled = true ;
                    break ;
                }
            }
        }

        return hasBeenScheduled ;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void cancelJob(Context context, int JOB_ID, boolean isCancelAllJobs)
    {
        try {
            JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE );
            if(scheduler!=null)
            {
                if(isCancelAllJobs)
                {
                    scheduler.cancelAll();
                }
                else
                {
                    scheduler.cancel(JOB_ID);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void cancelAllJobs(Context context)
    {
        try {
            JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE );
            jobScheduler.cancelAll();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
