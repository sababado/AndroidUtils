/*
 * Copyright [2012] [Robert James Szabo]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.sababado.views;

import com.sababado.utils.UtilText;

import android.content.Context;  
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * 
 * The purpose of this custom AutoCompleteTextView is to allow the suggestion box to show as soon as it is focused.
 * Destil's class was slightly modified in the <code>onFocusChanged</code> function to strip any leading white space from the filter.
 * @author Destil (stackoverflow.com user)
 * @author Modified by Robert J. Szabo
 * @since 08/27/2012
 * @version 1.0
 * @deprecated Please use <code>com.sababado.widget.InstantAutoComplete</code>
 */
public class InstantAutoComplete extends AutoCompleteTextView {

    public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
        	Editable text = getText();
        	if(text != null)
        	{
        		try
        		{
        			performFiltering(UtilText.trimLeft(text.toString()), 0);
        		}
        		catch(Exception e)
        		{}
        	}
        }
    }
}