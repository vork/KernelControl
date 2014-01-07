package com.vork.KernelControl.Activities.Base.Abstract;

import android.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.vork.KernelControl.Adapter.ActionBarSpinnerAdapter;
import com.vork.KernelControl.R;

import java.util.ArrayList;

public abstract class AbstractBaseNavDrawerSpinnerActivity extends AbstractBaseNavDrawerActivity
        implements AdapterView.OnItemSelectedListener {
    protected ActionBarSpinnerAdapter mSpinnerAdapter;
    protected int mSelectedSpinnerItem = -1;
    protected Spinner mActionBarSpinner;

    protected void setupActionBarSpinner(String curTab) {
        final ActionBar actionBar = getActionBar();
        assert actionBar != null;

        ArrayList<String> tabs = (ArrayList<String>) mChildCollection.get(curTab);

        if (tabs.size() > 0) { //Make sure there are tabs
            actionBar.setDisplayShowTitleEnabled(false); //No title - just the spinner

            View actionBarSpinnerCustomView = getLayoutInflater().inflate(R.layout.actionbar_spinner, null);
            actionBar.setCustomView(actionBarSpinnerCustomView);
            actionBar.setDisplayShowCustomEnabled(true);

            ArrayList<SpinnerNavItem> navSpinnerItems = new ArrayList<SpinnerNavItem>();
            for(String subtitle : tabs) {
                navSpinnerItems.add(new SpinnerNavItem(curTab, subtitle));
            }

            mSpinnerAdapter = new ActionBarSpinnerAdapter(getApplicationContext(), navSpinnerItems, mDarkUi);

            if (actionBarSpinnerCustomView != null) {
                mActionBarSpinner = (Spinner) actionBarSpinnerCustomView.findViewById(R.id.spinner);
                mActionBarSpinner.setAdapter(mSpinnerAdapter);
                mActionBarSpinner.setOnItemSelectedListener(this);
            }
        }
    }

    public void setSelectedTab(int position) {
        mActionBarSpinner.setSelection(position, true);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        mSelectedSpinnerItem = pos;
        //TODO Selection needs fixing
//        setSelectedItem(((SpinnerNavItem) mSpinnerAdapter.getItem(mSelectedSpinnerItem)).getTitle(),
//                ((SpinnerNavItem) mSpinnerAdapter.getItem(mSelectedSpinnerItem)).getSubtitle());
        onNavigationItemSelected(pos, id);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if(drawerOpen) {
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayShowCustomEnabled(false);
            } else {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public abstract boolean onNavigationItemSelected(int itemPosition, long itemId);
}
