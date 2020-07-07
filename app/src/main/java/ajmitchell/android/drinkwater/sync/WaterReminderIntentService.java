package ajmitchell.android.drinkwater.sync;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/*
This class is for running tasks in the background. uses the helper class ReminderTasks to run in background.
 */

public class WaterReminderIntentService extends IntentService {
    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}
