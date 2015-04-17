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

/**
 * Created by Wesley Lin on 3/23/15.
 */
public class Configuration {
    public static final String DOMAIN_GMAIL = "@gmail.com";
    public static final String DOMAIN_HOTMAIL = "@hotmail.com";
    public static final String DOMAIN_YAHOO = "@yahoo.com";
    public static final String DOMAIN_OUTLOOK = "@outlook.com";
    public static final String DOMAIN_AOL = "@aol.com";

    private String feedbackEmailAddress;
    private String[] emailDomains;
    private int feedbackTheme;

    Configuration(String feedbackEmailAddress) {
        this.feedbackEmailAddress = feedbackEmailAddress;
        setEmailDomains(new String[]{
                DOMAIN_GMAIL,
                DOMAIN_HOTMAIL,
                DOMAIN_YAHOO,
                DOMAIN_OUTLOOK,
                DOMAIN_AOL,
            });

        setFeedbackTheme(0);
    }

    public void setEmailDomains(String[] emailDomains) {
        this.emailDomains = emailDomains;
    }

    public String[] getEmailDomains() {
        return emailDomains;
    }

    /**
     * set FeedbackDialog{@link com.westlinkin.android_feedback_helper.ui.FeedbackDialog} theme
     * @param feedbackTheme the theme of the feedback dialog; if 0, an appropriate theme (based on the style)
     *                      will be selected for your
     */
    public void setFeedbackTheme(int feedbackTheme) {
        this.feedbackTheme = feedbackTheme;
    }

    public int getFeedbackTheme() {
        return feedbackTheme;
    }

    public static Configuration getDefaultConfiguration(String feedbackEmailAddress) {
        return new Configuration(feedbackEmailAddress);
    }
}
