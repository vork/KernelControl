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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.fourmob.colorpicker.ColorPickerDialog;
import com.fourmob.colorpicker.ColorPickerSwatch;
import com.vork.KernelControl.R;
import com.vork.KernelControl.Utils.Helper;
import com.vork.KernelControl.Utils.Preferences;

public class AppSettingsFragment extends PreferenceFragment implements Preferences {
    private Activity mActivity;
    private SharedPreferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.theme_settings);

        mActivity = getActivity();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        int accentColor = mPreferences.getInt(ACCENT_COLOR_PREF,
                getResources().getColor(R.color.accentBlue));

        Preference chooseAccentColor = findPreference("accent_color");

        if (chooseAccentColor != null) {
            final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
            colorPickerDialog.initialize(R.string.theme_settings_accent_color, new int[]{
                    getResources().getColor(R.color.accentBlue), getResources().getColor(R.color.accentPurple),
                    getResources().getColor(R.color.accentGreen), getResources().getColor(R.color.accentOrange),
                    getResources().getColor(R.color.accentRed)}, accentColor, 5, 2);
            colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                @Override
                public void onColorSelected(int color) {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putInt(ACCENT_COLOR_PREF, color);
                    editor.commit();
                    Helper.restartKC(mActivity);
                }
            });

            chooseAccentColor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    colorPickerDialog.show(getFragmentManager(), "colorpicker");
                    return false;
                }
            });
        }

        CheckBoxPreference darkUiSwitch = (CheckBoxPreference) findPreference("dark_ui_switch");

        if (darkUiSwitch != null) {
            darkUiSwitch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Helper.restartKC(mActivity);
                    return false;
                }
            });
        }


    }
}
