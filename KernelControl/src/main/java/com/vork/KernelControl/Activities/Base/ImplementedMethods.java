package com.vork.KernelControl.Activities.Base;

import android.app.Activity;
import android.content.Intent;

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
        Intent intent = null;
        if(group.equals(activity.getString(R.string.menu_item_cpu))) {
            intent = new Intent(activity, CPU.class);
        } else if (group.equals(activity.getString(R.string.menu_item_memory))) {
            intent = new Intent(activity, Memory.class);
        } else if (group.equals(activity.getString(R.string.menu_item_misc))) {

        } else if (group.equals(activity.getString(R.string.menu_item_info))) {

        }
        if (intent != null) {
            intent.putExtra(NAV_DRAWER_BUNDLE_EXTRA, childNr);
            activity.startActivity(intent);
        }
    }

    public static void executeGroupPress(Activity activity, int groupNr, String group) {
        Intent intent = null;
        if(group.equals(activity.getString(R.string.menu_item_cpu))) {
            intent = new Intent(activity, CPU.class);
        } else if (group.equals(activity.getString(R.string.menu_item_memory))) {
            intent = new Intent(activity, Memory.class);
        } else if (group.equals(activity.getString(R.string.menu_item_misc))) {

        } else if (group.equals(activity.getString(R.string.menu_item_info))) {

        }
        if (intent != null) {
            activity.startActivity(intent);
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
}
