package vn.nms.photo.app.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import vn.nms.photo.app.BuildConfig;
import vn.nms.photo.app.Constant;
import vn.nms.photo.app.R;
import vn.nms.photo.app.services.local.SharedPrefs;
import com.squareup.leakcanary.LeakCanary;

import java.util.Calendar;

import timber.log.Timber;

public class BaseApp extends Application implements Application.ActivityLifecycleCallbacks {

    private static BaseApp appContext;
    private Gson mGSon;

    public static BaseApp getAppContext() {
        return appContext;
    }

    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    public static boolean isAppBackground;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        registerActivityLifecycleCallbacks(this);
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not initialize your app in this process.
                return;
            }
            Timber.plant(new Timber.DebugTree());
            LeakCanary.install(this);
        } else {
            Fabric.with(this, new Crashlytics());
        }
        MobileAds.initialize(this, getString(R.string.ad_unit_id));
        if (SharedPrefs.getInstance().get(Constant.PREF_TIME_INSTALL, Long.class) == 0) {
            SharedPrefs.getInstance().put(Constant.PREF_TIME_INSTALL,
                    Calendar.getInstance().getTimeInMillis());
        }
    }

    public Gson getGSon() {
        if (mGSon == null) {
            mGSon = new Gson();
        }
        return mGSon;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground
            isAppBackground = false;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            isAppBackground = true;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


}
