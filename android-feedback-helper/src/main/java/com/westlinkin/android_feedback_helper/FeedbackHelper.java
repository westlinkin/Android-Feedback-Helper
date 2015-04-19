/*
 * Copyright 2015 Wesley Lin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.westlinkin.android_feedback_helper;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.westlinkin.android_feedback_helper.sender.EmailSender;
import com.westlinkin.android_feedback_helper.ui.FeedbackDialog;

/**
 * Created by Wesley Lin on 3/17/15.
 */
public class FeedbackHelper {

    private static final String TAG = FeedbackHelper.class.getSimpleName();
    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize FeedbackHelper which had already been initialized before. "
            + "To re-init FeedbackHelper with new feedback email address call FeedbackHelper.destroy() at first.";

    private static FeedbackHelper instance;

    private Configuration configuration;

    public static FeedbackHelper getInstance() {
        if (instance == null) {
            synchronized (FeedbackHelper.class) {
                if (instance == null) {
                    instance = new FeedbackHelper();
                }
            }
        }
        return instance;
    }

    public FeedbackHelper() {}

    /**
     * Initializes FeedbackHelper instance with feedback email address.<br />
     * If feedback eamil address was set before ( {@link #isInited()} == true) then this method does nothing.<br />
     * To force initialization with new feedback email address you should {@linkplain #destroy() destroy ImageLoader} at first.
     *
     * @param configuration {@linkplain Configuration FeedbackHelper configuration}
     */
    public synchronized void init(Configuration configuration) {
        if (this.configuration == null) {
            this.configuration = configuration;
        } else {
            Log.w(TAG, WARNING_RE_INIT_CONFIG);
        }
    }

    /**
     * Returns <b>true</b> - if FeedbackHelper is initialized; <b>false</b> - otherwise
     */
    public boolean isInited() {
        return configuration != null;
    }

    /**
     * You can {@linkplain #init(Configuration) init} FeedbackHelper with new feedback email address after calling this
     * method.
     */
    public void destroy() {
        if (configuration != null) {
            configuration = null;
        }
    }

    /**
     * Show a feedback dialog, let user to send feedback messages
     * @param activity The Activity where the send feedback action happens
     * @param appName The name of the App
     * @param showToast whether show a toast after the feedback message was sent
     * @param addAttachment whether add the content of the feedback, including device information as attachment
     */
    public void showFeedbackDialog(final Activity activity, final String appName, final boolean showToast, final boolean addAttachment) {
        FeedbackDialog feedbackDialog = FeedbackDialog.getInstance(configuration.getEmailDomains());

        feedbackDialog.setOnDialogButtonsClickListener(new FeedbackDialog.OnDialogButtonsClickListener() {
            @Override
            public void onSendClicked(String feedbackMessage, String userEmailAddress) {
                new SendFeedbackTask(activity, showToast).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        appName, feedbackMessage, userEmailAddress);
            }
        });
        feedbackDialog.setStyle(DialogFragment.STYLE_NORMAL, configuration.getFeedbackTheme());
        feedbackDialog.show(activity.getFragmentManager(), "feedback-dialog");
    }

    private class SendFeedbackTask extends AsyncTask<String, Void, Boolean> {
        private Activity activity;
        private boolean showToast;

        public SendFeedbackTask(Activity activity, boolean showToast) {
            this.activity = activity;
            this.showToast = showToast;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String appName = params[0];
            String msg = params[1];
            String userEmail = params[2];

            EmailSender emailSender = new EmailSender(activity);

            try {
                emailSender.send(appName, msg, userEmail, configuration.getFeedbackEmailAddress());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!showToast)
                return;
            if (aBoolean) {
                Toast.makeText(activity, activity.getString(R.string.afh_feedback_sent), Toast.LENGTH_SHORT).show();;
            } else {
                Toast.makeText(activity, activity.getString(R.string.afh_feedback_sent_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
