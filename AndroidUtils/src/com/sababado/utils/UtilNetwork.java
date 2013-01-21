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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Android network related Utility functions
 * @author Robert J. Szabo
 * @since 08/27/2012
 * @version 1.0
 */
public class UtilNetwork
{
	/**
	 * Checks if there is an Internet connection
	 * @param context The context to use to check the Internet connection
	 * @return True if a connection exists, false if not.
	 */
	public static boolean isNetworkAvailable(Context context)
	{
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
}
