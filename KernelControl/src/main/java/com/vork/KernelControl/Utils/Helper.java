package com.vork.KernelControl.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vork.KernelControl.Activities.CPU;
import com.vork.KernelControl.R;

import junit.framework.Assert;

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
        activity.startActivity(new Intent(activity, CPU.class).
                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    public static int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    public static void setTheme(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        int color = preferences.getInt(ACCENT_COLOR_PREF, activity.getResources().getColor(R.color.accentBlue));
        boolean darkUI = preferences.getBoolean("dark_ui_switch", false);

        if (color == activity.getResources().getColor(R.color.accentPurple)) {
            if (darkUI)
                activity.setTheme(R.style.KC_Dark_Purle);
            else
                activity.setTheme(R.style.KC_Light_Purple);
        } else if (color == activity.getResources().getColor(R.color.accentGreen)) {
            if (darkUI)
                activity.setTheme(R.style.KC_Dark_Green);
            else
                activity.setTheme(R.style.KC_Light_Green);
        } else if (color == activity.getResources().getColor(R.color.accentOrange)) {
            if (darkUI)
                activity.setTheme(R.style.KC_Dark_Orange);
            else
                activity.setTheme(R.style.KC_Light_Orange);
        } else if (color == activity.getResources().getColor(R.color.accentRed)) {
            if (darkUI)
                activity.setTheme(R.style.KC_Dark_Red);
            else
                activity.setTheme(R.style.KC_Light_Red);
        } else { // Blue
            if (darkUI)
                activity.setTheme(R.style.KC_Dark_Blue);
            else
                activity.setTheme(R.style.KC_Light_Blue);
        }
    }
}
