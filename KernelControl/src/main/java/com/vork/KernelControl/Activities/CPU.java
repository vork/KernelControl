package com.vork.KernelControl.Activities;

import android.os.Bundle;

import com.vork.KernelControl.BaseActivity;

public class CPU extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBarSpinner("CPU");
    }
}
