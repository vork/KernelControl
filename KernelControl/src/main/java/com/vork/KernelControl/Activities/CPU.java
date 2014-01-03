package com.vork.KernelControl.Activities;

import android.os.Bundle;

import com.vork.KernelControl.BaseNavDrawerActivity;
import com.vork.KernelControl.BaseNavDrawerSpinnerActivity;
import com.vork.KernelControl.R;

public class CPU extends BaseNavDrawerSpinnerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ToDo Allow stating of Activity with Bundle selected tab
        setupActionBarSpinner(getString(R.string.menu_item_cpu));
    }
}
