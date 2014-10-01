/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import java.io.IOException;
import android.content.Context;
import android.util.AttributeSet;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

public class LedFade extends ListPreference implements OnPreferenceChangeListener {

    public LedFade(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnPreferenceChangeListener(this);
    }

    private static final String FILE_FADE = "/sys/class/sec/led/led_fade";
    private static final String FILE_SPEED= "/sys/class/sec/led/led_speed";

    private static final String METHOD_BLINK = "0";
    private static final String METHOD_FADE = "1";
    private static final String METHOD_BLINKFAST = "2";
    private static final String METHOD_FADEFAST = "3";
    private static final String METHOD_CONT = "4";

    public static boolean isSupported() {
        return Utils.fileExists(FILE_FADE) && Utils.fileExists(FILE_SPEED);
    }

private static void setSysFsForMethod(String method)
    {
        if (method.equals(METHOD_BLINK))
        {
             Utils.writeValue(FILE_FADE, "0\n");
             Utils.writeValue(FILE_SPEED, "1\n");
        } else
        if (method.equals(METHOD_FADE))
        {
             Utils.writeValue(FILE_FADE, "1\n");
             Utils.writeValue(FILE_SPEED, "1\n");
        } else
        if (method.equals(METHOD_BLINKFAST))
        {
             Utils.writeValue(FILE_FADE, "0\n");
             Utils.writeValue(FILE_SPEED, "2\n");
        } else
        if (method.equals(METHOD_FADEFAST))
        {
             Utils.writeValue(FILE_FADE, "1\n");
             Utils.writeValue(FILE_SPEED, "2\n");
        } else
        if (method.equals(METHOD_CONT))
        {
             Utils.writeValue(FILE_FADE, "1\n");
             Utils.writeValue(FILE_SPEED, "0\n");
        }
    }

    /**
     * Restore led fading mode setting from SharedPreferences. (Write to kernel.)
     * @param context       The context to read the SharedPreferences from
     */
    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Utils.writeValue(FILE_FADE, sharedPrefs.getString(DeviceSettings.KEY_LED_FADE, "1"));
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Utils.writeValue(FILE_FADE, (String) newValue);
        return true;
    }

}
