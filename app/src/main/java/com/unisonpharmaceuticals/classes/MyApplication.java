package com.unisonpharmaceuticals.classes;

import android.app.Application;

/**
 * Created by Tushar Vataliya on 26-Jun-18.
 */
public class MyApplication extends Application
{

   /* private Database database;
    private static MyApplication  sugarContext;

    private static MyApplication mInstance;
    public static SessionManager sessionManager;

    @Override
    public void onCreate()
    {
        super.onCreate();

        sugarContext = this;
        this.database = new Database(this);

        mInstance = this;
        if(sessionManager==null)
        {
            sessionManager = new SessionManager(getApplicationContext());
        }
    }

    public void onTerminate(){
        if (this.database != null) {
            this.database.getDB().close();
        }
        super.onTerminate();
    }

    public static MyApplication getSugarContext(){
        return sugarContext;
    }

    protected Database getDatabase() {
        return database;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }*/
}
