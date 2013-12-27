package com.vork.KernelControl;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.negusoft.holoaccent.AccentHelper;
import com.vork.KernelControl.Adapter.ActionBarSpinnerAdapter;
import com.vork.KernelControl.Adapter.NavigationDrawerAdapter;
import com.vork.KernelControl.Settings.AppSettings;
import com.vork.KernelControl.Utils.Helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseActivity extends FragmentActivity implements
        NavigationDrawerAdapter.ToggleGroupListener,
        ActionBar.OnNavigationListener {
    private static final int MENU_ITEM_CPU_ID = 0;
    private static final int MENU_ITEM_MEM_ID = 1;
    private static final int MENU_ITEM_MISC_ID = 2;
    private static final int MENU_ITEM_INFO_ID = 3;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ExpandableListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private NavigationDrawerAdapter mAdapter;

    private ArrayList<String> mGroupList;
    private List<String> mChildList;
    private Map<String, List<String>> mChildCollection;

    private ArrayList<SpinnerNavItem> mNavSpinnerItems;
    private ActionBarSpinnerAdapter mSpinnerAdapter;


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

        setContentView(R.layout.activity_base);

        Crashlytics.start(this);

        setGroupData();
        setChildData();

        setupNavDrawer();
    }

    private void setGroupData() {
        mGroupList = new ArrayList<String>();
        mGroupList.add("CPU");
        mGroupList.add("Memory & I/O");
        mGroupList.add("Misc");
        mGroupList.add("Info");
    }

    private void setChildData() {
        String[] cpuTabs = {"Frequency", "Voltage"};
        String[] memTabs = {"Memory", "I/O"};
        String[] miscTabs = {}; //Empty for now
        String[] infoTabs = {"General", "Time in state", "Memory"};

        mChildCollection = new LinkedHashMap<String, List<String>>();

        for (String item : mGroupList) {
            if (item.equals("CPU")) {
                loadChild(cpuTabs);
            } else if (item.equals("Memory & I/O")) {
                loadChild(memTabs);
            } else if (item.equals("Misc")) {
                loadChild(miscTabs);
            } else if (item.equals("Info")) {
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

            mSpinnerAdapter = new ActionBarSpinnerAdapter(getApplicationContext(), mNavSpinnerItems);

            actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
        }
    }

    private void setupNavDrawer() {
        final ActionBar actionBar = getActionBar();
        assert actionBar != null;
        mTitle = mDrawerTitle = getTitle();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkUI = preferences.getBoolean("dark_ui_switch", false);

        //Setup the navigation drawer
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
        if (darkUI) {
            mDrawerList.setBackgroundColor(getResources().getColor(R.color.card_background_darkTheme));
        } else {
            mDrawerList.setBackgroundColor(getResources().getColor(R.color.card_background_lightTheme));
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mAdapter = new NavigationDrawerAdapter(this, mGroupList, mChildCollection, darkUI);
        mAdapter.setListener(this);

        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //ToDo jump to correct tab
                final String selected = (String) mAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                final String selected = (String) mAdapter.getGroup(groupPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_SHORT).show();
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

    public boolean toggleGroupState(int groupPos) {
        if (mDrawerList.isGroupExpanded(groupPos)) {
            mDrawerList.collapseGroup(groupPos);
            return false;
        } else {
            mDrawerList.expandGroup(groupPos);
            return true;
        }
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

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(getApplicationContext(), position);
        }
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
