package com.vork.KernelControl.Activities.Base.Abstract;

import android.app.ActionBar;

import com.vork.KernelControl.Adapter.ActionBarSpinnerAdapter;

import java.util.ArrayList;

public abstract class AbstractBaseNavDrawerSpinnerActivity extends AbstractBaseNavDrawerActivity implements
        ActionBar.OnNavigationListener {
    protected ActionBarSpinnerAdapter mSpinnerAdapter;
    protected int mSelectedSpinnerItem = -1;

    protected void setupActionBarSpinner(String curTab) {
        final ActionBar actionBar = getActionBar();
        assert actionBar != null;

        ArrayList<String> tabs = (ArrayList<String>) mChildCollection.get(curTab);

        if (tabs.size() > 0) { //Make sure there are tabs
            actionBar.setDisplayShowTitleEnabled(false); //No title - just the spinner
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

            ArrayList<SpinnerNavItem> navSpinnerItems = new ArrayList<SpinnerNavItem>();
            for(String subtitle : tabs) {
                navSpinnerItems.add(new SpinnerNavItem(curTab, subtitle));
            }

            mSpinnerAdapter = new ActionBarSpinnerAdapter(getApplicationContext(), navSpinnerItems, mDarkUi);

            actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
        }
    }

    public void setSelectedTab(int position) {
        final ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.setSelectedNavigationItem(position);
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
