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
