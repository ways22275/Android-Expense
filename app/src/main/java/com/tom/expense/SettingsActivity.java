package com.tom.expense;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private static final int JOB_ID = 20;
    public static final SimpleDateFormat SDF =
            new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private static final int REQUEST_NOTIFCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean reminder = sharedPreferences.getBoolean(key, false);
        Log.d(TAG, "onSharedPreferenceChanged: " + key + " : " + reminder);
        if (key.equals("pref_reminders")) {
            JobScheduler jobScheduler =
                    (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(JOB_ID,
                    new ComponentName(getPackageName(), MyJobService.class.getName()))
                    .setPeriodic(60 * 1000)
                    .setPersisted(true)
                    .build();
//                new ComponentName("com.tom.expense", "MyJobService"))
            jobScheduler.schedule(jobInfo);
        }
        if (key.equals("pref_testing")){
            boolean enabled = PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean(key, false);
            String time = "13:00";
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                Date date = sdf.parse(time);
                cal.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar alarm = Calendar.getInstance();
            alarm.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            alarm.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            Calendar now = Calendar.getInstance();
            if (alarm.before(now)){
                alarm.add(Calendar.DATE, 1);
            }
            Log.d(TAG, "time: " + SDF.format(alarm.getTime()));
            AlarmManager alarmManager =
                    (AlarmManager) getSystemService(ALARM_SERVICE);

            Intent intent = new Intent(this, NotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this,
                    REQUEST_NOTIFCATION,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            if (enabled) {
                alarmManager.setExact(AlarmManager.RTC, alarm.getTimeInMillis(), pendingIntent);
            }else{
                alarmManager.cancel(pendingIntent);
            }
        }
    }
}



