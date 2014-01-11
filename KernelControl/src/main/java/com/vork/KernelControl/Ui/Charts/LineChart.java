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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.vork.KernelControl.R;
import com.vork.KernelControl.Ui.Charts.Graphics.RectD;

import java.util.ArrayList;
import java.util.List;

public class LineChart extends View {

    private ArrayList<LineSeries> mLines = new ArrayList<LineSeries>();
    private int mLineToFill = -1;
    private boolean mRangeUserSet = false;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private boolean shouldUpdate = false;
    private Bitmap fullImage;
    private PorterDuffXfermode mXferMode = new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR);
    // Range
    private RectD mValueBounds = new RectD();
    private Rect mChartBounds = new Rect();
    private Rect mBounds = new Rect();
    //Styled Attributes
    private boolean mDrawGrid;
    private boolean mDrawBorder;
    private int mLineWidth;
    private int mGridLineColor;
    private int mOutlineColor;
    private int mGridLineWidth;
    private float mStepX;
    private float mStepY;
    private float mMaxX;
    private float mMaxY;
    private float mMinX;
    private float mMinY;
    private float mTextSize;
    private int mLabelColor;
    private String mLabelMeasureDescription;
    private String mLabelMax;
    private String mLabelMin;
    private String mLabelMaxTime;

    public LineChart(Context context) {
        this(context, null, 0);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.setWillNotDraw(false);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);

        mDrawGrid = attributes.getBoolean(R.styleable.LineChartView_drawGrid, true);
        mDrawBorder = attributes.getBoolean(R.styleable.LineChartView_drawBorder, true);
        mLineWidth = attributes.getDimensionPixelSize(R.styleable.LineChartView_lineWidth, 4);
        mGridLineColor = attributes.getColor(R.styleable.LineChartView_gridLineColor, Color.LTGRAY);
        //No extra outline color if it isn't specified
        mOutlineColor = attributes.getColor(R.styleable.LineChartView_gridOutlineColor, mGridLineColor);
        mGridLineWidth = attributes.getDimensionPixelSize(R.styleable.LineChartView_gridLineWidth, 1);
        mStepX = attributes.getFloat(R.styleable.LineChartView_gridStepX, -1);
        mStepY = attributes.getFloat(R.styleable.LineChartView_gridStepY, -1);
        mTextSize = attributes.getDimensionPixelSize(R.styleable.LineChartView_textSize, 0);
        mLabelColor = attributes.getColor(R.styleable.LineChartView_labelColor, mGridLineColor);
        mLabelMeasureDescription = attributes.getString(R.styleable.LineChartView_labelMeasureDescription);
        mLabelMaxTime = attributes.getString(R.styleable.LineChartView_labelMaxTime);
        mLabelMax = attributes.getString(R.styleable.LineChartView_labelMax);
        mLabelMin = attributes.getString(R.styleable.LineChartView_labelMin);

    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * The top label holds the label for what is measured and the maximum possible value
     *
     * @param measureLabel describes what is shown in the graph
     * @param maxLabel     the max possible value in y coordinates
     */
    public void setTopLabel(String measureLabel, String maxLabel) {
        final String[] values = new String[2];

        mLabelMeasureDescription = measureLabel;
        mLabelMax = maxLabel;
        shouldUpdate = true;
        requestLayout();
        invalidate();
    }

    /**
     * The bottom label holds the label for the maximum time that is shown and the start value
     *
     * @param maxTimeLabel label for the maximum shown time
     * @param startLabel   label for the start value of the graph
     */
    public void setBottomLabel(String maxTimeLabel, String startLabel) {
        final String[] values = new String[2];

        mLabelMaxTime = maxTimeLabel;
        mLabelMin = startLabel;
        shouldUpdate = true;
        requestLayout();
        invalidate();
    }

    public void removeAllLines() {
        while (mLines.size() > 0) {
            mLines.remove(0);
        }
        shouldUpdate = true;
        requestLayout();
        invalidate();
    }

    public void addLine(LineSeries line) {
        mLines.add(line);
        shouldUpdate = true;
        requestLayout();
        invalidate();
    }

    public void appendPointToLine(int indexOfLine, LineSeries.LinePoint point, float xStep) {
        if (mLines.get(indexOfLine).getSize() > (getMaxX() / xStep)) {
            mLines.get(indexOfLine).deleteLastPoint();
        }
        mLines.get(indexOfLine).appendPoint(point, xStep);
        shouldUpdate = true;
        requestLayout();
        invalidate();
    }

    public void drawLine(int indexOfLine, LineSeries line) {
        if (mLines.size() == 0) {
            mLines.add(indexOfLine, line);
        } else {
            mLines.set(indexOfLine, line);
        }
        shouldUpdate = true;
        requestLayout();
        invalidate();
    }

    public int getLineToFill() {
        return mLineToFill;
    }

    public void setLineToFill(int indexLineToFill) {
        mLineToFill = indexLineToFill;
        shouldUpdate = true;
        requestLayout();
        invalidate();
    }

    public void setRange(float maxY, float minY, float maxX, float minX) {
        mMaxX = maxX;
        mMinX = minX;
        mMaxY = maxY;
        mMinY = minY;
        mRangeUserSet = true;
    }

    public float getMaxY() {
        if (mRangeUserSet) {
            return mMaxY;
        } else {
            if (mLines.get(0).getSize() > 0) {
                mMaxY = mLines.get(0).getPoint(0).getY();
                for (LineSeries line : mLines) {
                    for (LineSeries.LinePoint point : line.getPoints()) {
                        if (point.getY() > mMaxY) {
                            mMaxY = point.getY();
                        }
                    }
                }
            } else {
                mMaxY = 0;
            }
            return mMaxY;
        }
    }

    public float getMaxX() {
        if (mRangeUserSet) {
            return mMaxX;
        } else {
            if (mLines.get(0).getSize() > 0) {
                mMaxX = mLines.get(0).getPoint(0).getX();
                for (LineSeries line : mLines) {
                    for (LineSeries.LinePoint point : line.getPoints()) {
                        if (point.getX() > mMaxX) {
                            mMaxX = point.getY();
                        }
                    }
                }
            } else {
                mMaxY = 0;
            }
            return mMaxX;
        }
    }

    public float getMinY() {
        if (mRangeUserSet) {
            return mMinY;
        } else {
            if (mLines.get(0).getSize() > 0) {
                mMinY = mLines.get(0).getPoint(0).getY();
                for (LineSeries line : mLines) {
                    for (LineSeries.LinePoint point : line.getPoints()) {
                        if (point.getY() < mMinY) {
                            mMinY = point.getY();
                        }
                    }
                }
            } else {
                mMaxY = 0;
            }
            return mMinY;
        }
    }

    public float getMinX() {
        if (mRangeUserSet) {
            return mMinX;
        } else {
            if (mLines.get(0).getSize() > 0) {
                mMinX = mLines.get(0).getPoint(0).getX();
                for (LineSeries line : mLines) {
                    for (LineSeries.LinePoint point : line.getPoints()) {
                        if (point.getX() < mMinX) {
                            mMinX = point.getX();
                        }
                    }
                }
            } else {
                mMaxY = 0;
            }
            return mMinX;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ///////////////////////////////////////////////////////////////////////////////////////

    private void drawGrid(Canvas canvas) {
        final int gridLinesHorizontal = (int) ((getMaxX() - getMinX()) / mStepX);
        final int gridLinesVertical = (int) ((getMaxY() - getMinY()) / mStepY);

        final float stepX = mChartBounds.width() / (float) (gridLinesHorizontal);
        final float stepY = mChartBounds.height() / (float) (gridLinesVertical);

        final float left = mChartBounds.left;
        final float top = mChartBounds.top;
        final float right = mChartBounds.right;
        final float bottom = mChartBounds.bottom;

        mPaint.setColor(mOutlineColor);
        mPaint.setStrokeWidth(mGridLineWidth);

        if (mDrawBorder) {
            canvas.drawLine(left, bottom, right, bottom, mPaint);
            canvas.drawLine(left, bottom, left, top, mPaint);
            canvas.drawLine(right, bottom, right, top, mPaint);
            canvas.drawLine(left, top, right, top, mPaint);
        }

        if (mDrawGrid) {
            mPaint.setColor(mGridLineColor);

            for (int i = 1; i < gridLinesHorizontal; i++) {
                canvas.drawLine(left + (stepX * i), top, left + (stepX * i), bottom, mPaint);
            }

            for (int i = 1; i < gridLinesVertical; i++) {
                canvas.drawLine(left, top + (stepY * i), right, top + (stepY * i), mPaint);
            }
        }
    }

    private void drawLabels(Canvas canvas) {
        Typeface robotoLight = Typeface.createFromAsset(getContext().getAssets(),
                "roboto_light.ttf");

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(mLabelColor);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(mTextSize);

        int w;
        int pos;

        final float dp = getResources().getDisplayMetrics().density;

        Rect bounds = new Rect();
        if (mLabelMeasureDescription != null || mLabelMax != null) {
            textPaint.getTextBounds(mLabelMax, 0, mLabelMax.length(), bounds);
            w = bounds.right - bounds.left;
            pos = (int) (mBounds.right - w - dp * 3);

            canvas.drawText(mLabelMax, pos, mBounds.top + mTextSize, textPaint);

            pos = (int) (mBounds.left + dp * 3);
            canvas.drawText(mLabelMeasureDescription, pos, mBounds.top + mTextSize, textPaint);
        }
        if (mLabelMaxTime != null || mLabelMin != null) {
            bounds = new Rect();
            textPaint.getTextBounds(mLabelMin, 0, mLabelMin.length(), bounds);
            w = bounds.right - bounds.left;
            pos = (int) (mBounds.right - w - dp * 3);

            canvas.drawText(mLabelMin, pos, mBounds.bottom, textPaint);

            pos = (int) (mBounds.left + dp * 3);
            canvas.drawText(mLabelMaxTime, pos, mBounds.bottom, textPaint);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // OVERRIDES
    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float dp = getResources().getDisplayMetrics().density;

        final int gridLeft = (int) (mGridLineWidth - 1 * dp);

        int gridTop;
        if (mLabelMeasureDescription != null || mLabelMax != null) {
            gridTop = (int) (mTextSize + (5 * dp) + mGridLineWidth - 1);
        } else {
            gridTop = (int) (mGridLineWidth - 1);
        }

        final int gridRight = (int) (getWidth() - mGridLineWidth + 1 * dp);

        int gridBottom;
        if (mLabelMaxTime != null || mLabelMin != null) {
            gridBottom = (int) (getHeight() - mTextSize - (2 * dp) - mGridLineWidth);
        } else {
            gridBottom = getHeight() - mGridLineWidth;
        }

        mChartBounds.set(gridLeft, gridTop, gridRight, gridBottom);

        mBounds.set(0, 0, getWidth(), getHeight());

        if (fullImage == null || shouldUpdate) {
            fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas ca = new Canvas(fullImage);

            //only draw them if we have label set
            if (mLabelMax != null || mLabelMin != null || mLabelMeasureDescription != null ||
                    mLabelMaxTime != null) {
                drawLabels(ca);
            }

            ca.save();
            ca.clipRect(mChartBounds.left + mGridLineWidth + 1, mChartBounds.top + mGridLineWidth,
                    mChartBounds.right - mGridLineWidth, mChartBounds.bottom - mGridLineWidth);

            int lineCount = 0;
            if (mLineToFill != -1) {
                for (LineSeries line : mLines) {
                    int count = 0;
                    float lastXPixels = 0, newYPixels = 0;
                    float lastYPixels = 0, newXPixels = 0;
                    float maxY = getMaxY();
                    float minY = getMinY();
                    float maxX = getMaxX();
                    float minX = getMinX();

                    mPaint.reset();

                    if (lineCount == mLineToFill) {
                        mPaint.setColor(line.getColor());
                        mPaint.setAlpha(90);
                        mPaint.setStrokeWidth(2 * dp);

                        boolean firstSet = false;
                        Path linePath = new Path();
                        for (LineSeries.LinePoint point : line.getPoints()) {
                            float yPercent = (point.getY() - minY) / (maxY - minY);
                            float xPercent = (point.getX() - minX) / (maxX - minX);
                            if (!firstSet) {
                                lastXPixels = mChartBounds.right - xPercent * mChartBounds.right;
                                lastYPixels = mChartBounds.bottom - (mChartBounds.bottom * yPercent);
                                linePath.moveTo(lastXPixels, mChartBounds.bottom - (
                                        mChartBounds.bottom * (minY / (maxY - minY))));
                                linePath.lineTo(lastXPixels, lastYPixels);
                                firstSet = true;
                            } else {
                                newXPixels = mChartBounds.right - (xPercent * mChartBounds.right);
                                newYPixels = mChartBounds.bottom - (mChartBounds.bottom * yPercent);
                                linePath.lineTo(newXPixels, newYPixels);
                            }
                        }
                        linePath.lineTo(newXPixels, mChartBounds.bottom - (
                                mChartBounds.bottom * (minY / (maxY - minY))));
                        linePath.close();

                        ca.drawPath(linePath, mPaint);

//                        mPaint.reset();
//                        mPaint.setXfermode(mXferMode);

//                        ca.drawPath(erasePath, mPaint);

                        mPaint.reset();
                    }
                    lineCount++;
                }
            }

            drawGrid(ca);

            mPaint.reset();
            mPaint.setAntiAlias(true);

            for (LineSeries line : mLines) {
                int count = 0;
                float lastXPixels = 0, newYPixels = 0;
                float lastYPixels = 0, newXPixels = 0;
                float maxY = getMaxY();
                float minY = getMinY();
                float maxX = getMaxX();
                float minX = getMinX();

                mPaint.setColor(line.getColor());
                mPaint.setStrokeWidth(mLineWidth);

                for (LineSeries.LinePoint p : line.getPoints()) {
                    float yPercent = (p.getY() - minY) / (maxY - minY);
                    float xPercent = (p.getX() - minX) / (maxX - minX);
                    if (count == 0) {
                        lastXPixels = mChartBounds.right - (xPercent * mChartBounds.right);
                        lastYPixels = mChartBounds.bottom - (mChartBounds.bottom * yPercent);
                    } else {
                        newXPixels = mChartBounds.right - (xPercent * mChartBounds.right);
                        newYPixels = mChartBounds.bottom - (mChartBounds.bottom * yPercent);
                        ca.drawLine(lastXPixels, lastYPixels, newXPixels, newYPixels, mPaint);
                        lastXPixels = newXPixels;
                        lastYPixels = newYPixels;
                    }
                    count++;
                }

            }

            ca.restore();
            shouldUpdate = false;
        }

        canvas.drawBitmap(fullImage, 0, 0, null);
    }
}
