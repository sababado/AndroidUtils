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

/**
 * <p>A class that implements <code>Loading</code> means that the class has ways to show
 * or hide a loading indicator of some sort.</p>
 * <p>This is useful when communicating from a <code>Fragment</code> to an <code>Activity</code>
 * @author Robert J. Szabo
 * @since 08/27/2012
 * @version 1.0
 */
public interface Loading
{
	/**
	 * Call this method to show a loading indicator if not already showing.
	 */
	public void showLoading();
	/**
	 * call this method to hide a loading indicator if showing.
	 */
	public void hideLoading();
}
