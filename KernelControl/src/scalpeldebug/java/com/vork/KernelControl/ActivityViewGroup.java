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
