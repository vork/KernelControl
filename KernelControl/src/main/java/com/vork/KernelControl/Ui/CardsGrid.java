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

package com.vork.KernelControl.Ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.stericson.RootTools.RootTools;
import com.vork.KernelControl.BuildConfig;
import com.vork.KernelControl.R;

import java.util.ArrayList;

public class CardsGrid extends RelativeLayout {
    /**
     * Disables stretching.
     */
    public static final int NO_STRETCH = 0;
    /**
     * Stretches the spacing between columns.
     */
    public static final int STRETCH_SPACING = 1;
    /**
     * Stretches columns.
     */
    public static final int STRETCH_COLUMN_WIDTH = 2;
    /**
     * Stretches the spacing between columns. The spacing is uniform.
     */
    public static final int STRETCH_SPACING_UNIFORM = 3;
    /**
     * Creates as many columns as can fit on screen.
     */
    public static final int AUTO_FIT = -1;
    int mWidthMeasureSpec = 0;
    //StyledAttributes
    private int mNumColumns;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mStretchMode;
    private int mColumnWidth;
    private int mRequestedColumnWidth;
    private int mRequestedNumColumns;
    private int mRequestedHorizontalSpacing;
    private ArrayList<LinearLayout> mViewList;
    private ArrayList<Integer> mHeightCalculation;
    private int[][] mIdStorage;

    public CardsGrid(Context context) {
        this(context, null, 0);
    }

    public CardsGrid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardsGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CardsGrid);

        if (attributes != null) {
            int numCols = attributes.getInt(R.styleable.CardsGrid_numColumns, -1);
            setNumColumns(numCols);

            int hSpace = attributes.getDimensionPixelOffset(R.styleable.CardsGrid_horizontalSpacing, 0);
            RootTools.log("hSpace: " + hSpace);
            setHorizontalSpacing(hSpace);

            int vSpace = attributes.getDimensionPixelOffset(R.styleable.CardsGrid_verticalSpacing, 0);
            RootTools.log("vSpace: " + vSpace);
            setVerticalSpacing(vSpace);

            int index = attributes.getInt(R.styleable.CardsGrid_stretchMode, STRETCH_SPACING);
            if (index >= 0) {
                setStretchMode(index);
            }

            int colWidth = attributes.getDimensionPixelOffset(R.styleable.CardsGrid_columnWidth, -1);
            RootTools.log("colWidth: " + colWidth);
            if (colWidth > 0) {
                setColumnWidth(colWidth);
            }

            attributes.recycle();
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public CardsGrid addCard(LinearLayout card) {
        if (mViewList == null) {
            mViewList = new ArrayList<LinearLayout>();
        }
        mViewList.add(card);

        return this;
    }

    public void commitCards() {
        addCardsToView();
        requestLayout();
        invalidate();
    }

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        if (horizontalSpacing != mRequestedHorizontalSpacing) {
            mRequestedHorizontalSpacing = horizontalSpacing;
            requestLayout();
            invalidate();
        }
    }

    public int getRequestedHorizontalSpacing() {
        return mRequestedHorizontalSpacing;
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing != mVerticalSpacing) {
            mVerticalSpacing = verticalSpacing;
            requestLayout();
            invalidate();
        }
    }

    public int getStretchMode() {
        return mStretchMode;
    }

    public void setStretchMode(int stretchMode) {
        if (stretchMode != mStretchMode) {
            mStretchMode = stretchMode;
            requestLayout();
            invalidate();
        }
    }

    public int getColumnWidth() {
        return mColumnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        if (columnWidth != mRequestedColumnWidth) {
            mRequestedColumnWidth = columnWidth;
            RootTools.log("RequestedColumnWidth: " + mRequestedColumnWidth);
            requestLayout();
            invalidate();
        }
    }

    public int getRequestedColumnWidth() {
        return mRequestedColumnWidth;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    public void setNumColumns(int numColumns) {
        if (numColumns != mRequestedNumColumns) {
            mRequestedNumColumns = numColumns;
            requestLayout();
            invalidate();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // OVERWRITTEN METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int paddingLeft = this.getPaddingLeft();
        int paddingRight = this.getPaddingRight();
        int paddingTop = this.getPaddingTop();
        int paddingBottom = this.getPaddingBottom();

//        if (widthMode == MeasureSpec.UNSPECIFIED) {
//            if (mColumnWidth > 0) {
//                widthSize = mColumnWidth + paddingLeft + paddingRight;
//            } else {
//                widthSize = paddingLeft + paddingRight;
//            }
//            widthSize += getVerticalScrollbarWidth();
//        }

        widthSize += paddingLeft + paddingRight + getVerticalScrollbarWidth();

        int childWidth = widthSize - paddingLeft - paddingRight;
        boolean didNotInitiallyFit = determineColumns(childWidth);

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            RootTools.log("HeightMode: UNSPECIFIED");
            mHeightCalculation = new ArrayList<Integer>();
            for (int i = 0; i < mNumColumns; i++) {
                mHeightCalculation.add(0);
            }

            int height = 0;
            int row = 0;
            View child = null;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                if (child != null) {
                    row = i % mNumColumns;
                    height = child.getMeasuredHeight();
                    height += mHeightCalculation.get(row);
                    height += getVerticalSpacing();
                    mHeightCalculation.set(row, height);
                }
            }

            heightSize = getMaxRowHeight() + paddingBottom + paddingTop +
                    getVerticalFadingEdgeLength() * 2;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            RootTools.log("HeightMode: AT_MOST");
        }

        if (widthMode == MeasureSpec.AT_MOST && mRequestedNumColumns != AUTO_FIT) {
            int ourSize = (mRequestedNumColumns * mColumnWidth)
                    + ((mRequestedNumColumns - 1) * mHorizontalSpacing)
                    + paddingLeft + paddingRight;
            if (ourSize > widthSize || didNotInitiallyFit) {
                widthSize |= MEASURED_STATE_TOO_SMALL;
            }
        }

        if (BuildConfig.DEBUG) {
            Display display = ((WindowManager) getContext().
                    getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            RootTools.log("Width Size: " + widthSize + " from: " + width + " height: " + heightSize + "" +
                    " from: " + height + " columns width: " + mColumnWidth);
        }
        setMeasuredDimension(widthSize, heightSize);

        super.onMeasure(getMeasuredWidthAndState(), getMeasuredHeightAndState());
        mWidthMeasureSpec = widthMeasureSpec;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if the space is enough for the desired views
     *
     * @param availableSpace the available space for the view
     * @return true if the space was enough else false
     */
    private boolean determineColumns(int availableSpace) {
        final int requestedHorizontalSpacing = mRequestedHorizontalSpacing;
        final int stretchMode = mStretchMode;
        int requestedColumnWidth = mRequestedColumnWidth;
        boolean didNotInitiallyFit = false;
        if(requestedColumnWidth >= availableSpace) { //Fix for too big layouts
            Crashlytics.log("available space is smaller than the requested column width");
            Crashlytics.setInt("Available Space for CardsGrid", availableSpace);
            Crashlytics.setInt("Requested Column Width", requestedColumnWidth);
            requestedColumnWidth = availableSpace;
            didNotInitiallyFit = true;
        }

        if (mRequestedNumColumns == AUTO_FIT) {
            if (requestedColumnWidth > 0) {
                mNumColumns = (availableSpace + requestedHorizontalSpacing) /
                        (requestedColumnWidth + requestedHorizontalSpacing);
                mRequestedNumColumns = mNumColumns;
            } else {
                // Just make up a number if we don't have enough info
                mNumColumns = 2;
                mRequestedNumColumns = mNumColumns;
            }
        } else {
            // We picked the columns
            mNumColumns = mRequestedNumColumns;
        }

        if (mNumColumns <= 0) {
            mNumColumns = 1;
        }

        switch (stretchMode) {
            case NO_STRETCH:
                mColumnWidth = requestedColumnWidth;
                mHorizontalSpacing = requestedHorizontalSpacing;
                break;
            default:
                int spaceLeftOver = availableSpace - (mNumColumns * requestedColumnWidth) -
                        ((mNumColumns - 1) * requestedHorizontalSpacing);

                if (spaceLeftOver < 0) {
                    didNotInitiallyFit = true;
                }

                switch (stretchMode) {
                    case STRETCH_COLUMN_WIDTH:
                        // Stretch the columns
                        mColumnWidth = requestedColumnWidth + spaceLeftOver / mNumColumns;
                        mHorizontalSpacing = requestedHorizontalSpacing;
                        break;

                    case STRETCH_SPACING:
                        // Stretch the spacing between columns
                        mColumnWidth = requestedColumnWidth;
                        if (mNumColumns > 1) {
                            mHorizontalSpacing = requestedHorizontalSpacing +
                                    spaceLeftOver / (mNumColumns - 1);
                        } else {
                            mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                        }
                        break;

                    case STRETCH_SPACING_UNIFORM:
                        // Stretch the spacing between columns
                        mColumnWidth = requestedColumnWidth;
                        if (mNumColumns > 1) {
                            mHorizontalSpacing = requestedHorizontalSpacing +
                                    spaceLeftOver / (mNumColumns + 1);
                        } else {
                            mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                        }
                        break;
                }

                break;
        }

        return didNotInitiallyFit;
    }

    private void addCardsToView() {
        //Layout position
        mIdStorage = new int[mNumColumns][mViewList.size()];

        LayoutParams lay;
        for (int i = 0; i < mViewList.size(); i++) {
            lay = null;
            final LinearLayout card = mViewList.get(i);
            int id = i + 1 + 100;
            int count = i + 1;
            int totalCardCount = i; //Total Card Count before adding the card

            ViewGroup parent = (ViewGroup) card.getParent();
            if (parent != null) {
                parent.removeView(card);
            }

            card.setId(id);


            RootTools.log("Card Id: " + card.getId());
            lay = new LayoutParams(getColumnWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            lay.setMargins(getHorizontalSpacing() / 2, getVerticalSpacing(),
                    getHorizontalSpacing() / 2, getVerticalSpacing());

            int row = totalCardCount % mNumColumns;

            int col = (int) Math.ceil(((double) count) / ((double) mNumColumns)) - 1;
            mIdStorage[row][col] = id;

            if (totalCardCount != 0) {
                if (count <= mNumColumns) { //Add the first cards next to each other
                    int prevId = mIdStorage[row - 1][col];
                    lay.addRule(RIGHT_OF, prevId);
                    RootTools.log("Adding " + id + " right of " + prevId);
                } else {
                    int prevId = mIdStorage[row][col - 1];
                    lay.addRule(BELOW, prevId);
                    if (row != 0) {
                        prevId = mIdStorage[row - 1][col];
                        lay.addRule(RIGHT_OF, prevId);
                    }
                    RootTools.log("Adding " + id + " below " + prevId);
                }
            } else {
                lay.addRule(ALIGN_PARENT_START, TRUE);
                RootTools.log("Adding " + id + " as first card");
            }

            this.addView(card, lay);
        }
    }

    private int getMaxRowHeight() {
        if (mHeightCalculation == null) {
            return 0;
        }
        int toRet = mHeightCalculation.get(0);
        for (int i = 0; i < mHeightCalculation.size(); i++) {
            if (toRet < mHeightCalculation.get(i)) {
                toRet = mHeightCalculation.get(i);
            }
        }
        toRet += getVerticalSpacing() * 4;
        return toRet;
    }
}
