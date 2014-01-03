package com.vork.KernelControl.Activities;

import android.os.Bundle;

import com.vork.KernelControl.BaseNavDrawerActivity;
import com.vork.KernelControl.BaseNavDrawerSpinnerActivity;

public class CPU extends BaseNavDrawerSpinnerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBarSpinner("CPU");
    }
}
