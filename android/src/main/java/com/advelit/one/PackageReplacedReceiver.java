package com.advelit.one;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.advelit.one.AdvelitOnePlugin;

public class PackageReplacedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AppStarter appStarter = new AppStarter();
        appStarter.run(context, intent, AppStarter.BYPASS_USER_PRESENT_MODIFICATION );
    }
}