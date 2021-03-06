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

package com.vork.KernelControl.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.vork.KernelControl.R;
import com.vork.KernelControl.Ui.CardGridView;
import com.vork.KernelControl.Ui.Charts.LineChart;
import com.vork.KernelControl.Ui.Charts.LineSeries;
import com.vork.KernelControl.Ui.ObservableScrollView;
import com.vork.KernelControl.Utils.Preferences;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

import static butterknife.ButterKnife.findById;

public class DummyFragment extends Fragment implements Preferences, ObservableScrollView.Callbacks {
    private String mName;
    private LineChart mChart;
    private CardGridView mCardsGrid;
    private ObservableScrollView mObservableScrollView;
    private LinearLayout mChartContainer;
    private View mPlaceholderView;
    private int mMaxScrollY;

    public DummyFragment() {
    }

    public DummyFragment(String name) {
        mName = name;
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dummy_fragment, container, false);

        mObservableScrollView = findById(rootView, R.id.scroll_view);
        mObservableScrollView.setCallbacks(this);

        mChartContainer = findById(rootView, R.id.ll_chart);
        mPlaceholderView = findById(rootView, R.id.placeholder);

        mObservableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onScrollChanged(mObservableScrollView.getScrollY());
                        mMaxScrollY = mObservableScrollView.computeVerticalScrollRange()
                                - mObservableScrollView.getHeight();
                    }
                });

        mChart = findById(rootView, R.id.chartView);
        mChart.setRange(100.5f, 0, 59.5f, 0.5f);

        TextView txtChartTitle = findById(rootView, R.id.txt_chartViewTitle);
        txtChartTitle.setText(mName);

        TextView txtChartValue = findById(rootView, R.id.txt_curValue);
        txtChartValue.setText("2%");

        mCardsGrid = findById(rootView, R.id.card_list);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean darkUi = preferences.getBoolean(DARK_UI_PREF, false);

        for (int i = 0; i < 6; i++) {
            Card card = new Card(getActivity().getApplicationContext());
            if (darkUi) {
                card.setBackgroundResourceId(R.drawable.darkcard);
            }
            CardHeader header = new CardHeader(getActivity().getApplicationContext());
            header.setTitle("Card " + i);
            card.addCardHeader(header);
            card.setTitle("CardText");
            mCardsGrid.addCard(card);
        }
        mCardsGrid.commitCards();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mObservableScrollView = findById(view, R.id.scroll_view);

        mObservableScrollView.setClipToPadding(false);
        setInsets(getActivity(), mObservableScrollView);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int color = preferences.getInt(ACCENT_COLOR_PREF, getResources().getColor(R.color.accentBlue));
        LineSeries series = new LineSeries(color);

        LineSeries.LinePoint point = new LineSeries.LinePoint();
        point.set(2, 0);
        series.addPoint(point);

        point = new LineSeries.LinePoint();
        point.set(3, 2);
        series.addPoint(point);

        point = new LineSeries.LinePoint();
        point.set(10, 10);
        series.addPoint(point);

        point = new LineSeries.LinePoint();
        point.set(70, 30);
        series.addPoint(point);

        point = new LineSeries.LinePoint();
        point.set(50, 60);
        series.addPoint(point);

        mChart.drawLine(0, series);
        mChart.setLineToFill(0);
    }

    @Override
    public void onScrollChanged(int scrollY) {
        scrollY = Math.min(mMaxScrollY, scrollY);

        int translationY = mPlaceholderView.getTop() - scrollY;

        translationY = translationY < 0 ? 0 : translationY;

        mChartContainer.animate().cancel();
        mChartContainer.setTranslationY(translationY + scrollY);
    }
}