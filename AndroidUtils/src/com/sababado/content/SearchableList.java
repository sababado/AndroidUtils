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

package com.sababado.content;

import java.util.List;

import com.sababado.widget.FilterableBaseAdapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * This interface defines what a searchable {@link android.app.ListActivity ListActivity} or {@link android.app.ListFragment ListFragment} should consist of.
 * @author Robert J. Szabo
 * @since 01/20/2013
 * @version 1.0
 */
public interface SearchableList extends TextWatcher
{
	/**
	 * Convenience method to start a filter process given a
	 * {@link CharSequence}
	 * 
	 * @param s
	 *            What to filter the list by.
	 */
	public void onTextChanged(CharSequence s);

	/**
	 * Set the data type to monitor for the filterable list. The data must be of
	 * {@link java.io.Serializable Serializable} type so that it may be persisted on
	 * configuration changes. <b>This should be called AFTER {@link android.app.ListFragment#setListAdapter(android.widget.ListAdapter) setListAdapter(android.widget.ListAdapter)}</b>
	 * 
	 * @param listData
	 *            The data to show in the list
	 */
	public <T> void setListData(List<T> listData);

	/**
	 * Returns the list data managed by this object.
	 * 
	 * @return listData managed list data.
	 */
	public List<?> getListData();
	
	/**
	 * A filtered list of data is returned. This list does not have any relation to
	 * the data that might be visible on screen.
	 * <b>This is available if using a {@link FilterableBaseAdapter}.</b>
	 * 
	 * @return Filtered listData
	 */
	public List<?> getFilteredListData();

	/**
	 * Set the text of the searchable {@link android.widget.EditText EditText}
	 * 
	 * @param text
	 *            text to set.
	 */
	public void setSearchText(String text);

	/**
	 * Get the text of the searchable {@link android.widget.EditText EditText}
	 * 
	 * @return Search view's text.
	 */
	public Editable getSearchText();

	/**
	 * Set the text for when a filter has no results. <i><b>To incorporate the
	 * filter string in the text then include "{0}" (minus quotes) where the
	 * filter string should be.</b></i>
	 * 
	 * @param text
	 *            Text to set
	 */
	public void setEmptySearchString(String text);

	/**
	 * Get the text for the empty search string
	 * 
	 * @return The empty search string
	 */
	public String getEmptySearchString();

	/**
	 * Get the text for the empty list string
	 * 
	 * @return the empty list string.
	 */
	public String getEmptyListText();

	/**
	 * Set the text for the empty list string
	 * 
	 * @param text
	 *            Text to set.
	 */
	public void setEmptyListText(CharSequence text);

	/**
	 * Get the flag for whether or not all results will show on an empty search.
	 * <b>This is flag is implicitly used by an implementation of {@link FilterableBaseAdapter}</b>
	 * 
	 * @return true means show all, false means show empty search message.
	 */
	public boolean isShowAllOnEmpty();

	/**
	 * Set the flag for weather or not all results will show on an empty search.
	 * <b>This is flag is implicitly used by an implementation of {@link FilterableBaseAdapter}</b>
	 * 
	 * @param showAllOnEmpty
	 *            true means show all, false means show empty search message.
	 */
	public void setShowAllOnEmpty(boolean showAllOnEmpty);

	/**
	 * Toggle the visibility of the searchtext view.
	 * 
	 * @param clearText
	 *            To clear the text before showing or after hiding, this should
	 *            be true, or to do nothing it should be false.
	 * @return true if the {@link android.widget.EditText EditText} is showing, false if it is
	 *         hidden.
	 */
	public boolean toggleSearchTextViewVisibility(boolean clearText);
	
	/**
	 * Force a filter on the text in the layout's {@link android.widget.EditText EditText} "search view".
	 */
	public void forceFilter();
}
