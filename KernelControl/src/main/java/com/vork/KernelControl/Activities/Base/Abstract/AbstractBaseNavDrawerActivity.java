package com.vork.KernelControl.Activities.Base.Abstract;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.crashlytics.android.Crashlytics;
import com.vork.KernelControl.Adapter.NavigationDrawerAdapter;
import com.vork.KernelControl.R;
import com.vork.KernelControl.Settings.AppSettings;

import static butterknife.ButterKnife.findById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBaseNavDrawerActivity extends AbstractBaseActivity implements
        NavigationDrawerAdapter.ToggleGroupListener {
    protected DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    protected ExpandableListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private NavigationDrawerAdapter mAdapter;

    private ArrayList<String> mGroupList;
    protected Map<String, List<String>> mChildCollection;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Android specific methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_nav_drawer_base);
        LayoutInflater.from(this).inflate(R.layout.activity_nav_drawer_base, mViewGroupContent);

        mGroupList = setGroupData();
        mChildCollection = setChildData(mGroupList);

        setupNavDrawer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AppSettings.class));
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            Crashlytics.log("Settings");
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Abstract methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates the group data for the navigation drawer
     *
     * @return list with group items
     */
    protected abstract ArrayList<String> setGroupData();

    /**
     * Creates the child data for the navigation drawer
     *
     * @param groupList the groups used in the drawer
     * @return a map with the children belonging to the group
     */
    protected abstract Map<String, List<String>> setChildData(ArrayList<String> groupList);

    /**
     * Executed when a child is pressed
     *
     * @param groupNr the position of the group in the navigation drawer
     * @param group   the label of the group
     * @param childNr the number of the pressed child
     */
    abstract protected void executeOnChildPress(int groupNr, String group, int childNr);

    /**
     * Executed when a group is pressed
     *
     * @param groupNr the position of the group in the navigation drawer
     * @param group   the label of the group
     */
    abstract protected void executeOnGroupPress(int groupNr, String group);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void closeNavigationDrawer() {
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Internal methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void setupNavDrawer() {
        final ActionBar actionBar = getActionBar();
        assert actionBar != null;
        mTitle = mDrawerTitle = getTitle();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkUi = preferences.getBoolean("dark_ui_switch", false);

        //Setup the navigation drawer
        mDrawerList = findById(this, R.id.left_drawer);
        if (mDarkUi) {
            mDrawerList.setBackgroundColor(getResources().getColor(R.color.card_background_darkTheme));
        } else {
            mDrawerList.setBackgroundColor(getResources().getColor(R.color.card_background_lightTheme));
        }
        mDrawerLayout = findById(this, R.id.drawer_layout);

        mAdapter = new NavigationDrawerAdapter(this, mGroupList, mChildCollection, mDarkUi);
        mAdapter.setListener(this);

        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //ToDo jump to correct tab
                //                final String child = (String) mAdapter.getChild(
//                        groupPosition, childPosition);
                final String group = (String) mAdapter.getGroup(groupPosition);
                executeOnChildPress(groupPosition, group, childPosition);
                return false;
            }
        });
        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                final String group = (String) mAdapter.getGroup(groupPosition);
                executeOnGroupPress(groupPosition, group);
                return true;
            }
        });

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    public boolean toggleGroupState(int groupPos) {
        if (mDrawerList.isGroupExpanded(groupPos)) {
            mDrawerList.collapseGroup(groupPos);
            return false;
        } else {
            mDrawerList.expandGroup(groupPos);
            return true;
        }
    }

    /**
     * Returns true if the navigation drawer is open.
     */
    protected boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(mDrawerList);
    }

    public void setDrawerIndicatorEnabled(boolean isEnabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(isEnabled);
    }

    protected void setSelectedItem(String activityName, String tabName) {
        int index = mDrawerList.getFlatListPosition(ExpandableListView.getPackedPositionForChild(
                getGroupIndex(activityName), getChildIndex(activityName, tabName)));
        mDrawerList.setItemChecked(index, true);
        setSelectedItem(activityName);
    }

    protected void setSelectedItem(String activityName) {
        int index = mDrawerList.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(getGroupIndex(activityName)));
        mDrawerList.setItemChecked(index, true);
    }

    private int getGroupIndex(String groupName) {
        Set<String> groups = mChildCollection.keySet();
        int toRet = -1;

        int temp = 0;
        for (String group : groups) {
            if (group.equals(groupName)) {
                toRet = temp;
                break;
            } else {
                temp++;
            }
        }

        return toRet;
    }

    private int getChildIndex(String groupName, String childName) {
        int toRet = -1;

        List<String> childs = mChildCollection.get(groupName);

        int temp = 0;
        for (String child : childs) {
            if (child.equals(childName)) {
                toRet = temp;
            } else {
                temp++;
            }
        }

        return toRet;
    }

    /**
     * Holder for the ActionBar Spinner Items
     */
    public class SpinnerNavItem {

        private String mTitle;
        private String mSubtitle;

        public SpinnerNavItem(String title, String subtitle) {
            this.mTitle = title;
            this.mSubtitle = subtitle;
        }

        public String getTitle() {
            return this.mTitle;
        }

        public String getSubtitle() {
            return this.mSubtitle;
        }
    }
}
