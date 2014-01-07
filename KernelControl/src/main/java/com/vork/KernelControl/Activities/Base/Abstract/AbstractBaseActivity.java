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
