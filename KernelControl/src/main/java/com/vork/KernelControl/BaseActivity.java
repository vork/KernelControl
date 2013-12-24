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
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.negusoft.holoaccent.AccentHelper;
import com.vork.KernelControl.Activities.CPU;
import com.vork.KernelControl.Settings.AppSettings;
import com.vork.KernelControl.UI.SlidingMenu;
import com.vork.KernelControl.Utils.Helper;

public class BaseActivity extends FragmentActivity {
    private static final int MENU_ITEM_CPU_ID = 0;
    private static final int MENU_ITEM_MEM_ID = 1;
    private static final int MENU_ITEM_MISC_ID = 2;
    private static final int MENU_ITEM_INFO_ID = 3;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private SlidingMenu.MenuAdapter mAdapter;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

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

        final ActionBar actionBar = getActionBar();
        assert actionBar != null;
        mTitle = mDrawerTitle = getTitle();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkUI = preferences.getBoolean("dark_ui_switch", false);

        //Setup the navigation drawer
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mAdapter = new SlidingMenu.MenuAdapter(this);
        mAdapter.add(new SlidingMenu.MenuItemList(getString(R.string.menu_item_cpu),
                darkUI ? R.drawable.ic_ic_menu_cpu_d : R.drawable.ic_ic_menu_cpu_l, MENU_ITEM_CPU_ID));

        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
        mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerList.setItemChecked(position, true);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (((SlidingMenu.MenuItemList) mAdapter.getItem(position)).mId) {
                    case MENU_ITEM_CPU_ID:
                        startActivity(new Intent(context, CPU.class).
                                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        Crashlytics.log("CPU");
                        break;
                    default:
                        break;
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, postDelayed);
    }
}
