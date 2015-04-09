package com.appsol.apps.devotional;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

public class AlarmScheduler extends BroadcastReceiver {
	public AlarmScheduler() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		//throw new UnsupportedOperationException("Not yet implemented");
		 //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
		generateNotification(context, "Today's Devotion Available.");

	}
	
	
    private static void generateNotification(Context context,  String alert) {
        int icon = R.drawable.nd;
        long when = System.currentTimeMillis();
        
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.appico)
        .setContentTitle("Nsore Devotional")
        .setContentText(alert);
        

        
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("MESSAGE", "Devotion reading");
        resultIntent.putExtra("TYPE","1");
        
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
        		resultIntent.FLAG_ACTIVITY_SINGLE_TOP);
        
     // The stack builder object will contain an artificial back stack for the
     // started Activity.
     // This ensures that navigating backward from the Activity leads out of
     // your application to the Home screen.
     TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
     // Adds the back stack for the Intent (but not the Intent itself)
     stackBuilder.addParentStack(MainActivity.class);
     // Adds the Intent that starts the Activity to the top of the stack
     stackBuilder.addNextIntent(resultIntent);
     PendingIntent resultPendingIntent =
             stackBuilder.getPendingIntent(
                 0,
                 PendingIntent.FLAG_UPDATE_CURRENT
             );
     mBuilder.setContentIntent(resultPendingIntent);
     NotificationManager mNotificationManager =
         (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
     // mId allows you to update the notification later on.
     Notification notification= mBuilder.build();
     notification.defaults |= Notification.DEFAULT_SOUND;
     
     //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
     
     // Vibrate if vibrate is enabled
     notification.defaults |= Notification.DEFAULT_VIBRATE;
     notification.flags |= Notification.FLAG_AUTO_CANCEL;
     mNotificationManager.notify(0, notification);
        
     

    }
	
	
	
}
