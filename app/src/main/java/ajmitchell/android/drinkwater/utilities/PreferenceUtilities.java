package ajmitchell.android.drinkwater.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

/*
This class is for utilities. All of my utility methods that update water, ounces, and charging counts in SharedPreferences
*/
public final class PreferenceUtilities {
    public static final String KEY_WATER_COUNT = "water-count";
    public static final String KEY_OUNCES_COUNT = "ounces-count";
    public static final String KEY_CHARGING_REMINDER_COUNT = "charging-reminder-count";

    public static final int DEFAULT_COUNT = 0;
    public static final int DEFAULT_OUNCES = 0;
    public static final int OUNCES_INCREMENT = 8;

    synchronized private static void setWaterCount(Context context, int glassesOfWater) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_WATER_COUNT, glassesOfWater);
        editor.apply();
    }
    synchronized private static void setOuncesCount(Context context, int ouncesOfWater) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_OUNCES_COUNT, ouncesOfWater);
        editor.apply();
    }

    public static int getWaterCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int glassesOfWater = prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
        return glassesOfWater;
    }

    public static int getOuncesCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int ouncesOfWater = prefs.getInt(KEY_OUNCES_COUNT, DEFAULT_OUNCES);
        return ouncesOfWater;
    }

    synchronized public static void incrementWaterCount(Context context) {
        int waterCount = PreferenceUtilities.getWaterCount(context);
        PreferenceUtilities.setWaterCount(context, ++waterCount);
    }

    synchronized public static void incrementOuncesCount(Context context) {
        int ouncesCount = PreferenceUtilities.getOuncesCount(context);
        PreferenceUtilities.setOuncesCount(context, OUNCES_INCREMENT + ouncesCount);
    }

    synchronized public static void incrementChargingReminderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, ++chargingReminders);
        editor.apply();
    }

    public static int getChargingReminderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);
        return chargingReminders;
    }
}
