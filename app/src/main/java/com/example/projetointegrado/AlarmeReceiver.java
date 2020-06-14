package com.example.projetointegrado;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static android.app.AlarmManager.RTC_WAKEUP;
import static com.example.projetointegrado.App.CHANNEL_ID;

public class AlarmeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Intent fullScreenIntent = new Intent(context, ActivityAlarmeAtivo.class);
        int notificationId = intent.getIntExtra("NOTIFICATION_ID", 0);
        fullScreenIntent.putExtra("NOTIFICATION_ID", notificationId);

        int alarmType = intent.getIntExtra("ALARM_TYPE", 0);
        if (alarmType != 0) {
            if (alarmType == 1) {
                int horas = intent.getIntExtra("ALARM_HOUR", 0);
                int minutos = intent.getIntExtra("ALARM_MINUTES", 0);
                int[] dias = intent.getIntArrayExtra("ALARM_DAYS");

                Calendar calendar = Calendar.getInstance();

                int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
                int tomorrow = weekDay == 7 ? 0 : weekDay + 1;

                boolean isDiasEmpty = true;

                for (int i = 0; i < dias.length; i++) {
                    if (dias[i] == 1) isDiasEmpty = false;
                }

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                Calendar nextNotifTime = Calendar.getInstance();
                nextNotifTime.add(Calendar.MONTH, 1);
                nextNotifTime.set(Calendar.DATE, 1);
                nextNotifTime.add(Calendar.DATE, -1);

                if(day == nextNotifTime.get(Calendar.DAY_OF_MONTH)){
                    if(month == 11){
                        year = year + 1;
                        month = 0;
                    }
                    else{
                        day = 1;
                        month = month + 1;
                    }
                } else {
                    day = day + 1;
                }

                calendar.set(year, month, day, horas, minutos, 0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent newIntent = new Intent(context, AlarmeReceiver.class);
                newIntent.putExtra("NOTIFICATION_ID", notificationId);
                newIntent.putExtra("ALARM_TYPE", 1);
                newIntent.putExtra("ALARM_HOUR", horas);
                newIntent.putExtra("ALARM_MINUTES", minutos);
                newIntent.putExtra("ALARM_DAYS", dias);

                if (dias[tomorrow] == 1 || isDiasEmpty) {
                    newIntent.putExtra("MUST_PLAY_NOTIFICATION", true);
                }

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, newIntent, 0);
                alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else if (alarmType == 2) {
                //TODO IMPLEMENT INTERVAL ALARM REPEATING
            }
        }

        if (intent.getBooleanExtra("MUST_PLAY_NOTIFICATION", false)) {
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                    fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setContentText(context.getString(R.string.notification_text))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .build();

            notificationManagerCompat.notify(notificationId, notification);
        }
    }
}
