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

package com.sababado.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Android View related Utility functions
 * @author Robert J. Szabo
 * @since 08/27/2012
 * @version 1.0
 */
public class UtilViews
{
	/**
	 * Help with garbage collection and destruction of unnecessary drawables for a view.
	 * @param view
	 */
	public static void unbindDrawables(View view) {
		if(view == null)
			return;
		
	    if (view.getBackground() != null) {
	        view.getBackground().setCallback(null);
	    }
	    if (view instanceof ViewGroup)
	    {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
	        {
	            unbindDrawables(((ViewGroup) view).getChildAt(i));
	        }
	        try
	        {
	        	((ViewGroup) view).removeAllViews();
	        }
	        catch (UnsupportedOperationException mayHappen)
	        {
	        	// AdapterViews, ListViews and potentially other ViewGroups don’t support the removeAllViews operation
        	}
	    }
	}
}
