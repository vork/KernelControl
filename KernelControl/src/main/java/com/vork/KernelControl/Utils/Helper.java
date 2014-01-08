/*
 * This file is part of KernelControl.
 *
 *     KernelControl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KernelControl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KernelControl.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.vork.KernelControl.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vork.KernelControl.Activities.CPU;
import com.vork.KernelControl.R;

import junit.framework.Assert;

import hugo.weaving.DebugLog;

public class Helper implements Preferences {

    /**
     * replaces a layout with a fragment and animates the switch
     *
     * @param fragmentManager
     * @param contentId       the layout which will be replaced
     * @param fragment        the fragment the layout is replaced with
     */
    public static void switchFragment(FragmentManager fragmentManager, int contentId, Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(contentId, fragment);
        ft.commit();
    }

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

    @DebugLog
    public static int getDrawable(Context context, String name) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    public static void setTheme(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        int color = preferences.getInt(ACCENT_COLOR_PREF, activity.getResources().getColor(R.color.accentBlue));
        boolean darkUI = preferences.getBoolean(DARK_UI_PREF, false);

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
