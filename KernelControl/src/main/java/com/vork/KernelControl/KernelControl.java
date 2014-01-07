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
