package com.advelit.one;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.advelit.one.AdvelitOnePlugin;

import android.util.Log;

public class AppStarter {

    public static final int BYPASS_USER_PRESENT_MODIFICATION = -1;
    private static final String ADVELIT_ONE_AUTOSTART = "advelit_one_autostart";

    public void run(Context context, Intent intent, int componentState) {
        this.run(context, intent, componentState, false);
    }

    public void run(Context context, Intent intent, int componentState, boolean onAutostart) {
        Log.d("AppStarter", "UserPresentReceiver component, new state:" + String.valueOf(componentState));
        if (componentState != BYPASS_USER_PRESENT_MODIFICATION) {
            ComponentName receiver = new ComponentName(context, UserPresentReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver, componentState, PackageManager.DONT_KILL_APP);
        }
        Log.d("AppStarter", "STARTING APP...");
        SharedPreferences sp = context.getSharedPreferences(AdvelitOnePlugin.PREFS, Context.MODE_PRIVATE);
        String packageName = context.getPackageName();
        Log.d("packageName", packageName);
        String activityClassName = sp.getString(AdvelitOnePlugin.ACTIVITY_CLASS_NAME, "");
        if (!activityClassName.equals("")) {
            Log.d("AppStarter", activityClassName);
            Intent activityIntent = new Intent();
            activityIntent.setClassName(context, String.format("%s.%s", packageName, activityClassName));
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (onAutostart) {
                activityIntent.putExtra(ADVELIT_ONE_AUTOSTART, true);
            }
            context.startActivity(activityIntent);
        }
        String serviceClassName = sp.getString(AdvelitOnePlugin.SERVICE_CLASS_NAME, "");
        if (!serviceClassName.equals("")) {
            Intent serviceIntent = new Intent();
            serviceIntent.setClassName(packageName, serviceClassName);
            if (onAutostart) {
                serviceIntent.putExtra(ADVELIT_ONE_AUTOSTART, true);
            }
            context.startService(serviceIntent);
        }

    }
}
