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

package com.vork.KernelControl.Activities.Base;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.vork.KernelControl.Activities.CPU;
import com.vork.KernelControl.Activities.Memory;
import com.vork.KernelControl.R;
import com.vork.KernelControl.Utils.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ImplementedMethods implements Constants {

    public static void executeChildPress(Activity activity, int groupNr, String group, int childNr) {
        Class<?> cls = null;
        if (group.equals(activity.getString(R.string.menu_item_cpu))) {
            cls = CPU.class;
        } else if (group.equals(activity.getString(R.string.menu_item_memory))) {
            cls = Memory.class;
        } else if (group.equals(activity.getString(R.string.menu_item_misc))) {

        } else if (group.equals(activity.getString(R.string.menu_item_info))) {

        }

        if (cls != null) {
            Intent intent = new Intent(activity, cls);
            intent.putExtra(NAV_DRAWER_BUNDLE_EXTRA, childNr);

            activityStarter(activity, cls, intent);
        }
    }

    public static void executeGroupPress(Activity activity, int groupNr, String group) {
        Class<?> cls = null;
        if (group.equals(activity.getString(R.string.menu_item_cpu))) {
            cls = CPU.class;
        } else if (group.equals(activity.getString(R.string.menu_item_memory))) {
            cls = Memory.class;
        } else if (group.equals(activity.getString(R.string.menu_item_misc))) {

        } else if (group.equals(activity.getString(R.string.menu_item_info))) {

        }

        if (cls != null) {
            Intent intent = new Intent(activity, cls);

            activityStarter(activity, cls, intent);
        }
    }

    public static ArrayList<String> setGroupData(Activity activity) {
        ArrayList<String> mGroupList = new ArrayList<String>();
        mGroupList.add(activity.getString(R.string.menu_item_cpu));
        mGroupList.add(activity.getString(R.string.menu_item_memory));
        mGroupList.add(activity.getString(R.string.menu_item_misc));
        mGroupList.add(activity.getString(R.string.menu_item_info));

        return mGroupList;
    }

    public static Map<String, List<String>> setChildData(Activity activity, ArrayList<String> groupList) {
        String[] cpuTabs = {activity.getString(R.string.menu_item_cpu_child_frequency),
                activity.getString(R.string.menu_item_cpu_child_voltage)};

        String[] memTabs = {activity.getString(R.string.menu_item_memory_child_memory),
                activity.getString(R.string.menu_item_memory_child_io)};

        String[] miscTabs = {}; //Empty for now

        String[] infoTabs = {activity.getString(R.string.menu_item_info_child_general),
                activity.getString(R.string.menu_item_info_child_tis),
                activity.getString(R.string.menu_item_info_child_memory)};

        Map<String, List<String>> mChildCollection = new LinkedHashMap<String, List<String>>();

        List<String> childList = null;
        for (String item : groupList) {
            if (item.equals(activity.getString(R.string.menu_item_cpu))) {
                childList = loadChild(cpuTabs);
            } else if (item.equals(activity.getString(R.string.menu_item_memory))) {
                childList = loadChild(memTabs);
            } else if (item.equals(activity.getString(R.string.menu_item_misc))) {
                childList = loadChild(miscTabs);
            } else if (item.equals(activity.getString(R.string.menu_item_info))) {
                childList = loadChild(infoTabs);
            }

            mChildCollection.put(item, childList);
        }

        return mChildCollection;
    }

    private static List<String> loadChild(String[] items) {
        List<String> mChildList = new ArrayList<String>();
        for (String item : items)
            mChildList.add(item);

        return mChildList;
    }

    private static void activityStarter(final Activity activity, final Class<?> cls, final Intent intent) {
        int postDelayed = 140;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (activity instanceof BaseNavDrawerActivity) {
            ((BaseNavDrawerActivity) activity).closeNavigationDrawer();
        } else if (activity instanceof BaseNavDrawerSpinnerActivity) { //TODO: A bit ugly - I know
            ((BaseNavDrawerSpinnerActivity) activity).closeNavigationDrawer();
        }

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean sameActivity = false;
                if (activity.getClass().getSimpleName().equals(cls.getSimpleName())) {
                    sameActivity = true;
                }

                if (sameActivity) {
                    if (activity instanceof BaseNavDrawerSpinnerActivity) { //Only for spinner
                        //If a group item is pressed there are no extras in the intent
                        //So we just set the selectedTab to -1 and ignoring it
                        int selectedTab = intent.getIntExtra(NAV_DRAWER_BUNDLE_EXTRA, -1);
                        if (selectedTab != -1) {
                            ((BaseNavDrawerSpinnerActivity) activity).setSelectedTab(selectedTab);
                        }
                    }
                } else {
                    activity.startActivity(intent);
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, postDelayed);
    }
}
