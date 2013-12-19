package com.vork.KernelControl.Settings;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.negusoft.holoaccent.AccentHelper;
import com.vork.KernelControl.R;
import com.vork.KernelControl.Utils.Helper;

import java.util.List;

public class AppSettings extends PreferenceActivity {

    //For HoloAccent
    private final AccentHelper mAccentHelper = new AccentHelper();

    @Override
    public Resources getResources() {
        return mAccentHelper.getResources(this, super.getResources());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Helper.setTheme(this);
        super.onCreate(savedInstanceState);

        final ActionBar bar = getActionBar();

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected boolean isValidFragment(String fragmentName) {
        if (AppSettingsFragment.class.getName().equals(fragmentName))
            return true;
        return false;

    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings_headers, target);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkUI = preferences.getBoolean("dark_ui_switch", false);
        if (darkUI) {
            for(Header header : target) {
                if (header.titleRes == R.string.theme_settings) {
                    header.iconRes = Helper.getDrawable(this, "ic_action_theme_dark");
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Crashlytics.log("In Settings");
    }
}
