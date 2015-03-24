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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Wesley Lin on 3/24/15.
 */
public class Utils {

    public static String getHashMD5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger bi = new BigInteger(1, md.digest(string.getBytes()));
            return bi.toString(16);
        } catch (NoSuchAlgorithmException ex) {

            return "";
        }
    }

    /**
     * Format a int value to String
     * @param value the int value you want to format
     * @param length the return String's length
     * @return the string formatted, fill "0" if the value String's length is smaller than {@param length}
     */
    public static String formatCalendarIntValue(int value, int length) {
        String valueString = String.valueOf(value);
        while (valueString.length() < length) {
            valueString = "0" + valueString;
        }
        return valueString;
    }
}
