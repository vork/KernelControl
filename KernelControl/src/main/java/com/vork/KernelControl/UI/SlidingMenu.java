package com.vork.KernelControl.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vork.KernelControl.R;

public class SlidingMenu {
    static class ViewHolder {

        public TextView title;
        public ImageView icon;

        public void attach(View v) {
            icon = (ImageView) v.findViewById(R.id.menu_icon);
            title = (TextView) v.findViewById(R.id.menu_title);
        }
    }

    public static class MenuItemList {

        String mTitle = "";
        int mIconRes = 0;
        public int mId = 0;
        Drawable mIconDraw = null;

        public MenuItemList(String title, int iconRes, int id) {
            mTitle = title;
            mIconRes = iconRes;
            mId = id;
        }

        public MenuItemList(String title, Drawable iconDraw, int id) {
            mTitle = title;
            mIconDraw = iconDraw;
            mId = id;
        }
    }

    public class MenuCategory {

        public MenuCategory() {
        }
    }

    public static class MenuAdapter extends ArrayAdapter<Object> {
        private Context mContext;

        public MenuAdapter(Context context) {
            super(context, 0);
            mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position) instanceof MenuItemList ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItem(position) instanceof MenuItemList;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public String getTitle(int position) {
            Object item = getItem(position);

            if (item instanceof MenuItemList) {
                MenuItemList menuItem = (MenuItemList) item;
                return menuItem.mTitle;
            }
            return null;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Object item = getItem(position);

            if (item instanceof MenuItemList) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(
                            R.layout.menu_row_item, parent, false);
                    holder = new ViewHolder();
                    holder.attach(convertView);
                    assert convertView != null;
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                MenuItemList menuItem = (MenuItemList) item;
                if (menuItem.mIconDraw == null) {
                    holder.icon.setImageResource(menuItem.mIconRes);
                } else {
                    holder.icon.setImageDrawable(menuItem.mIconDraw);
                }
                holder.title.setText(menuItem.mTitle);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                boolean darkUI = preferences.getBoolean("dark_ui_switch", false);
                if (darkUI) {
                    holder.title.setTextAppearance(mContext, R.style.MenuItemDark);
                } else {
                    holder.title.setTextAppearance(mContext, R.style.MenuItemLight);
                }

            } else {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(
                            R.layout.menu_row_category, parent, false);
                }
            }

            return convertView;
        }
    }
}
