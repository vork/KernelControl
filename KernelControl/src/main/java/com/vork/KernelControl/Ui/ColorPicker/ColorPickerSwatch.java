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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fourmob.colorpicker.R;

public class ColorPickerSwatch extends FrameLayout implements View.OnClickListener {
    private ImageView mCheckmarkImage;
    private int mColor;
    private OnColorSelectedListener mOnColorSelectedListener;
    private ImageView mSwatchImage;

    public ColorPickerSwatch(Context paramContext, int color, boolean checked, OnColorSelectedListener onColorSelectedListener) {
        super(paramContext);
        this.mColor = color;
        this.mOnColorSelectedListener = onColorSelectedListener;
        LayoutInflater.from(paramContext).inflate(R.layout.color_picker_swatch, this);
        this.mSwatchImage = ((ImageView) findViewById(R.id.color_picker_swatch));
        this.mCheckmarkImage = ((ImageView) findViewById(R.id.color_picker_checkmark));
        setColor(color);
        setChecked(checked);
        setOnClickListener(this);
    }

    private void setChecked(boolean checked) {
        if (checked) {
            this.mCheckmarkImage.setVisibility(View.VISIBLE);
            return;
        }
        this.mCheckmarkImage.setVisibility(View.GONE);
    }

    public void onClick(View view) {
        if (this.mOnColorSelectedListener != null)
            this.mOnColorSelectedListener.onColorSelected(this.mColor);
    }

    protected void setColor(int color) {
        Drawable[] drawables = new Drawable[1];
        drawables[0] = getContext().getResources().getDrawable(R.drawable.color_picker_swatch);
        this.mSwatchImage.setImageDrawable(new com.fourmob.colorpicker.ColorStateDrawable(drawables, color));
    }

    public static abstract interface OnColorSelectedListener {
        public abstract void onColorSelected(int color);
    }
}