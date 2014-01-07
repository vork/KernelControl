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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vork.KernelControl.Activities.Base.BaseNavDrawerSpinnerActivity;
import com.vork.KernelControl.R;
import com.vork.KernelControl.Utils.Helper;

import static butterknife.ButterKnife.findById;

public class Memory extends BaseNavDrawerSpinnerActivity {
    private final static int MEMORY_TAB = 0;
    private final static int IO_TAB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBarSpinner(getString(R.string.menu_item_memory));

        Intent intent = getIntent();
        int selectedTab = intent.getIntExtra(NAV_DRAWER_BUNDLE_EXTRA, 0);

        if (savedInstanceState == null) {
            setSelectedTab(selectedTab);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        super.onNavigationItemSelected(itemPosition, itemId);
        if (itemPosition == 0) {
            Helper.switchFragment(getSupportFragmentManager(), R.id.content_frame, new MemoryMemTab());
        } else {
            Helper.switchFragment(getSupportFragmentManager(), R.id.content_frame, new MemoryIoTab());
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

            TextView txtDummy = findById(rootView, R.id.txt_dummy);
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

            TextView txtDummy = findById(rootView, R.id.txt_dummy);
            txtDummy.setText("I/O");

            return rootView;
        }
    }
}
