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

package com.vork.KernelControl.Activities.Base.Abstract;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.negusoft.holoaccent.AccentHelper;
import com.negusoft.holoaccent.dialog.AccentAlertDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.stericson.RootTools.RootTools;
import com.vork.KernelControl.ActivityViewGroup;
import com.vork.KernelControl.R;
import com.vork.KernelControl.Utils.Constants;
import com.vork.KernelControl.Utils.Helper;
import com.vork.KernelControl.Utils.Preferences;

public abstract class AbstractBaseActivity extends FragmentActivity implements Constants, Preferences {
    protected ViewGroup mViewGroupContent;
    private SharedPreferences mPreferences;

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
        mViewGroupContent = ActivityViewGroup.get(this);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setNavigationBarAlpha(0);
        int accentColor = getResources().getColor(R.color.action_bar_color);
        tintManager.setStatusBarTintColor(accentColor);

        boolean firstRun = mPreferences.getBoolean(FIRST_RUN_PREF, true);
        boolean suDenied = mPreferences.getBoolean(SU_DENIED, false);
        boolean suAvailable = RootTools.isRootAvailable();

        if((firstRun || suDenied) && suAvailable) {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(FIRST_RUN_PREF, false);
            editor.commit();
            launchFirstRunDialog();
        } else if (firstRun && !suAvailable) { // Device isn't rooted
            String title = getString(R.string.device_no_su_title);
            String message = getString(R.string.device_no_su_message);

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(SU_DENIED, true);
            editor.commit();

            suCheckResultDialog(title, message);
        }

        Crashlytics.start(this);
    }

    private void launchFirstRunDialog() {
        String title = getString(R.string.first_run_title);
        String message = getString(R.string.first_run_message);
        String grant = getString(R.string.first_run_grant);
        String deny = getString(R.string.first_run_deny);

        AccentAlertDialog.Builder builder = new AccentAlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(grant, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean checkSu = RootTools.isAccessGiven();
                if(checkSu) {
                    String title = getString(R.string.su_success_title);
                    String message = getString(R.string.su_success_message);

                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(SU_DENIED, false);
                    editor.commit();

                    suCheckResultDialog(title, message);
                } else {
                    String title = getString(R.string.su_failed_title);
                    String message = getString(R.string.su_failed_message);

                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(SU_DENIED, true);
                    editor.commit();

                    suCheckResultDialog(title, message);
                }
            }
        });
        builder.setNegativeButton(deny, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = getString(R.string.su_denied_title);
                String message = getString(R.string.su_denied_message);

                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(SU_DENIED, true);
                editor.commit();

                suCheckResultDialog(title, message);
            }
        });
        builder.show();
    }

    private void suCheckResultDialog(String title, String message) {
        AccentAlertDialog.Builder builder = new AccentAlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing. Absolutely nothing..
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
