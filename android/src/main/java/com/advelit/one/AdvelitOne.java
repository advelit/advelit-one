package com.advelit.one;

import android.util.Log;

public class AdvelitOne {
    public Void removeLauncher() {
        return null;
    }

    public Void reboot() {
        return null;
    }

    public boolean ethernetUp() {
        return true;
    }

    public boolean ethernetDown() {
        return true;
    }

    public Object toggleAutoStart(Object options) {
        Log.i("toggleAutoStart", String.valueOf(options));
        return options;
    }

    public Void getInstalledApps() {
        return null;
    }
}
