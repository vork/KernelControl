package com.vork.KernelControl.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vork.KernelControl.R;

public class Helper implements Preferences {

    /**
     * Restart the activity smoothly
     *
     * @param activity
     */
    public static void restartKC(final Activity activity) {
        if (activity == null)
            return;
        final int enter_anim = android.R.anim.fade_in;
        final int exit_anim = android.R.anim.fade_out;
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.finish();
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.startActivity(activity.getIntent());
    }

    public static void setTheme(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        int color = preferences.getInt(ACCENT_COLOR_PREF, activity.getResources().getColor(R.color.accentBlue));

        if (color == activity.getResources().getColor(R.color.accentPurple)) {
           activity.setTheme(R.style.PurpleLight);
        } else if (color == activity.getResources().getColor(R.color.accentGreen)) {
           activity.setTheme(R.style.GreenLight);
        } else if (color == activity.getResources().getColor(R.color.accentOrange)) {
           activity.setTheme(R.style.OrangeLight);
        } else if (color == activity.getResources().getColor(R.color.accentRed)) {
           activity.setTheme(R.style.RedLight);
        } else { // Blue
           activity.setTheme(R.style.BlueLight);
        }
    }
}
