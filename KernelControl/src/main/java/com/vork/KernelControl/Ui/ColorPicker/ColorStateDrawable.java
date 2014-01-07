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

package com.vork.KernelControl.Ui.ColorPicker;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

public class ColorStateDrawable extends LayerDrawable {
    private int mColor;

    public ColorStateDrawable(Drawable[] drawables, int color) {
        super(drawables);
        this.mColor = color;
    }

    private int getPressedColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = (0.7F * hsv[2]);
        return Color.HSVToColor(hsv);
    }

    public boolean isStateful() {
        return true;
    }

    protected boolean onStateChange(int[] states) {
        boolean pressed = false;
        for (int state : states) {
            if (state == android.R.attr.state_pressed || state == android.R.attr.state_focused) {
                pressed = true;
            }
        }
        if (pressed) {
            super.setColorFilter(getPressedColor(this.mColor), PorterDuff.Mode.SRC_ATOP);
        } else {
            super.setColorFilter(this.mColor, PorterDuff.Mode.SRC_ATOP);
        }
        return super.onStateChange(states);
    }
}
