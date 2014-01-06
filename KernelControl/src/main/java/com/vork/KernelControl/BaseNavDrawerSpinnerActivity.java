package com.vork.KernelControl;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;

import com.vork.KernelControl.Adapter.ActionBarSpinnerAdapter;

import java.util.ArrayList;

public abstract class BaseNavDrawerSpinnerActivity extends BaseNavDrawerActivity implements
        ActionBar.OnNavigationListener {
    private ArrayList<SpinnerNavItem> mNavSpinnerItems;
    protected ActionBarSpinnerAdapter mSpinnerAdapter;
    protected int mSelectedSpinnerItem = -1;

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

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        mSelectedSpinnerItem = itemPosition;
        setSelectedItem(((SpinnerNavItem) mSpinnerAdapter.getItem(mSelectedSpinnerItem)).getTitle(),
                ((SpinnerNavItem) mSpinnerAdapter.getItem(mSelectedSpinnerItem)).getSubtitle());
        return false;
    }
}
