package ajmitchell.android.drinkwater.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import ajmitchell.android.drinkwater.MainActivity;
import ajmitchell.android.drinkwater.R;

public class NotificationUtils {
    public static final int WATER_REMINDER_NOTIFICATION_ID = 1138;
    public static final int WATER_REMINDER_PENDING_INTENT_ID = 3417;
    public static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

//     Creating a helper method that will return a PendingIntent. This method will create the intent that will open the
//     MainActivity and trigger when the notification is pressed.

    private PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

//    Second helper method that will create and return a bitmap we will use to create our notification

    private Bitmap LargeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24);
    }


    // TODO (7) Create a method called remindUserBecauseCharging which takes a Context.
    // This method will create a notification for charging. It might be helpful
    // to take a look at this guide to see an example of what the code in this method will look like:
    // https://developer.android.com/training/notify-user/build-notification.html

    // TODO (8) Get the NotificationManager using context.getSystemService
    //NotificationManager is a service, which is why we're doing the getSystemService

    // TODO (9) Create a notification channel for Android O devices

    // TODO (10) In the remindUserBecauseCharging method use NotificationCompat.Builder to create a notification
    // that:
    // - has a color of R.color.colorPrimary - use ContextCompat.getColor to get a compatible color
    // - has ic_drink_notification as the small icon
    // - uses icon returned by the largeIcon helper method as the large icon
    // - sets the title to the charging_reminder_notification_title String resource
    // - sets the text to the charging_reminder_notification_body String resource
    // - sets the style to NotificationCompat.BigTextStyle().bigText(text)
    // - sets the notification defaults to vibrate
    // - uses the content intent returned by the contentIntent helper method for the contentIntent
    // - automatically cancels the notification when the notification is clicked

    // TODO (11) If the build version is greater than or equal to JELLY_BEAN and less than OREO,
    // set the notification's priority to PRIORITY_HIGH

    // TODO (12) Trigger the notification by calling notify on the NotificationManager.
    // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()
    private static void remindUserBecauseCharging (Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context,
                WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink_notification)
        )
    }






}
