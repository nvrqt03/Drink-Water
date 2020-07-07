package ajmitchell.android.drinkwater.sync;
/*
This is a helper class for the WaterReminderIntentService - keeps the tasks running in the background
 */

import android.content.Context;

import ajmitchell.android.drinkwater.utilities.NotificationUtils;
import ajmitchell.android.drinkwater.utilities.PreferenceUtilities;


public class ReminderTasks {
    public static String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";

    private static void incrementWaterCount(Context context) {
        PreferenceUtilities.incrementWaterCount(context);
        NotificationUtils.clearAllNotifications(context);
    }

    private static void incrementOuncesCount(Context context) {
        PreferenceUtilities.incrementOuncesCount(context);
        NotificationUtils.clearAllNotifications(context);
    }

    public static void executeTask(Context context, String action) {
        if (ACTION_INCREMENT_WATER_COUNT.equals(action)) {
            incrementWaterCount(context);
            incrementOuncesCount(context);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        }
    }
}
