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

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * The purpose of this gallery is to slow down scrolling. Instead of speeding through each view in the gallery,
 * the gallery will scroll through as if the D-Pad left or right arrows are pressed.
 * @author Various Questions from StackOverflow (not sure who the original author is).
 * @since 08/27/2012
 * @version 1.0
 * @deprecated Please use <code>com.sababado.widget.SlowGallery</code>
 */
public class SlowGallery extends Gallery {
	
	public SlowGallery(Context context) {
		super(context);
	}
	public SlowGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SlowGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2)
	{
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
	  int kEvent;
	  if(isScrollingLeft(e1, e2)){ //Check if scrolling left
	    kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
	  }
	  else{ //Otherwise scrolling right
	    kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
	  }
	  onKeyDown(kEvent, null);
	  return true;  
	}

}
