package me.xd.tiktoklimiter;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import me.xd.task.PeriodicTaskUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeriodicTaskUtils.startAsForeground(this);
        PeriodicTaskUtils.requestIgnoreBatteryOptimizations(this);
        final ComponentName component = new ComponentName(this, XdDeviceAdminReceiver.class);
        if (!getSystemService(DevicePolicyManager.class).isAdminActive(component)) {
            final Intent i = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component);
            i.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getApplicationInfo().loadLabel(getPackageManager()));
            startActivity(i);
        }
        finish();
    }
}
