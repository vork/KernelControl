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

package com.vork.KernelControl.Utils.Database.DatabaseObjects;

import java.util.ArrayList;

public class Profile {
    int mId = 0;
    String mName = "";
    ArrayList<Integer> mInterfaceId = null;

    public Profile(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public Profile(String name) {
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<Integer> getInterfaceId() {
        return mInterfaceId;
    }

    public void setInterfaceId(ArrayList<Integer> interfaceId) {
        mInterfaceId = interfaceId;
    }
}
