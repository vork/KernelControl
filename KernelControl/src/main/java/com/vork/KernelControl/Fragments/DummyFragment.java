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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vork.KernelControl.R;

import uk.co.chrisjenx.paralloid.Parallaxor;

import static butterknife.ButterKnife.findById;

public class DummyFragment extends Fragment {
    String mName;

    public DummyFragment() {}
    public DummyFragment(String name) {
        mName = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dummy_fragment, container, false);

        TextView txtDummy = findById(rootView, R.id.txt_dummy);
        txtDummy.setText(mName);

        ScrollView scrollView = findById(rootView, R.id.scroll_view);

        if (scrollView instanceof Parallaxor) {
            ((Parallaxor) scrollView).parallaxViewBy(txtDummy, 0.25f);
        }

        return rootView;
    }

}