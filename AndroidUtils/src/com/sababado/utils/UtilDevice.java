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

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Android device related Utility functions
 * @author Robert J. Szabo
 * @since 08/27/2012
 * @version 1.1
 */
public class UtilDevice
{
	/**
	 * Constant to determine if the device is pre-honeycomb (API 11)
	 */
	public static final boolean IS_PRE_HONEYCOMB = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;
	 /*
	  *  Original values Copied from Android docs, since we don't have these values in Froyo 2.2
	  *  The static block will ensure that the latest values are copied if they exist.
	  */
	//Temporary placeholder for the reverse port/land values.
	private static int screen_orientation_reverse_land_temp = 8;
	private static int screen_orientation_reverse_port_temp = 9;
    static
    {
    	try //to get the current values for the reverse port/land keys. This is necessary pre API 9
    	{
    		screen_orientation_reverse_land_temp = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
    		screen_orientation_reverse_port_temp = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
    	}
    	catch(Exception e)
    	{}
    }
    /**
     * Constant corresponding to reverseLandscape in the screenOrientation attribute.
     * This is tailored to work pre-API 9
     */
    public static final int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = screen_orientation_reverse_land_temp;
    /**
     * Constant corresponding to reversePortrait  in the screenOrientation attribute.
     * This is tailored to work pre-API 9
     */
    public static final int SCREEN_ORIENTATION_REVERSE_PORTRAIT = screen_orientation_reverse_port_temp;
    /*
     * End copy
     */
	/**
	 * Request to hide the soft input window from the context of the window that is currently accepting input.
	 * @param context
	 * @param view The view that has keyboard focus
	 */
	public static void hideKeyboard(Context context, View view)
	{
		InputMethodManager imm = (InputMethodManager)(context.getSystemService(Context.INPUT_METHOD_SERVICE));
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/**
	 * Request that the current input method's soft input area be shown to the user, if needed.
	 * @param context
	 * @param view The view that should get keyboard focus
	 */
	public static void showKeyboard(Context context, View view)
	{
		InputMethodManager imm = (InputMethodManager)(context.getSystemService(Context.INPUT_METHOD_SERVICE));
		imm.showSoftInput(view, 0);
	}
	
	/**
	 * Get the density pixel (dp) measurement of the width of this device
	 * @param activity The activity that is invoking this method
	 * @return density pixels as an integer
	 */
	@SuppressWarnings ("deprecation")
	public static int getDeviceDpWidth(Activity activity)
	{
		DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        float logicalDensity = metrics.density;
        int dp= (int)(display.getWidth() / logicalDensity +0.5);
        return dp;
	}
	
	/**
	 * Get the density pixel (dp) measurement of the height of this device
	 * @param activity The activity that is invoking this method
	 * @return density pixels as an integer
	 */
	@SuppressWarnings ("deprecation")
	public static int getDeviceDpHeight(Activity activity)
	{
		DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        float logicalDensity = metrics.density;
        int dp= (int)(display.getHeight() / logicalDensity +0.5);
        return dp;
	}
	
	/**
	 * Get the density pixel (dp) measurement of the width and height of this device
	 * @param activity The activity that is invoking this method
	 * @return density pixels as an integer array [width, height]
	 */
	@SuppressWarnings ("deprecation")
	public static int[] getDeviceDpDimensions(Activity activity)
	{
		DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        float logicalDensity = metrics.density;
        int dpWidth= (int)(display.getWidth() / logicalDensity +0.5);
        int dpHeight= (int)(display.getHeight() / logicalDensity +0.5);
        return new int[]{dpWidth,dpHeight};
	}
	
	/**
	 * Lock the current phone orientation; prevent future rotations on this Activity.
	 * @param activity The activity to prevent rotations on.
	 */
	public static void lockOrientation(Activity activity) {
		int orientation = activity.getResources().getConfiguration().orientation;
	    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
	        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	    else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
	        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    }
	    else if(orientation == SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
	        activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
	    }
	    else if(orientation == SCREEN_ORIENTATION_REVERSE_PORTRAIT){
	        activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_PORTRAIT);
	    }
	}

	/**
	 * Unlock an Activity's orientation and allow rotations to occur.
	 * @param activity The activity to unlock.
	 */
	public static void unlockOrientation(Activity activity) {
	    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}
}
