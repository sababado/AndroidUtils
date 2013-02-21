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

package com.sababado.widget;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.sababado.content.SearchableList;

/**
 * This class provides a {@link BaseAdapter} with {@link Filterable} implementation.
 * Most of the standard adapter methods are overriden to work with the {@link Filterable} data set.
 * @author Robert Szabo
 * @since 01/20/2013
 * @version 1.0
 */
public abstract class FilterableBaseAdapter extends BaseAdapter implements Filterable
{
	private static final String TAG = "FilterableBaseAdapter";
	//This adapter's filter.
	private GenericLists<?>.ListFilter filter;
	// Object to hold the list data.
	private GenericLists<?> mGenericLists;
	//Reference to the SearchableList that this adapter belongs to.
	private SearchableList mSearchableList;
	
	/**
	 * Create a new {@link FilterableBaseAdapter} with a reference to the {@link SearchableList} fragment/activity.
	 * @param searchableListFragment Reference to the {@link SearchableList} fragment/activity.
	 * @param listData The data to show in the list and filter.
	 */
	public <T> FilterableBaseAdapter(SearchableList searchableListFragment, List<T> listData)
	{
		mSearchableList = searchableListFragment;
		
		//try setting the list data.
		try
		{
			if (listData.get(0) != null && listData.get(0) != null)
			{
				// make sure data is Serializable
				if (!(listData.get(0) instanceof Serializable))
					throw new RuntimeException("Exception in Searchable List: List data must be of a Serializable type.");
			}
			mGenericLists = new GenericLists<T>(listData);
		}
		catch(NullPointerException e)
		{
		}
	}

	/**
	 * Get how many filtered items are represented by this adapter.
	 * @return how many filtered items are represented by this adapter.
	 */
	@Override
	public final int getCount()
	{
		if(mGenericLists == null || mGenericLists.mFilteredListData == null)
			return 0;
		return mGenericLists.mFilteredListData.size();
	}

	/**
	 * Get the data item associated with the specified position in the filtered data set.
	 * @param Position of the item whose data we want within the adapter's data set.
	 * @return The data at the specified position of the filtered data set.
	 */
	@Override
	public final Object getItem(int position)
	{
		if(mGenericLists == null || mGenericLists.mFilteredListData == null)
			return null;
		return mGenericLists.mFilteredListData.get(position);
	}

	/**
	 * Get a View that displays the data at the specified position in the data set. <b>To get items from the filtered data set use the {@link #getItem(int)} method.</b>
	 * You can either create a {@link View} manually or inflate it from an XML layout file. When the {@link View} is inflated, the parent {@link View} ({@link android.widget.GridView GridView}, {@link android.widget.ListView ListView}...) will apply default layout parameters unless you use {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean) inflate(int, android.view.ViewGroup, boolean)} to specify a root view and to prevent attachment to the root.
	 * @param position The position of the item within the adapter's data set of the item whose view we want.
	 * @param convertView The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this {@link View} is always of the right type (see {@link #getViewTypeCount()} and {@link #getItemViewType(int)}).
	 * @param parent The parent that this view will eventually be attached to
	 * @return A {@link View} corresponding to the data at the specified position.
	 */
	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	@Override
	public Filter getFilter()
	{
		if (filter == null)
			filter = mGenericLists.getListFilter();
		return filter;
	}
	
	/**
	 * This should be called in order to reduce the memory footprint of the
	 * lists before the {@link android.app.Fragment Fragment}'s state saves
	 */
	public Serializable saveInstanceState()
	{
		mGenericLists.saveInstanceState();
		return mGenericLists;
	}

	/**
	 * This should be called after this object is restored from the
	 * {@link android.app.Fragment Fragment} resume.
	 */
	public void restoreState(Serializable savedState)
	{
		mGenericLists = (GenericLists<?>)savedState;
		mGenericLists.restoreState();
	}
	
	/**
	 * Alter the data set of this list. Be sure to call {@link #notifyDataSetChanged()} after this to refresh the list.
	 * @param listData
	 */
	public <T> void setListData(List<T> listData)
	{
		Log.v(TAG, "listData: "+(listData == null));
		if(listData == null)
			mGenericLists = null;
		else
			mGenericLists = new GenericLists<T>(listData);
	}
	
	/**
	 * Get the Original list data managed by this adapter
	 * @return
	 */
	public List<?> getListData()
	{
		if(mGenericLists == null)
			return null;
		return mGenericLists.mListData;
	}
	
	/**
	 * Get the Filtered list data managed by this adapter.
	 * @return
	 */
	public List<?> getFilteredListData()
	{
		return mGenericLists.mFilteredListData;
	}
	
	/**
	 * Callback to filter data based on a constraint. Filter the data passed in the <code>listData</code> parameter based on the <code>constraint</code>.
	 * The filtered list should be returned.
	 * <b>The <code>constraint</code> parameter will <i>NOT</i> be null</b>.
	 * @param listData A copy of the data to filter.
	 * @param constraint Non null string to filter the data by.
	 * @return The filtered List. Return <code>null</code> signify an empty filtered list.
	 */
	public abstract List<?> performFiltering(List<?> listData, CharSequence constraint);
	
	/**
	 * Wrapper around the lists so that the lists can be generic.
	 * 
	 * @author Robert Szabo
	 * @since 01/20/2013
	 * @version 1.0
	 * 
	 * @param <T>
	 *            Type of the Lists' data
	 */
	protected class GenericLists<T> implements Serializable
	{
		private static final long serialVersionUID = 1L;
		/**
		 * The original data list.
		 */
		List<T> mListData;
		/**
		 * The filtered data list
		 */
		List<T> mFilteredListData;

		/**
		 * Create a new object with a given list of data. This data is set as the base list.
		 * @param listData Data to use.
		 */
		public GenericLists(List<T> listData)
		{
			mListData = listData;
			mFilteredListData = new ArrayList<T>();
			mFilteredListData.addAll(mListData);
		}

		/**
		 * This should be called in order to reduce the memory footprint of the
		 * lists before the {@link android.app.Fragment Fragment}'s state saves
		 * 
		 * @return Return's {@link FilterableBaseAdapter this} object for convenience.
		 */
		public GenericLists<T> saveInstanceState()
		{
			mFilteredListData = null;
			return this;
		}

		/**
		 * This should be called after this object is restored from the
		 * {@link android.app.Fragment Fragment}'s resume.
		 */
		public void restoreState()
		{
			mFilteredListData = new ArrayList<T>();
			mFilteredListData.addAll(mListData);
		}

		/**
		 * Factory method to get the List Filter.
		 * 
		 * @return
		 */
		public ListFilter getListFilter()
		{
			return new ListFilter();
		}

		/**
		 * Performs filtering grunt work tasks. Calls a callback in the
		 * {@link FilterableBaseAdapter} to filter a given list.
		 * 
		 * @author Robert Szabo
		 * 
		 */
		private class ListFilter extends Filter
		{

			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				FilterResults retval = new FilterResults();
				retval.values = mListData;
				retval.count = mListData.size();
				if (constraint != null && constraint.toString().length() > 0)
				{
					//Prevent aliasing to ruin this data array
					List<T> dataCopy = new ArrayList<T>(mListData);
					List<?> filt = FilterableBaseAdapter.this.performFiltering(dataCopy, constraint);
					if(filt == null) //empty list
						filt = new ArrayList<T>();
					retval.count = filt.size();
					retval.values = filt;
				}
				return retval;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
			{
				mFilteredListData.clear();

				List<T> resultsValues = (List<T>) results.values;
				
				//build a boolean to say if the list is empty or not
				boolean isEmpty = resultsValues == null || resultsValues.size() < 1;

				if(isEmpty)
				{
					if (mSearchableList.isShowAllOnEmpty())
					{
						// if the flag is true
						// show all results.
						Log.v("FilterableBaseAdapter", "show all on empty");
						resultsValues = mListData;
					}
					else if (!mSearchableList.isShowAllOnEmpty())
					{
						//init to empty
						resultsValues = new ArrayList<T>(0);
						// if the flag is false
						if (constraint.length() > 0)
						{
							// and the search string isn't empty
							// then show the empty search message.
							Log.v("FilterableBaseAdapter", "show empty message on search");
							mSearchableList.setEmptyListText(MessageFormat.format(mSearchableList.getEmptySearchString(), constraint));
						}
						else
						{
							// and the search string IS empty
							// show the default empty text message
							Log.v("FilterableBaseAdapter", "show empty message in general");
							mSearchableList.setEmptyListText(mSearchableList.getEmptyListText());
						}
					}
				} //end if empty check

				notifyDataSetChanged();

				for (T t : resultsValues)
					mFilteredListData.add(t);
				notifyDataSetInvalidated();
			}
		}
	}
}