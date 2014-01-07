package com.vork.KernelControl.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vork.KernelControl.Activities.Base.BaseNavDrawerSpinnerActivity;
import com.vork.KernelControl.R;

public class CPU extends BaseNavDrawerSpinnerActivity {
    private final static int FREQUENCY_TAB = 0;
    private final static int VOLTAGE_TAB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBarSpinner(getString(R.string.menu_item_cpu));

        Intent intent = getIntent();
        int selectedTab = intent.getIntExtra(NAV_DRAWER_BUNDLE_EXTRA, 0);

        if(savedInstanceState == null) {
            setSelectedTab(selectedTab);

            if(selectedTab == VOLTAGE_TAB) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new CpuVoltageTab())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new CpuFrequencyTab())
                        .commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        super.onNavigationItemSelected(itemPosition, itemId);
        Log.d("KernelControl", "pos: " + itemPosition);
        if(itemPosition == 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new CpuFrequencyTab())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new CpuVoltageTab())
                    .commit();
        }

        return false;
    }

    public static class CpuFrequencyTab extends Fragment {
        public CpuFrequencyTab() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dummy_fragment, container, false);

            TextView txtDummy = (TextView) rootView.findViewById(R.id.txt_dummy);
            txtDummy.setText("Frequency");

            return rootView;
        }

        }

    public static class CpuVoltageTab extends Fragment {
        public CpuVoltageTab() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dummy_fragment, container, false);

            TextView txtDummy = (TextView) rootView.findViewById(R.id.txt_dummy);
            txtDummy.setText("Voltage");

            return rootView;
        }
    }
}
