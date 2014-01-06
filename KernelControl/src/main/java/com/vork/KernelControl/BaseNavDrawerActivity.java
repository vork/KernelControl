package com.vork.KernelControl;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.crashlytics.android.Crashlytics;
import com.vork.KernelControl.Adapter.NavigationDrawerAdapter;
import com.vork.KernelControl.Settings.AppSettings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseNavDrawerActivity extends BaseActivity implements
        NavigationDrawerAdapter.ToggleGroupListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ExpandableListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private NavigationDrawerAdapter mAdapter;

    private ArrayList<String> mGroupList;
    private List<String> mChildList;
    protected Map<String, List<String>> mChildCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nav_drawer_base);

        setGroupData();
        setChildData();

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
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private void setGroupData() {
        mGroupList = new ArrayList<String>();
        mGroupList.add(getString(R.string.menu_item_cpu));
        mGroupList.add(getString(R.string.menu_item_memory));
        mGroupList.add(getString(R.string.menu_item_misc));
        mGroupList.add(getString(R.string.menu_item_info));
    }

    private void setChildData() {
        String[] cpuTabs = {getString(R.string.menu_item_cpu_child_frequency),
                getString(R.string.menu_item_cpu_child_voltage)};

        String[] memTabs = {getString(R.string.menu_item_memory_child_memory),
                getString(R.string.menu_item_memory_child_io)};

        String[] miscTabs = {}; //Empty for now

        String[] infoTabs = {getString(R.string.menu_item_info_child_general),
                getString(R.string.menu_item_info_child_tis),
                getString(R.string.menu_item_info_child_memory)};

        mChildCollection = new LinkedHashMap<String, List<String>>();

        for (String item : mGroupList) {
            if (item.equals(getString(R.string.menu_item_cpu))) {
                loadChild(cpuTabs);
            } else if (item.equals(getString(R.string.menu_item_memory))) {
                loadChild(memTabs);
            } else if (item.equals(getString(R.string.menu_item_misc))) {
                loadChild(miscTabs);
            } else if (item.equals(getString(R.string.menu_item_info))) {
                loadChild(infoTabs);
            }

            mChildCollection.put(item, mChildList);
        }
    }

    private void loadChild(String[] items) {
        mChildList = new ArrayList<String>();
        for (String item : items)
            mChildList.add(item);
    }

    private void setupNavDrawer() {
        final ActionBar actionBar = getActionBar();
        assert actionBar != null;
        mTitle = mDrawerTitle = getTitle();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkUi = preferences.getBoolean("dark_ui_switch", false);

        //Setup the navigation drawer
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
        if (mDarkUi) {
            mDrawerList.setBackgroundColor(getResources().getColor(R.color.card_background_darkTheme));
        } else {
            mDrawerList.setBackgroundColor(getResources().getColor(R.color.card_background_lightTheme));
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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
                Log.d("KernelControl", groupPosition + " " + childPosition);
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

    //Needs to be overwritten
    abstract protected void executeOnChildPress(int groupNr, String group, int childNr);
    abstract protected void executeOnGroupPress(int groupNr, String group);

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

    private void selectItem(final Context context, final int position) {
        int postDelayed = 0;
        Handler h = new Handler();
        //Smooth menu closing
        postDelayed = 120;
//        mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerList.setItemChecked(position, true);

//        h.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                switch (((SlidingMenu.MenuItemList) mAdapter.getItem(position)).mId) {
//                    case MENU_ITEM_CPU_ID:
//                        startActivity(new Intent(context, CPU.class).
//                                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP));
//                        Crashlytics.log("CPU");
//                        break;
//                    default:
//                        break;
//                }
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
//        }, postDelayed);
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
        for(String group : groups) {
            if(group.equals(groupName)) {
                toRet = temp;
                break;
            } else {
                temp ++;
            }
        }

        return toRet;
    }

    private int getChildIndex(String groupName, String childName) {
        int toRet = -1;

        List<String> childs = mChildCollection.get(groupName);

        int temp = 0;
        for(String child : childs) {
            if(child.equals(childName)) {
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
