package ajmitchell.android.drinkwater.sync;
// TODO (1) Create a class called ReminderTasks

// TODO (2) Create a public static constant String called ACTION_INCREMENT_WATER_COUNT
// TODO (3) Create a private static void method called incrementWaterCount
// TODO (4) Add a Context called context to the argument list
// TODO (5) From incrementWaterCount, call the PreferenceUtility method that will ultimately update the water count
// TODO (6) Create a public static void method called executeTask
// TODO (7) Add a Context called context and String parameter called action to the parameter list

// TODO (8) If the action equals ACTION_INCREMENT_WATER_COUNT, call this class's incrementWaterCount

import android.content.Context;

import ajmitchell.android.drinkwater.utilities.PreferenceUtilities;


public class ReminderTasks {
    public static String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";

    private static void incrementWaterCount(Context context) {
        PreferenceUtilities.incrementWaterCount(context);
    }

    private static void incrementOuncesCount(Context context) {
        PreferenceUtilities.incrementOuncesCount(context);
    }

    public static void executeTask(Context context, String action) {
        if (action == ACTION_INCREMENT_WATER_COUNT) {
            incrementWaterCount(context);
            incrementOuncesCount(context);
        }
    }
}
