package com.advelit.one;

import android.util.Base64;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.provider.Settings;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageInfo;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;

import com.getcapacitor.Plugin;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;

@CapacitorPlugin(name = "AdvelitOne")
public class AdvelitOnePlugin extends Plugin {
    public static final String PREFS = "autostart";
    public static final String ACTIVITY_CLASS_NAME = "class";
    public static final String SERVICE_CLASS_NAME = "service";

    private final AdvelitOne implementation = new AdvelitOne();

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void executeSuAction(PluginCall call, String command) {
        Runtime runtime = Runtime.getRuntime();
        Process localProcess = null;
        OutputStreamWriter osw = null;

        try {
            localProcess = runtime.exec("su");
            osw = new OutputStreamWriter(localProcess.getOutputStream());
            osw.write(command);
            osw.flush();
            osw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            call.reject(ex.getLocalizedMessage(), null, ex);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    call.reject(e.getLocalizedMessage(), null, e);
                }
            }
        }
        try {
            if (localProcess != null) {
                localProcess.waitFor();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            call.reject(e.getLocalizedMessage(), null, e);
        }
        if (localProcess != null && localProcess.exitValue() == 0) {
            call.resolve(new JSObject().put("status", true));
        }
    }

    private void setAutoStart(final String className, boolean enabled, boolean isService, PluginCall call) {
        try {
            Context context = getActivity().getApplicationContext();
            int componentState;
            SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (enabled) {
                componentState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
                final String preferenceKey = isService ? SERVICE_CLASS_NAME : ACTIVITY_CLASS_NAME;
                editor.putString(preferenceKey, className);
            } else {
                componentState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                editor.remove(ACTIVITY_CLASS_NAME);
                editor.remove(SERVICE_CLASS_NAME);
            }
            editor.commit();
            ComponentName bootCompletedReceiver = new ComponentName(context, BootCompletedReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(bootCompletedReceiver, componentState, PackageManager.DONT_KILL_APP);
            call.resolve(new JSObject().put("status", true));
        } catch (Exception e) {
            e.printStackTrace();
            call.reject(e.getLocalizedMessage(), null, e);
        }
    }

    @PluginMethod
    public void isDebuggingOpen(PluginCall call) {
        try {
            boolean isEnabled = Settings.Global.getInt(getActivity().getContentResolver(), Settings.Global.ADB_ENABLED, 0) != 0;
            call.resolve(new JSObject().put("status", isEnabled));
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            call.reject(e.getLocalizedMessage(), null, e);
        }
    }

    @PluginMethod
    public void openDebugging(PluginCall call) {
        try {
            boolean status = Settings.Global.putInt(getActivity().getContentResolver(), Settings.Global.ADB_ENABLED, 1);
            call.resolve(new JSObject().put("status", status));
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            call.reject(e.getLocalizedMessage(), null, e);
        }
    }

    @PluginMethod
    public void closeDebugging(PluginCall call) {
        try {
            boolean status = Settings.Global.putInt(getActivity().getContentResolver(), Settings.Global.ADB_ENABLED, 0);
            call.resolve(new JSObject().put("status", status));
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            call.reject(e.getLocalizedMessage(), null, e);
        }
    }

    @PluginMethod
    public void toggleAutoStart(PluginCall call) {
        final Boolean enabled = call.getBoolean("enabled");
        final String serviceClassName = call.getString("serviceClassName");

        if (enabled && !Boolean.getBoolean(serviceClassName)) {
            setAutoStart(getActivity().getLocalClassName(), true, false, call);
        }

        if (enabled && Boolean.getBoolean(serviceClassName)) {
            setAutoStart(serviceClassName, true, true, call);
        }

        if (!enabled) {
            setAutoStart(null, false, false, call);
        }
    }

    @PluginMethod
    public void runCommand(PluginCall call) {
        final String command = call.getString("command");

        if (Boolean.getBoolean(command)) {
            executeSuAction(call, command);
        }
    }

    @PluginMethod
    public void removeLauncher(PluginCall call) {
        String[] knownLaunchers = {
            "com.yxt.mboxlaunchernew",
        };

        for (String knownLauncher : knownLaunchers) {
            executeSuAction(call, "pm uninstall -k --user 0 " + knownLauncher);
        }
    }

    @PluginMethod
    public void reboot(PluginCall call) {
        executeSuAction(call, "/system/bin/reboot");
    }

    @PluginMethod
    public void ethernetUp(PluginCall call) {
        executeSuAction(call, "ifconfig eth0 up");
    }

    @PluginMethod
    public void ethernetDown(PluginCall call) {
        executeSuAction(call, "ifconfig eth0 down");
    }

    @PluginMethod
    public void getInstalledApps(PluginCall call) {
        try {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageManager packageManager = getActivity().getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(mainIntent, 0);
            JSONArray applicationsList = new JSONArray();
            for (ResolveInfo resolveInfo : resolveInfoList) {
                PackageInfo pInfo = packageManager.getPackageInfo(resolveInfo.activityInfo.packageName, 0);
                String packageName = resolveInfo.activityInfo.packageName;
                String packageVersion = pInfo.versionName;
                CharSequence intentLabel = resolveInfo.loadLabel(packageManager);
                Drawable appIcon = resolveInfo.loadIcon(packageManager);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = drawableToBitmap(appIcon);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] intentImageBytes = baos.toByteArray();
                String intentIconBase64 = Base64.encodeToString(intentImageBytes, Base64.DEFAULT);
                intentIconBase64 = "data:image/png;base64, " + intentIconBase64;
                JSONObject intentInfo = new JSONObject();
                intentInfo.put("label", intentLabel);
                intentInfo.put("package", packageName);
                intentInfo.put("version", packageVersion);
                intentInfo.put("packageIcon", intentIconBase64);
                applicationsList.put(intentInfo);
            }
            call.resolve(new JSObject().put("data", applicationsList));
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            call.reject(e.getLocalizedMessage(), null, e);
        }
    }
}
