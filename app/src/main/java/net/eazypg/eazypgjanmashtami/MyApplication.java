package net.eazypg.eazypgjanmashtami;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();


        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}
