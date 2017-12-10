package org.projects.shoppinglist;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anders on 01-12-2017.
 */

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Log.d("firebase", "persistance enabled");
        } else
            Log.d("firebase", "persistance not enabled");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}