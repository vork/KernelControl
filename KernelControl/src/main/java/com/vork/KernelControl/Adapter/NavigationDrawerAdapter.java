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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vork.KernelControl.R;

import java.util.List;
import java.util.Map;

import static butterknife.ButterKnife.findById;

public class NavigationDrawerAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> mGroups;
    private List<String> mChilds;
    private ToggleGroupListener mToggleListener;

    public NavigationDrawerAdapter(Activity context, List<String> mChilds,
                                   Map<String, List<String>> groupCollection) {
        this.context = context;
        this.mGroups = groupCollection;
        this.mChilds = mChilds;
    }

    public void setListener(ToggleGroupListener listener) {
        mToggleListener = listener;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(mChilds.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String ChildName = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_row_item, null);
        }

        assert convertView != null;

        TextView item = findById(convertView, R.id.child_title);
        item.setText(ChildName);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return mGroups.get(mChilds.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return mChilds.get(groupPosition);
    }

    public int getGroupCount() {
        return mChilds.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.menu_row_item,
                    null);
        }
        final TextView item = findById(convertView, R.id.group_title);
        item.setText(groupName);

        final ImageView expandIcon = findById(convertView, R.id.expandable_icon);

        //Rotate the expand icon as a indicator
        if (getChildrenCount(groupPosition) == 0) {
            expandIcon.setVisibility(View.INVISIBLE);
        } else {
            expandIcon.setPivotX(expandIcon.getWidth() / 2);
            expandIcon.setPivotY(expandIcon.getHeight() / 2);
            expandIcon.setVisibility(View.VISIBLE);
            if (isExpanded) {
                expandIcon.setRotation(180);
            } else {
                expandIcon.setRotation(0);
            }
            expandIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean expanded = mToggleListener.toggleGroupState(groupPosition);
                    //Rotate the icon
                    expandIcon.setPivotX(expandIcon.getWidth() / 2);
                    expandIcon.setPivotY(expandIcon.getHeight() / 2);
                    if (expanded) {
                        expandIcon.setRotation(180);
                    } else {
                        expandIcon.setRotation(0);
                    }
                    expandIcon.setPressed(true);
                }
            });
        }
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface ToggleGroupListener {
        boolean toggleGroupState(int groupPos);
    }
}