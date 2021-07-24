package com.hackslash.haaziri.intro;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String ORIGINAL_BLUETOOTH_NAME = "originalBluetoothName";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getOriginalBluetoothName() {
        return pref.getString(ORIGINAL_BLUETOOTH_NAME, "");
    }

    /**
     * This part saves the original bluetooth name of the device
     */
    public void setOriginalBluetoothName(String originalBluetoothName) {
        editor.putString(ORIGINAL_BLUETOOTH_NAME, originalBluetoothName);
        editor.commit();
    }

    public void close() {
        editor.commit();
    }
}
