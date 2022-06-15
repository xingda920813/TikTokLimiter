package me.xd.tiktoklimiter;

import android.app.ActivityManager;
import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.AudioManager;

import java.util.Calendar;

import me.xd.task.DelayProvider;
import me.xd.task.PeriodicTaskService;

public class App extends Application {

    private static final String[] PACKAGES = {
            "com.dw.btime",
            "com.xunmeng.pinduoduo",
            "com.ss.android.article.news",
            "com.ss.android.ugc.aweme"
    };

    @Override
    public void onCreate() {
        super.onCreate();
        PeriodicTaskService.init(Icon.createWithBitmap(Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)), this::runOnce,
                fromAlarmManager -> DelayProvider.DAILY);
    }

    void runOnce() {
        if (!validateTime()) {
            killApps();
            return;
        }
        startLauncher();
        killApps();
        lockScreen();
        mute();
        killApps();
    }

    private static boolean validateTime() {
        final Calendar cal = Calendar.getInstance();
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int minute = cal.get(Calendar.MINUTE);
        if (hour == 0) {
            return minute <= 30;
        } else if (hour == 23) {
            return minute >= 30;
        }
        return false;
    }

    private void startLauncher() {
        final Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void killApps() {
        final ActivityManager am = getSystemService(ActivityManager.class);
        for (final String pkg : PACKAGES) {
            am.killBackgroundProcesses(pkg);
        }
    }

    private void lockScreen() {
        try {
            getSystemService(DevicePolicyManager.class).lockNow();
        } catch (final Exception ignored) {}
    }

    private void mute() {
        getSystemService(AudioManager.class).setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
}
