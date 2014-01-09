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
import java.util.List;

public class InterfaceTypes {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a ListView from every interface in a folder and reads the value
     */
    public static class FolderListViewInterface extends Base {


        public FolderListViewInterface() {};
    }

    /**
     * If one interface has multiple values - seperated by a char - it will add them to a ListView
     */
    public static class MultiValueListViewInterface {

        public MultiValueListViewInterface() {};
    }

    /**
     * Creates a SeekBar
     */
    public static class SeekBarKernelInterface extends SelectableValue {

        /**
         * Create a SeekBar
         */
        public SeekBarKernelInterface() {}
    }

    /**
     * Creates an EditText
     */
    public static class EditTextKernelInterface extends SelectableValue {

        /**
         * Creates an EditText interface
         */
        public EditTextKernelInterface() {}
    }

    /**
     * Creates an Switch
     */
    public static class SwitchKernelInterface extends KInterface {

        /**
         * Creates a Switch with a summary which toggles with the state.
         */
        public SwitchKernelInterface() {}
    }

    /**
     * Creates a Spinner
     */
    public static class SpinnerInterface extends KInterface {
        public ArrayList<String> spinnerOptions = new ArrayList<String>();
        public ArrayList<String> spinnerValue = new ArrayList<String>();
        public String availableOptionsPath = " ";

        /**
         * Creates a spinner with a fixed but unexposed number of options. Use this if the value of
         * the interface is cryptic and you want to add a description.
         */
        public SpinnerInterface() {}

    }

    public static class Category extends Base {
        private List<Base> inCategory;
        /**
         * Creates a category
         */
        public Category() {}

        public Base get(int index) {
            if(inCategory == null) {
                return null;
            }
            return inCategory.get(index);
        }

        public void add(Base kernelInterface) {
            if(inCategory == null) {
                inCategory = new ArrayList<Base>();
            }
            inCategory.add(kernelInterface);
        }

        public void remove(Base kernelInterface) {
            if(inCategory == null) {
                inCategory = new ArrayList<Base>();
            }
            inCategory.remove(kernelInterface);
        }

        public void remove(int index) {
            if(inCategory == null) {
                inCategory = new ArrayList<Base>();
            }
            inCategory.remove(index);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Nothing to see here
     */
    private static abstract class SelectableValue extends KInterface {
        public int possibleStep;
        public int minValue;
        public int maxValue;

        public SelectableValue() {}
    }

    /**
     * Nothing to see here
     */
    private static abstract class KInterface extends Base {
        public String interfacePath;
        public String summary;
        public String summaryOff;
        public String summaryOn;

        public KInterface() {}
    }

    /**
     * Nothing to see here
     */
    public static abstract class Base {
        public String name;

        public Base() {}
    }
}
