package com.vork.KernelControl.Activities.Base.Abstract;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.negusoft.holoaccent.AccentHelper;
import com.vork.KernelControl.ActivityViewGroup;
import com.vork.KernelControl.Utils.Constants;
import com.vork.KernelControl.Utils.Helper;

public abstract class AbstractBaseActivity extends FragmentActivity implements Constants {
    protected boolean mDarkUi = false;
    protected ViewGroup mViewGroupContent;

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

        Crashlytics.start(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
