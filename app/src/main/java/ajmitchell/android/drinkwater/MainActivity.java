package ajmitchell.android.drinkwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
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
        mOuncesCountDisplay.setText(ounces+"");
    }

//    Updating the cups display
    private void updateCups() {
        int cups = PreferenceUtilities.getWaterCount(this);
        mCupsDisplay.setText(cups+"");
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

}