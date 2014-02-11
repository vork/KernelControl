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

package com.vork.KernelControl.Settings;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.vork.KernelControl.R;
import com.vork.KernelControl.Ui.ColorPicker.ColorPickerPalette;
import com.vork.KernelControl.Ui.ColorPicker.ColorPickerSwatch;
import com.vork.KernelControl.Utils.Helper;
import com.vork.KernelControl.Utils.Preferences;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static butterknife.ButterKnife.findById;

public class ThemeSettingsFragment extends Fragment implements Preferences, ColorPickerSwatch.OnColorSelectedListener {
    private Activity mActivity;
    private SharedPreferences mPreferences;

    private int mAccentColor;
    private int[] mColors;

    private boolean mDarkUi;

    @InjectView(R.id.color_picker) protected ColorPickerPalette mColorPicker;

    @OnClick(R.id.btn_save_theme)
    public void saveThemeRestart() {
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putBoolean(DARK_UI_PREF, mDarkUi);
        editor.putInt(ACCENT_COLOR_PREF, mAccentColor);

        editor.commit();

        Helper.restartKC(mActivity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        mAccentColor = mPreferences.getInt(ACCENT_COLOR_PREF,
                getResources().getColor(R.color.accentBlue));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.theme_preference, null);

        ButterKnife.inject(this, v);

        com.negusoft.holoaccent.widget.AccentSwitch darkUiSwitch = findById(v, R.id.dark_ui_switch);

        mDarkUi = mPreferences.getBoolean(DARK_UI_PREF, false);
        darkUiSwitch.setChecked(mDarkUi);

        darkUiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDarkUi = isChecked;
            }
        });

        mColors = new int[]{getResources().getColor(R.color.accentBlue), getResources().getColor(R.color.accentPurple),
                getResources().getColor(R.color.accentGreen), getResources().getColor(R.color.accentOrange),
                getResources().getColor(R.color.accentRed)};

        mColorPicker.init(2, 5, this);
        mColorPicker.drawPalette(mColors, mAccentColor);

        return v;
    }

    @Override
    public void onColorSelected(int color) {
        if (color != mAccentColor) {
            mAccentColor = color;
            // Redraw palette to show checkmark on newly selected color before dismissing.
            mColorPicker.drawPalette(mColors, mAccentColor);
        }
    }
}
