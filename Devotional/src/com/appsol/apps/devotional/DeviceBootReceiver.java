package com.appsol.apps.devotional;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeviceBootReceiver extends BroadcastReceiver {
	private PendingIntent pendingIntent;

	public DeviceBootReceiver() {
	}

	 @Override
	    public void onReceive(Context context, Intent intent) {
	        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
	            /* Setting the alarm here */

//	            Intent alarmIntent = new Intent(context, AlarmScheduler.class);
//
//	            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//
//	            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//	            int interval = 8000;
//
//	            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

	        	
	        	Intent alarmIntent = new Intent(context, AlarmScheduler.class);
	            pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
	    		 AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    	        int interval = 1000 * 60 * 20;

	    	        /* Set the alarm to start at 10:30 AM */
	    	        Calendar calendar = Calendar.getInstance();
	    	        calendar.setTimeInMillis(System.currentTimeMillis());
	    	        calendar.set(Calendar.HOUR_OF_DAY, 4);
	    	        calendar.set(Calendar.MINUTE, 10);
	    	        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
	    	        /* Repeating on every 20 minutes interval */
	    	        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
	    	                AlarmManager.INTERVAL_DAY, pendingIntent);
	        	
	        	
	            //Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
	        }
	    }
}
