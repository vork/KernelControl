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

package com.vork.KernelControl;

import android.app.Application;


public class KernelControl extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // The following line triggers the initialization of ACRA
/*        if (!BuildConfig.DO_DEBUG) {
            Crashlytics.start(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String name = preferences.getString("account_name", "");
            String mail = preferences.getString("account_pref", SettingsFragment.NO_ACCOUNT_NAME);
            if (!mail.equals(SettingsFragment.NO_ACCOUNT_NAME)) {
                Crashlytics.setUserEmail(mail);
                if (!name.equals("")) {
                    Crashlytics.setUserName(name);
                }
            }

        }*/
    }
}
