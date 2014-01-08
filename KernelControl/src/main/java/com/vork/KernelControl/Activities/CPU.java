/*
 * This file is part of KernelControl.
 *
 *     KernelControl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KernelControl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KernelControl.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import com.vork.KernelControl.Utils.Database.DatabaseHandler;
import com.vork.KernelControl.Utils.Database.DatabaseObjects.KernelInterface;
import com.vork.KernelControl.Utils.Helper;

import java.util.List;

import static butterknife.ButterKnife.findById;

public class CPU extends BaseNavDrawerSpinnerActivity {
    private final static int FREQUENCY_TAB = 0;
    private final static int VOLTAGE_TAB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBarSpinner(getString(R.string.menu_item_cpu));

        Intent intent = getIntent();
        int selectedTab = intent.getIntExtra(NAV_DRAWER_BUNDLE_EXTRA, 0);

        if (savedInstanceState == null) {
            setSelectedTab(selectedTab);
        }

        DatabaseHandler db = new DatabaseHandler(this);
        KernelInterface frequency = new KernelInterface("Frequency", "This sets the CPU Frequency.", "/sys/sys/sys/");
        frequency = db.addKernelInterface(frequency);
        KernelInterface governor = new KernelInterface("Governor", "/sys/sys/sys/");
        governor = db.addKernelInterface(governor);
        governor.setSetOnBoot(true);

        Log.d("KernelControl", "Reading all interfaces");
        List<KernelInterface> interfaces = db.getAllKernelInterfaces();

        for(KernelInterface kernelInterface : interfaces) {
            String log = "Id: " + kernelInterface.getId() + " Name: " + kernelInterface.getName() +
                    " Description: " + kernelInterface.getDescription() + " Path: " + kernelInterface.getPath() +
                    " Value: " + kernelInterface.getValue() + " SOB: " + kernelInterface.getSetOnBoot();
            Log.d("KernelControl" , log);
        }

        governor.setSetOnBoot(true);
        governor.setValue("2");

        db.updateKernelInterface(governor);

        governor = db.getKernelInterface("Governor");
        String log = "Id: " + governor.getId() + " Name: " + governor.getName() +
                " Description: " + governor.getDescription() + " Path: " + governor.getPath() +
                " Value: " + governor.getValue() + " SOB: " + governor.getSetOnBoot();
        Log.d("KernelControl" , log);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        super.onNavigationItemSelected(itemPosition, itemId);
        if (itemPosition == 0) {
            Helper.switchFragment(getSupportFragmentManager(), R.id.content_frame, new CpuFrequencyTab());
        } else {
            Helper.switchFragment(getSupportFragmentManager(), R.id.content_frame, new CpuVoltageTab());
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

            TextView txtDummy = findById(rootView, R.id.txt_dummy);
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

            TextView txtDummy = findById(rootView, R.id.txt_dummy);
            txtDummy.setText("Voltage");

            return rootView;
        }
    }
}
