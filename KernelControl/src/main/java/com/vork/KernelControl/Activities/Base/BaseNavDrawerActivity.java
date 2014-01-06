package com.vork.KernelControl.Activities.Base;

import com.vork.KernelControl.Activities.Base.Abstract.AbstractBaseNavDrawerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseNavDrawerActivity extends AbstractBaseNavDrawerActivity {


    @Override
    protected ArrayList<String> setGroupData() {
        return ImplementedMethods.setGroupData(this);
    }

    @Override
    protected Map<String, List<String>> setChildData(ArrayList<String> groupList) {
        return ImplementedMethods.setChildData(this, groupList);
    }


    @Override
    protected void executeOnChildPress(int groupNr, String group, int childNr) {
        ImplementedMethods.executeChildPress(this, groupNr, group, childNr);
    }

    @Override
    protected void executeOnGroupPress(int groupNr, String group) {
        ImplementedMethods.executeGroupPress(this, groupNr, group);
    }
}
