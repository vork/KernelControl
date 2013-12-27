package com.vork.KernelControl.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vork.KernelControl.BaseActivity;
import com.vork.KernelControl.R;

import java.util.ArrayList;

public class ActionBarSpinnerAdapter extends BaseAdapter {

    private TextView mTxtTitle;
    private TextView mTxtSubtitle;
    private ArrayList<BaseActivity.SpinnerNavItem> mSpinnerNavItem;
    private Context mContext;

    public ActionBarSpinnerAdapter(Context context,
                                  ArrayList<BaseActivity.SpinnerNavItem> spinnerNavItem) {
        this.mSpinnerNavItem = spinnerNavItem;
        this.mContext = context;
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
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.actionbar_spinner_item, null);
        }

        mTxtTitle = (TextView) convertView.findViewById(R.id.spinner_list_item_header);
        mTxtSubtitle = (TextView) convertView.findViewById(R.id.spinner_list_item_subtitle);

        mTxtTitle.setText(mSpinnerNavItem.get(position).getTitle());
        mTxtSubtitle.setText(mSpinnerNavItem.get(position).getSubtitle());
        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.actionbar_spinner_dropdown_item, null);
        }

        assert convertView != null;
        mTxtSubtitle = (TextView) convertView.findViewById(R.id.spinner_drop_down_item);

        mTxtSubtitle.setText(mSpinnerNavItem.get(position).getSubtitle());
        return convertView;
    }

}
