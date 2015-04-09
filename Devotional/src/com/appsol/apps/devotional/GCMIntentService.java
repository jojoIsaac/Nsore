package com.appsol.apps.devotional;

import static com.appsol.apps.projectcommunicate.classes.CommonUtilities.SENDER_ID;
import static com.appsol.apps.projectcommunicate.classes.CommonUtilities.displayMessage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.ServerUtilities;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        //displayMessage(context, "Your device registred","App-status-Registration");
        Log.d("NAME", "");
        ServerUtilities.register(context, Connector.getUserId(), Connector.ID, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
       // displayMessage(context, getString(R.string.gcm_unregistered),"App-status-UNRegister");
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * @return 
     * */
    
    
    void handleMessages(String type,String message,Context context)
    {
    	
    	String alert="";
    	try {
			JSONObject alertobject= new JSONObject(message);
			alert= alertobject.optString("alert");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
        // notifies user
        generateNotification(context, alert,message,type);
    }
    
    @Override
    protected void onMessage(Context context, Intent intent) {
       
        try
        {
        	 Log.i(TAG, "Received message");
             String message = intent.getExtras().getString("message");
             String type= intent.getExtras().getString("Type");
        	 handleMessages(type, message,context);
             displayMessage(context, message,type);
        }
        catch(Exception e)
        {
        	
        }
       
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message,"App-status-Message-deleted");
        // notifies user
       // generateNotification(context, message,"");
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
       // displayMessage(context, getString(R.string.gcm_error, errorId),"App-status-ERR");
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
       // displayMessage(context, getString(R.string.gcm_recoverable_error,
              //  errorId),"App-status-ERR");
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     * @param alert 
     */
    private static void generateNotification(Context context,  String alert,String message,String type) {
        int icon = R.drawable.nd;
        long when = System.currentTimeMillis();
        
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.appico)
        .setContentTitle("Nsore Devotional")
        .setContentText(alert);
        

        
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("MESSAGE", message);
        resultIntent.putExtra("TYPE",type);
        resultIntent.putExtra("FC", 1);
        
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
        

        
//        
//        NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(icon, message, when);
//        
//        String title = context.getString(R.string.app_name);
//        
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        notificationIntent.putExtra("MESSAGE", message);
//        notificationIntent.putExtra("TYPE",type);
//        // set intent so it does not start a new activity
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent =
//                PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, title, message, intent);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        
//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//        
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
//        
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        notificationManager.notify(0, notification);      

    }

}
