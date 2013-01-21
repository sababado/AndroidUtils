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
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

import com.sababado.utils.UtilNetwork;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * <p>This implementation of an AsyncTask is to help make service calls convenient and easy.
 * <br/>Supply an implementation of a <code>AsyncServiceListener</code> as a callback. It's appropriate functions will be called when there is either a service call failure or success.
 * </p>
 * <p><b>Logging statements are off by default and can be turned off by calling</b> <code>setLogLevel(AsyncServiceCallTask.LOGGING_OFF);</code></p>
 * @author Robert J. Szabo
 * @version 1.1
 * @since 8/30/2012
 * @updates 
 * 1.1 gets parameter name and values from all param name and values (user defined plus constant)<br/>
 * 1.0.2 added additional logs.<br/>
 * 1.0.1 added ability to turn on and off logs
 */
public class AsyncServiceCallTask extends AsyncTask<Void, String, Bundle>
{
	/**
	 * Find the result from the success bundle with this tag.
	 */
	public static final String EXTRA_SERVICE_RESULT = "EXTRA_SERVICE_RESULT";
	/**
	 * Find the error message from the failure bundle with this tag.
	 */
	private static final String EXTRA_ERR_MSG = "EXTRA_ERR_MSG";
	/**
	 * Find the error code from the failure bundle with this tag.
	 */
	private static final String EXTRA_ERR_CODE = "EXTRA_ERR_CODE";

	/**
	 * Error code corresponding to an unknown error. There is an error code missing for this situation.
	 */
	public static final int ERR_CODE_MISSING_ERR_CODE = 9;
	/**
	 * Error code corresponding to no network connection
	 */
	public static final int ERR_CODE_NO_NETWORK = 10;
	/**
	 * Error code corresponding to the maximum number of service call attempts has been reached.
	 */
	public static final int ERR_CODE_MAX_ATTEMPTS_REACHED = 11;
	/**
	 * Error code corresponding to an <code>IllegalStateException</code> when parsing the response.
	 */
	public static final int ERR_CODE_PARSE_ILLEGAL_STATE = 12;
	/**
	 * Error code corresponding to an <code>IOException</code> when parsing the response
	 */
	public static final int ERR_CODE_PARSE_IOEXCEPTION = 13;
	/**
	 * Error code corresponding to a <code>XmlPullParserException</code> when parsing the response.
	 */
	public static final int ERR_CODE_XML_PULLPARSER_EXCEPTION = 14;
	/**
	 * Error code corresponding to the fact that nothing came back from the service call. The response is <code>null</code>.
	 */
	public static final int ERR_CODE_NO_RESULTS = 15;

	//Listener associated with this async task
	private AsyncServiceListener mAsyncServiceListener;
	//number of attempts this call will try before failing
	private int MAX_ATTEMPTS = 3;
	//Service that this async task will execute
	private Service mService;	
	//Context invoking this async task
	private Context mContext;
	
	/**
	 * Constant used to not log any calls
	 */
	public static final int LOGGING_OFF = 0;
	/**
	 * Constant used to log all calls
	 */
	public static final int LOGGING_ON_ALL = 1;
	/**
	 * Constant used to log only errors
	 */
	public static final int LOGGING_ONLY_ERRORS = 2;
	
	//used to set a debug log type
	private static final int LOG_TYPE_DEBUG = 0;
	//used to set an error log type
	private static final int LOG_TYPE_ERROR = 1;
	//Used to see which calls should be logged.
	private static int mLogLevel = LOGGING_OFF;
	
	/**
	 * Set a log level so that this AsyncServicCallTask can determine which logs should be allowed.
	 * @param logLevel
	 */
	public void setLogLevel(int logLevel)
	{
		if(logLevel != LOGGING_OFF && logLevel != LOGGING_ON_ALL)
			throw new RuntimeException("Inappropriate use of log level. Use an AsyncServiceCallTask.LOGGING_* constant");
		mLogLevel = logLevel;
	}
	
	/**
	 * Initialize this async task with a callback listener, a service to execute, and the invoking context.
	 * <br>This sets the default maximum number of attempts for this task at 3.
	 * @param asyncServiceListener Callback Listener for this task
	 * @param service Service to execute
	 * @param ctx Invoking context
	 */
	public AsyncServiceCallTask(AsyncServiceListener asyncServiceListener, Service service, Context ctx)
	{
		mAsyncServiceListener = asyncServiceListener;
		mService = service;
		mContext = ctx;
	}
	
	/**
	 * Initialize this async task with a callback listener, a service to execute, and the invoking context and a maximum number of attempts that this task should try.
	 * @param asyncServiceListener Callback Listener for this task
	 * @param service Service to execute
	 * @param ctx Invoking context
	 * @param maxAutoRetryAttempts Max number of attempts
	 */
	public AsyncServiceCallTask(AsyncServiceListener asyncServiceListener, Service service, Context ctx, int maxAutoRetryAttempts)
	{
		mAsyncServiceListener = asyncServiceListener;
		mService = service;
		mContext = ctx;
		MAX_ATTEMPTS = maxAutoRetryAttempts;
	}
	
	@Override
	protected Bundle doInBackground(Void...args)
	{
		log(LOG_TYPE_DEBUG,"****in AsyncServiceCallTask do in Background");
		int attempts = 0;
		
		if(!UtilNetwork.isNetworkAvailable(mContext))
		{
			Bundle responseBundle = new Bundle();
			responseBundle.putString(EXTRA_ERR_MSG, "Sorry, there is limited or no connectivity. Please try again later.");
			responseBundle.putInt(EXTRA_ERR_CODE, ERR_CODE_NO_NETWORK);
			return responseBundle;
		}
		
		//get names and values
		String[] paramNames = mService.getAllParamNames();
		String[] paramValues= mService.getAllParamValues();
		
		//build parameter list
		String paramString = "";
		if(paramNames != null && paramNames.length > 0)
		{
			try
			{
				//this assumes there is a 1 to 1 number of names and values
				List<NameValuePair> nvPairs = new LinkedList<NameValuePair>();
				for(int i=0; i<paramNames.length; i++)
				{
					nvPairs.add(new BasicNameValuePair(paramNames[i],paramValues[i]));
				}
				paramString = URLEncodedUtils.format(nvPairs, "utf-8");
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				String message = "Failed paring param names and values (Index out of bounds). Make sure the there are the same number of param names and values.";
				log(LOG_TYPE_ERROR,message);
				throw new ArrayIndexOutOfBoundsException(message);
			}
		}
		
		String url = mService.getUrl();
		//format the url string
		if(paramString.length() > 0)
		{
			//format url to have parameters only if there are parameters to add.
			if(!url.endsWith("?"))
		        url += "?";
			url += paramString;
		}
		
		log(LOG_TYPE_DEBUG,"Url: "+url);
		
		HttpResponse response = null;
		
		//while under the maximum number of attempts...
		while(attempts < MAX_ATTEMPTS)
		{
			try
			{
				log(LOG_TYPE_DEBUG, (attempts+1)+"/"+MAX_ATTEMPTS+": Creating Http Client");
				//create the HttpClient
				HttpClient client = new DefaultHttpClient();
				//set the request
				switch(mService.getCallType())
				{
					case Service.CALL_TYPE_GET:
						log(LOG_TYPE_DEBUG, "Making GET Call");
						HttpGet getRequest = new HttpGet(url);
						response = client.execute(getRequest);
						break;
					case Service.CALL_TYPE_POST:
						log(LOG_TYPE_DEBUG, "Making POST Call");
						HttpPost postRequest = new HttpPost(url);
						response = client.execute(postRequest);
						break;
					case Service.CALL_TYPE_PUT:
						log(LOG_TYPE_DEBUG, "Making PUT Call");
						HttpPut putRequest = new HttpPut(url);
						response = client.execute(putRequest);
						break;
					case Service.CALL_TYPE_DELETE:
						log(LOG_TYPE_DEBUG, "Making DELETE Call");
						HttpDelete deleteRequest = new HttpDelete(url);
						response = client.execute(deleteRequest);
						break;
					default:
						throw new RuntimeException("Invalid Call type, please see Service.CALL_TYPE_* for possible types.");
				}
			}
			catch(IOException e)
			{
				attempts++;
				publishProgress("IOException: Retrying, attempt "+(attempts+1), e.getMessage());
			}
			
			if(response != null)
				break;
		}
		
		//check if exceeded max number of attempts
		if(attempts == MAX_ATTEMPTS)
		{
			//max number of attempts exceeded, error
			Bundle responseBundle = new Bundle();
			responseBundle.putString(EXTRA_ERR_MSG, "Failed "+MAX_ATTEMPTS+" attempts, please retry later.");
			responseBundle.putInt(EXTRA_ERR_CODE, ERR_CODE_MAX_ATTEMPTS_REACHED);
			return responseBundle;
		}
				
		return parseResponse(response);
	}
	
	private Bundle parseResponse(HttpResponse result)
	{
		Bundle responseBundle = new Bundle();
		if(result != null)
		{
			if(result.getStatusLine().getStatusCode() != 200)
			{
				//something is wrong
				int statusCd = result.getStatusLine().getStatusCode();
				responseBundle.putString(EXTRA_ERR_MSG, "Service Failed: "+statusCd+": "+result.getStatusLine().getReasonPhrase()+": "+mService.getUrl());
				responseBundle.putInt(EXTRA_ERR_CODE, statusCd);
				return responseBundle;
			}
			try
			{
				Bundle bundle = new Bundle();
				bundle.putSerializable(EXTRA_SERVICE_RESULT, mService.parseResults(result.getEntity().getContent()));
				return bundle;
			}
			catch (IllegalStateException e)
			{
				responseBundle.putString(EXTRA_ERR_MSG, "IllegalStateException: "+e.getMessage());
				responseBundle.putInt(EXTRA_ERR_CODE, ERR_CODE_PARSE_ILLEGAL_STATE);
				return responseBundle;
			}
			catch (IOException e)
			{
				responseBundle.putString(EXTRA_ERR_MSG, "IOException: "+e.getMessage());
				responseBundle.putInt(EXTRA_ERR_CODE, ERR_CODE_PARSE_IOEXCEPTION);
				return responseBundle;
			}
			catch (XmlPullParserException e)
			{
				responseBundle.putString(EXTRA_ERR_MSG, "XmlPullParserException: "+e.getMessage());
				responseBundle.putInt(EXTRA_ERR_CODE, ERR_CODE_XML_PULLPARSER_EXCEPTION);
				return responseBundle;
			}
		}
		else
		{
			responseBundle.putString(EXTRA_ERR_MSG, "No result from service call.");
			responseBundle.putInt(EXTRA_ERR_CODE, ERR_CODE_NO_RESULTS);
			return responseBundle;
		}
	}
	
	@Override
	protected void onProgressUpdate(String... values)
	{
		mAsyncServiceListener.onServiceCallProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Bundle result)
	{
		String errMsg = result.getString(EXTRA_ERR_MSG);
		if(errMsg == null)
		{
			mAsyncServiceListener.onServiceCallSuccess(result);
		}
		else
		{
			int errCd = result.getInt(EXTRA_ERR_CODE,ERR_CODE_MISSING_ERR_CODE);
			mAsyncServiceListener.onServiceCallFailure(errMsg, errCd);
		}
	}
	
	/**
	 * Log debug points if the log level allows it. Use <code>setLogLevel()</code> to change the log level.
	 * @param message
	 */
	private void log(int logLevel, String message)
	{
		if(mLogLevel == LOGGING_ON_ALL)
		{
			if(logLevel == LOG_TYPE_DEBUG)
				Log.d("AsyncServiceCallTask",message);
			else if(logLevel == LOG_TYPE_ERROR)
				Log.e("AsyncServiceCallTask",message);
		}
		if(mLogLevel == LOGGING_ONLY_ERRORS)
		{
			if(logLevel == LOG_TYPE_ERROR)
				Log.e("AsyncServiceCallTask",message);
		}
	}
}
