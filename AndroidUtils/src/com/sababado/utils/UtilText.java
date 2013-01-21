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

import android.text.SpannableString;
import android.text.style.UnderlineSpan;

/**
 * Text related Utility functions
 * @author Robert J. Szabo
 * @since 08/27/2012
 * @version 1.0
 */
public class UtilText
{
	/**
	 * Return a <code>String</code> as underlined.
	 * @param text
	 * @return
	 */
	public static SpannableString getUnderlinedContent(String text)
	{
		SpannableString content = new SpannableString(text);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		return content;
	}
	
	/**
	 * Trim empty space from the left of the the string.<br/>
	 * <b>Example:</b> <i>" string "</i> will become <i>"string "</i> 
	 * @param s The string to trim
	 * @return The trimmed string
	 */
	public static String trimLeft(String s) {
        return s.replaceAll("^\\s+", "");
    }
    
	/**
	 * Trim empty space from the right of the the string.<br/>
	 * <b>Example:</b> <i>" string "</i> will become <i>" string"</i> 
	 * @param s The string to trim
	 * @return The trimmed string
	 */
    public static String trimRight(String s) {
        return s.replaceAll("\\s+$", "");
    }
}
