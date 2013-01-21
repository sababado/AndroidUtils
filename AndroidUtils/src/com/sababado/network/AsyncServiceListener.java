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

package com.sababado.network;


import android.os.Bundle;


/**
 * A class must implement this interface to receive callbacks from an <code>AsyncServiceCallTask</code>.
 * @author Robert J. Szabo
 * @since 08/27/2012
 * @version 1.0
 */
public interface AsyncServiceListener
{
	/**
	 * Call back for when the <code>AsyncServiceCallTask</code> wants to relay progress updates about the service call.
	 * @param progress An array of strings, or various progress updates.
	 */
	public void onServiceCallProgressUpdate(String[] progress);
	
	/**
	 * Call back for when the <code>AsyncServiceCallTask</code> has successfully completed. The service call result object (an <code>ArrayList</code>)
	 * can be extracted from the bundle.
	 * @param success
	 */
	public void onServiceCallSuccess(Bundle success);
	
	/**
	 * Call back for when the <code>AsyncServiceCallTask</code> has failed.
	 * @param errMsg The fail message.
	 * @param errCode The error code associated with this failure.
	 */
	public void onServiceCallFailure(String errMsg, int errCode);
}
