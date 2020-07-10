package ajmitchell.android.drinkwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ajmitchell.android.drinkwater.sync.ReminderTasks;
import ajmitchell.android.drinkwater.sync.ReminderUtilities;
import ajmitchell.android.drinkwater.sync.WaterReminderIntentService;
import ajmitchell.android.drinkwater.utilities.NotificationUtils;
import ajmitchell.android.drinkwater.utilities.PreferenceUtilities;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mWaterCountDisplay;
    private TextView mChargingCountDisplay;
    private ImageView mChargingImageView;
    private TextView mOuncesCountDisplay;
    private TextView mCupsDisplay;
    private Toast mToast;

    IntentFilter mChargingIntentFilter;
    ChargingBroadcastReceiver mChargingReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaterCountDisplay = findViewById(R.id.tv_water_count);
        mChargingCountDisplay = findViewById(R.id.tv_charging_reminder_count);
        mChargingImageView = findViewById(R.id.iv_power_increment);
        mOuncesCountDisplay = findViewById(R.id.tv_ounces_count);
        mCupsDisplay = findViewById(R.id.tv_cups_count);

//        Set the original values
        updateWaterCount();
        updateOunces();
        updateCups();
        updateChargingReminderCount();

        ReminderUtilities.scheduleChargingReminder(this);

//        setting up the SharedPreferences listener
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        // instantiating the intent filter and charging broadcast receiver
        mChargingIntentFilter = new IntentFilter();
        mChargingReceiver = new ChargingBroadcastReceiver();

        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    // helper function created for testing - below override of onResume was not working with this code. Also did not work by specifying API level
    // however removing the code and simply using the sticky intent causes the correct results - showing the correct charging icon if charging even when
    // app moves to background. docs also say use sticky intent - https://developer.android.com/training/monitoring-device-state/battery-monitoring.html
    public void isCharging() {
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        boolean isCharging = batteryManager.isCharging();
        showCharging(isCharging);
    }

    // setup broadcast receiver
    @Override
    protected void onResume() {
        super.onResume();

//            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
//            boolean isCharging = batteryManager.isCharging();
//            showCharging(isCharging);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent currentBatteryStatusIntent = registerReceiver(null, intentFilter);
        int batteryStatus = currentBatteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING || batteryStatus == BatteryManager.BATTERY_STATUS_FULL;
        showCharging(isCharging);
        registerReceiver(mChargingReceiver, mChargingIntentFilter);
    }

    // override onPause and unregister receiver - won't need unless in the foreground
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChargingReceiver);
    }

    //    Here we need to retrieve values from SharedPreferences and set the values in our UI
//    This updates the textView to display the new water count from SharedPrefs
    private void updateWaterCount() {
        int waterCount = PreferenceUtilities.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount + "");
    }

    //    Updating the ounces display
    private void updateOunces() {
        int ounces = PreferenceUtilities.getOuncesCount(this);
        mOuncesCountDisplay.setText(ounces + "");
    }

    //    Updating the cups display
    private void updateCups() {
        int cups = PreferenceUtilities.getWaterCount(this);
        mCupsDisplay.setText(cups + "");
    }

    //      Updating the charging reminder count from sharedPrefs
    private void updateChargingReminderCount() {
        int charging = PreferenceUtilities.getChargingReminderCount(this);
        String updateChargingReminder = getResources().getQuantityString(R.plurals.charge_notification_count, charging, charging);
        mChargingCountDisplay.setText(updateChargingReminder);
    }

    public void incrementWater(View view) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, R.string.water_chug_toast, Toast.LENGTH_SHORT);
        mToast.show();
        Intent incrementWaterIntent = new Intent(this, WaterReminderIntentService.class);
        incrementWaterIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        startService(incrementWaterIntent);
    }

    private void showCharging(boolean isCharging) {
        if (isCharging) {
            mChargingImageView.setImageResource(R.drawable.ic_power_green_24);
        } else {
            mChargingImageView.setImageResource(R.drawable.ic_power_grey_80);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // must unregister preference change listener once app is destroyed
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PreferenceUtilities.KEY_WATER_COUNT.equals(key)) {
            updateWaterCount();
            updateCups();
            updateOunces();
        } else if (PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT.equals(key)) {
            updateChargingReminderCount();
        }
    }

    private class ChargingBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Boolean isCharging = (action.equals(Intent.ACTION_POWER_CONNECTED));
            showCharging(isCharging);
        }
    }
}