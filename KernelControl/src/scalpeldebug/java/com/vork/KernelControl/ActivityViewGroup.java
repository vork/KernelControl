package com.vork.KernelControl;

import android.app.Activity;
import android.view.ViewGroup;

import com.jakewharton.scalpel.ScalpelFrameLayout;

public final class ActivityViewGroup {
    public static ViewGroup get(Activity activity) {
        ScalpelFrameLayout scalpel = new ScalpelFrameLayout(activity);
        scalpel.setLayerInteractionEnabled(true);
        activity.setContentView(scalpel);
        return scalpel;
    }

    private ActivityViewGroup() {
        throw new AssertionError("No instances.");
    }
}
