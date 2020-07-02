package com.mbetemalu.jokesapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MessageService extends IntentService {
    //Declare a constant KEY to pass a message from the main activity to the service
    public static final String EXTRA_MESSAGE = "MESSAGE";

    //Declare a notification id which is used to identify the notification
    public static final int NOTIFICATION_ID = 1;
    //Create a handler so hat you can post on the main thread

    //To create a handler on the main thread we need to creat a handler object in the method that runs on the main thread
    private Handler handler;


    public MessageService() {
        
        super("MessageService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //This method contains the code we want to run when the service receives an intent
        
        synchronized (this){
            //this synchronized method allows us to lock a particular clock of code from access bt other threads
            try{
                //wait for 10 secs
                wait(1000);
            }catch (InterruptedException error){
                error.printStackTrace();
            }
            
            //the try and catch allows us to perform code actions on the try block and catch error exceptioms in the catch block, hence making us able to trace the line of code that has errors during debugging
        }
        //Get the text from the intent
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    private void showText(final String text) {
        //this is shown in the logcat
        Log.v("DelayedMessageService", "What is the secret of comedy?:" + text);

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(this, MainActivity.class);

        //The task stack builder makes sure that when the back button is pressed it will play nicely when the activity is started
        //The task stack builder allows you to access the history of activities used by the back button
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        //The get pending intent method specifies the pending intent's behaviour
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        //This allows you to create a notification with specific features
        Notification notification = new Notification.Builder(this)
                //This displays a small notification icon
        .setSmallIcon(R.mipmap.ic_joke_round)
                //This sets the title of the application
        .setContentTitle(getString(R.string.app_name))
                //set content text
        .setContentText(text)
                //makes the notification disappear when clicked
        .setAutoCancel(true)
                //Gives maximum priority to allow peeking
        .setPriority(Notification.PRIORITY_MAX)
                //Set it to vibrate to get a large head-up notification
        .setDefaults(Notification.DEFAULT_VIBRATE)
                //Open the main Activity on clicking the notification
        .setContentIntent(pendingIntent)
                .build();

        //Issuing the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,notification);
    }


    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
