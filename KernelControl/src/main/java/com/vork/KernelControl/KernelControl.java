/*
 * Copyright 2013 Benedikt Boss
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
