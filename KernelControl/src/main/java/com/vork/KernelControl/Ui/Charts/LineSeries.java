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

package com.vork.KernelControl.Ui.Charts;

import java.util.ArrayList;

public class LineSeries {

    private int mColor;

    private ArrayList<LinePoint> mPoints = new ArrayList<LinePoint>();

    public LineSeries(int color) {
        mColor = color;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Appends a point to a line series. useful for realtime charts
     * <p/>
     * Don't use this function directly! Use the appendPointToLine from LineChart
     *
     * @param point
     * @param xStep step between each update
     */
    public void appendPoint(LinePoint point, float xStep) {
        for (LinePoint point1 : mPoints) {
            point1.increaseX(xStep);
        }
        mPoints.add(point);
    }

    public void deleteLastPoint() {
        mPoints.remove(0);
    }

    /**
     * Adds a point to a line series.
     *
     * @param point
     */
    public void addPoint(LinePoint point) {
        mPoints.add(point);
    }

    public ArrayList<LinePoint> getPoints() {
        return mPoints;
    }

    public int getSize() {
        return mPoints.size();
    }

    public int getColor() {
        return mColor;
    }

    public LinePoint getPoint(int index) {
        return mPoints.get(index);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // INNER CLASS
    ///////////////////////////////////////////////////////////////////////////////////////

    public static class LinePoint implements Comparable<LinePoint> {
        private float mX;
        private float mY;

        public LinePoint() {
        }

        public LinePoint(float x, float y) {
            mX = x;
            mY = y;
        }

        public float getY() {
            return mY;
        }

        public void set(float y, float x) {
            mY = y;
            mX = x;
        }

        public void increaseX(float step) {
            mX += step;
        }

        public float getX() {
            return mX;
        }

        @Override
        public int compareTo(LinePoint another) {
            return Double.compare(mX, another.mX);
        }
    }
}
