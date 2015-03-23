/*
 * Copyright 2015. Wesley Lin
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

package com.westlinkin.android_feedback_helper.utils;

import java.util.Date;
import java.util.IllegalFormatException;

/**
 * Created by Wesley Lin on 3/23/15.
 */
public class MailUntils {

    // mail subject, need app_name and case number to format
    private static final String MAIL_SUBJECT = "Feedback about %s #%s";

    public static String getMailSubject(String appName) {
        try {
            return String.format(MAIL_SUBJECT, appName, String.valueOf(new Date().getTime()));
        } catch (IllegalFormatException e) {
            return String.format(MAIL_SUBJECT, "Unknown app", "0000");
        }
    }

    // todo: fill the processor
    public static String emailPwdProcessor(String pwdInValue) {
        return "";
    }

    // todo: fill the processor
    public static String emailNameProcessor(String nameInValue) {
        return "";
    }
}
