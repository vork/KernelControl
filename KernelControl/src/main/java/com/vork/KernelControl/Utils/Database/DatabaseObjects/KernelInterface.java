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

public class KernelInterface {
    int mId = 0;
    String mName = "";
    String mDescription = "";
    String mPath = "";
    String mValue = "";
    Boolean mSetOnBoot = false;

    public KernelInterface(int id, String name, String description, String path) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mPath = path;
    }

    public KernelInterface(int id, String name, String path) {
        this.mId = id;
        this.mName = name;
        this.mPath = path;
    }

    public KernelInterface(String name, String description, String path) {
        this.mName = name;
        this.mDescription = description;
        this.mPath = path;
    }

    public KernelInterface(String name, String path) {
        this.mName = name;
        this.mPath = path;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public Boolean getSetOnBoot() {
        return mSetOnBoot;
    }

    public void setSetOnBoot(Boolean setOnBoot) {
        mSetOnBoot = setOnBoot;
    }

}
