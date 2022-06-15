package me.xd.tiktoklimiter;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.AudioManager;

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
        startLauncher();
        killApps();
        mute();
        killApps();
    }

    private void startLauncher() {
        final Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void killApps() {
        final ActivityManager am = getSystemService(ActivityManager.class);
        for (int i = 0; i < 2; i++) {
            for (final String pkg : PACKAGES) {
                am.killBackgroundProcesses(pkg);
            }
        }
    }

    private void mute() {
        getSystemService(AudioManager.class).setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
}
