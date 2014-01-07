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

package com.vork.KernelControl.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vork.KernelControl.Activities.Base.Abstract.AbstractBaseNavDrawerActivity;
import com.vork.KernelControl.R;

import java.util.ArrayList;

public class ActionBarSpinnerAdapter extends BaseAdapter {

    private ArrayList<AbstractBaseNavDrawerActivity.SpinnerNavItem> mSpinnerNavItem;
    private Context mContext;
    private Boolean mDarkUi;

    public ActionBarSpinnerAdapter(Context context,
                                   ArrayList<AbstractBaseNavDrawerActivity.SpinnerNavItem> spinnerNavItem, boolean darkUi) {
        this.mSpinnerNavItem = spinnerNavItem;
        this.mContext = context;
        this.mDarkUi = darkUi;
    }

    @Override
    public int getCount() {
        return mSpinnerNavItem.size();
    }

    @Override
    public Object getItem(int index) {
        return mSpinnerNavItem.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.actionbar_spinner_item, null);

            holder = new ViewHolder();
            holder.mTxtTitle = (TextView) convertView.findViewById(android.R.id.text1);
            holder.mTxtSubTitle = (TextView) convertView.findViewById(android.R.id.text2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTxtTitle.setText(mSpinnerNavItem.get(position).getTitle());
        holder.mTxtSubTitle.setText(mSpinnerNavItem.get(position).getSubtitle());

        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);

            convertView.setMinimumHeight(mContext.getResources().
                    getDimensionPixelOffset(R.dimen.default_touch_size));

            holder = new ViewHolder();
            holder.mTxtTitle = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTxtTitle.setText(mSpinnerNavItem.get(position).getSubtitle());

        if (mDarkUi) {
            holder.mTxtTitle.setTextAppearance(mContext, R.style.KC_Dark_TextAppearance_Widget_ActionBar_Spinner_DropDownItem);
        } else {
            holder.mTxtTitle.setTextAppearance(mContext, R.style.KC_Light_TextAppearance_Widget_ActionBar_Spinner_DropDownItem);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView mTxtTitle;
        public TextView mTxtSubTitle;
    }

}
