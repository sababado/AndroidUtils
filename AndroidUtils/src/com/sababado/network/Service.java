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


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Extend this class to define a service call which will be made by an {@link AsyncServiceCallTask}.
 * @author Robert J. Szabo
 * @since 11/21/2012
 * @version 1.1
 * @updates 
 * 1.1 added constant parameters.<br/>
 */
public abstract class Service
{
	private String url;
	private int callType;
	private String[] paramNames;
	private String[] paramValues;
	private String[] constantParamNames;
	private String[] constantParamValues;
	
	public static final int CALL_TYPE_GET = 0;
	public static final int CALL_TYPE_POST = 1;
	public static final int CALL_TYPE_DELETE = 2;
	public static final int CALL_TYPE_PUT = 3;
	
	/**
	 * Initialize the service call with data
	 * @param url Example: <code>http://www.exampleurl.com/path/to/servicecall</code>
	 * @param callType Use public int types from the {@link Service} class like {@link Service#CALL_TYPE_GET}
	 * @param paramNames an array containing parameter names
	 * @param paramValues an array containing corresponding parameter values
	 */
	public Service(String url, int callType, String[] paramNames, String[] paramValues)
	{
		super();
		this.url = url;
		this.callType = callType;
		this.paramNames = paramNames;
		this.paramValues = paramValues;
	}
	
	/**
	 * Get the url that is used to make the service call
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}
	
	/**
	 * Set the url used to make the service call<br/>
	 * Example: <code>http://www.exampleurl.com/path/to/servicecall</code>
	 * @param url
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	/**
	 * Get the call type used by this service call
	 * @return 
	 */
	public int getCallType()
	{
		return callType;
	}
	/**
	 * Set the call type to be used by the service call
	 * <br/>Example: {@link Service#CALL_TYPE_POST}
	 * @param callType
	 */
	public void setCallType(int callType)
	{
		this.callType = callType;
	}

	/**
	 * Get the param names to be used in the service call
	 * @return
	 */
	public String[] getParamNames()
	{
		return paramNames;
	}

	/**
	 * Set the param names to be used in the service call
	 * @param paramNames an array containing parameter names
	 */
	public void setParamNames(String[] paramNames)
	{
		this.paramNames = paramNames;
	}

	/**
	 * Get the parameter values to be used in the service call
	 * @return paramValues
	 */
	public String[] getParamValues()
	{
		return paramValues;
	}

	/**
	 * Set the parameter values to be used in the service call
	 * @param paramValues an array containing corresponding parameter values
	 */
	public void setParamValues(String[] paramValues)
	{
		this.paramValues = paramValues;
	}
	
	/**
	 * Get the constant param names to be used in the service call
	 * @return
	 */
	public String[] getConstantParamNames()
	{
		return constantParamNames;
	}

	/**
	 * Set the constant param names and values to be used in the service call.
	 * <b>If the parameter name already exists then its name/value pair will <i>not</i> be added.</b>
	 * @param paramNames an array containing parameter names
	 * @param paramValues an array containing parameter values
	 */
	public void setConstantParams(String[] paramNames, String[] paramValues)
	{
		this.constantParamNames = paramNames;
		this.constantParamValues = paramValues;
	}

	/**
	 * Get the constant parameter values to be used in the service call
	 * @return paramValues
	 */
	public String[] getConstantParamValues()
	{
		return constantParamValues;
	}
	
	/**
	 * Get all the param names (user defined and constant) to be used in the service call
	 * @return
	 */
	public String[] getAllParamNames()
	{
		if(constantParamNames == null)
			return paramNames;
		if(paramNames == null)
			return constantParamNames;
		String[] C= new String[paramNames.length+constantParamNames.length];
	    System.arraycopy(paramNames, 0, C, 0, paramNames.length);
	    System.arraycopy(constantParamNames, 0, C, paramNames.length, constantParamNames.length);
		return C;
	}

	/**
	 * Get all the parameter values (user defined and constant) to be used in the service call
	 * @return paramValues
	 */
	public String[] getAllParamValues()
	{
		if(constantParamValues == null)
			return paramValues;
		if(paramValues == null)
			return constantParamValues;
		String[] C= new String[paramValues.length+constantParamValues.length];
	    System.arraycopy(paramValues, 0, C, 0, paramValues.length);
	    System.arraycopy(constantParamValues, 0, C, paramValues.length, constantParamValues.length);
		return C;
	}
	
	
	
	/**
	 * Define parsing logic here
	 * @param is {@link InputStream} that contains the successful response from the service call
	 * @return An {@link ArrayList} of any type containing the parsed results.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public abstract ArrayList parseResults(InputStream is) throws XmlPullParserException, IOException;
}
