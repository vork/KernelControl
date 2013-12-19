/*******************************************************************************
 * Copyright 2013 NEGU Soft
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.negusoft.holoaccent.preference;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Window;

import com.negusoft.holoaccent.dialog.DividerPainter;

public class EditTextPreference extends android.preference.EditTextPreference {

	private final DividerPainter mPainter;

	public EditTextPreference(Context context) {
		super(context);
		mPainter = new DividerPainter(context);
	}

	public EditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPainter = new DividerPainter(context);
	}

	public EditTextPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPainter = new DividerPainter(context);
	}
	
	@Override
	protected void showDialog(Bundle state) {
		super.showDialog(state);
		Window w = getDialog().getWindow();
		mPainter.paint(w);
	}
}
