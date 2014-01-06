package com.vork.KernelControl.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vork.KernelControl.Activities.Base.BaseNavDrawerSpinnerActivity;
import com.vork.KernelControl.R;

public class Memory extends BaseNavDrawerSpinnerActivity {
    private final static int MEMORY_TAB = 0;
    private final static int IO_TAB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBarSpinner(getString(R.string.menu_item_memory));

        Intent intent = getIntent();
        int selectedTab = intent.getIntExtra(NAV_DRAWER_BUNDLE_EXTRA, 0);

        if(savedInstanceState == null) {
            setSelectedTab(selectedTab);

            if(selectedTab == IO_TAB) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new MemoryIoTab())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new MemoryMemTab())
                        .commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        super.onNavigationItemSelected(itemPosition, itemId);
        if(itemPosition == 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new MemoryMemTab())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new MemoryIoTab())
                    .commit();
        }

        return false;
    }

    public static class MemoryMemTab extends Fragment {
        public MemoryMemTab() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dummy_fragment, container, false);

            TextView txtDummy = (TextView) rootView.findViewById(R.id.txt_dummy);
            txtDummy.setText("Memory");

            return rootView;
        }

    }

    public static class MemoryIoTab extends Fragment {
        public MemoryIoTab() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dummy_fragment, container, false);

            TextView txtDummy = (TextView) rootView.findViewById(R.id.txt_dummy);
            txtDummy.setText("I/O");

            return rootView;
        }
    }
}
