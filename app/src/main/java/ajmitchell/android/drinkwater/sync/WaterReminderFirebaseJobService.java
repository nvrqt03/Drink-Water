package ajmitchell.android.drinkwater.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class WaterReminderFirebaseJobService extends JobService {
    private AsyncTask mBackgroundTask;


    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = WaterReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_CHARGING_REMINDER);
                return null;
            }
            /* jobServices needs to tell the system when the job is done by calling jobFinished. The job is finished
            * when the async task is done, and it signals it's done by calling onPostExecute. So we're going to override this
            * and call jobFinished here. the jobParameters(job) are a bunch of key/value arguments that are passed in when the job
            * starts, and needsReschedule represents if the job needs to be rescheduled - which we don't need since it was successful.
            */
            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
                super.onPostExecute(o);
            }
        };
        mBackgroundTask.execute();
        return true; // returning true means that our job is still doing some work
    }

    /* method runs if the requirements of your job are no longer met. if background task is not null,
     * we will cancel. if conditions are met again, job will be retried.
     */
    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
