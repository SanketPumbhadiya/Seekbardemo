package com.example.seekbardemoimagedemo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesClass {

    public final String PrefName = "my-Pref";
    public final String Key_GridRowConfig = "GridRowConfig";
    public final String Key_GridColumnConfig = "SeekbarProgress";
    public final String Key_CountrySpinner = "SelectCountry";

    SharedPreferences sharedPref;

    public SharedPreferencesClass(Context context) {
        sharedPref = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
    }

    public void putGridRowConfigShowImage(boolean gridRowConfig) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Key_GridRowConfig, gridRowConfig);
        editor.apply();
    }

    public boolean getGridRowImageConfig() {
        return sharedPref.getBoolean(Key_GridRowConfig, true);
    }

    public void putGridColumnConfigProgress(int seekbar) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Key_GridColumnConfig, seekbar);
        editor.apply();
    }

    public int getGridColumnProgressConfig() {
        return sharedPref.getInt(Key_GridColumnConfig, -1);
    }

    public boolean isGridRowCheckKey() {
        return sharedPref.contains(Key_GridRowConfig);
    }

    public boolean isGridColumnCheckKey() {
        return sharedPref.contains(Key_GridColumnConfig);
    }

    public boolean isSpinnerItemCheckKey() {
        return sharedPref.contains(Key_CountrySpinner);
    }

    public void putSpinnerItem(String item) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Key_CountrySpinner, item);
        editor.apply();
    }

    public String getSpinnerItem() {
        return sharedPref.getString(Key_CountrySpinner, "CANADA");
    }
}