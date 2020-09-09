package com.org.coffeeshop.Utils;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.org.coffeeshop.MyApplication;

/**
 * Created by arvind on 13/05/19
 */
public class Preferences {
    /**
     * @param iKey   save the passed string in preference corresponding the key
     * @param iValue value of the string to be stored
     */
    public static void saveStringPreferencessync(String iKey, String iValue) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getsApplicationContext()).edit().putString(iKey, iValue).apply();
    }

    /**
     * @param iKey   save the passed string in preference corresponding the key
     * @param iValue value of the string to be stored
     */
    public static void saveIntPreferencessync(String iKey, int iValue) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getsApplicationContext()).edit().putInt(iKey, iValue).apply();
    }

    /**
     * Returns the current language saved in preferences
     *
     * @param iKey key to check in the preference
     * @return string stored with the key passed OR default value when key not available in preference
     */
    public static String getStringPreferences(String iKey) {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getsApplicationContext()).getString(iKey, "");
    }

    /**
     * Returns the current language saved in preferences
     *
     * @param iKey key to check in the preference
     * @return string stored with the key passed OR default value when key not available in preference
     */
    public static int getIntPreferences(String iKey) {
        Context c=MyApplication.getsApplicationContext();
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getsApplicationContext()).getInt(iKey,0);
    }

}
