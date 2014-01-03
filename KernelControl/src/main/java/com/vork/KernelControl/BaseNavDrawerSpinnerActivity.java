package com.vork.KernelControl;

import android.app.ActionBar;

import com.vork.KernelControl.Adapter.ActionBarSpinnerAdapter;

import java.util.ArrayList;

public class BaseNavDrawerSpinnerActivity extends BaseNavDrawerActivity {
    private ArrayList<SpinnerNavItem> mNavSpinnerItems;
    private ActionBarSpinnerAdapter mSpinnerAdapter;

    protected void setupActionBarSpinner(String curTab) {
        final ActionBar actionBar = getActionBar();
        assert actionBar != null;

        ArrayList<String> tabs = (ArrayList<String>) mChildCollection.get(curTab);

        if (tabs.size() > 0) { //Make sure there are tabs
            actionBar.setDisplayShowTitleEnabled(false); //No title - just the spinner
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

            mNavSpinnerItems = new ArrayList<SpinnerNavItem>();
            for(String subtitle : tabs) {
                mNavSpinnerItems.add(new SpinnerNavItem(curTab, subtitle));
            }

            mSpinnerAdapter = new ActionBarSpinnerAdapter(getApplicationContext(), mNavSpinnerItems, mDarkUi);

            actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
        }
    }
}
