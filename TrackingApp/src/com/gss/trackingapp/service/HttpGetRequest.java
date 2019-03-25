package com.gss.trackingapp.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class HttpGetRequest extends AsyncTask<String, Void, String> {
	JSONObject jsonObject;
    private OnTaskDoneListener onTaskDoneListener;
	
	   public static final String REQUEST_METHOD = "POST";
	   public static final int READ_TIMEOUT = 15000;
	   public static final int CONNECTION_TIMEOUT = 15000;
	   public HttpGetRequest(OnTaskDoneListener onTaskDoneListener,JSONObject jsonObject) {
		    this.jsonObject=jsonObject;
	        this.onTaskDoneListener = onTaskDoneListener;
	    }
	  
		   String server_response;

		    @Override
		    protected String doInBackground(String... strings) {

		        URL url;
		        HttpURLConnection httpConnection = null;

		        try {
		            url = new URL(strings[0]);
		            Log.e("strings[0]", ""+strings[0]);
		           /* httpConnection = (HttpURLConnection) url.openConnection();
		            httpConnection.setRequestMethod("POST");

		            httpConnection.setDoOutput(true);

		            DataOutputStream os = new DataOutputStream(httpConnection.getOutputStream());
	                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
	                os.writeBytes(jsonObject.toString());
		            OutputStream os = httpConnection.getOutputStream();
		            BufferedWriter writer = new BufferedWriter(
		                    new OutputStreamWriter(os, "UTF-8"));
		            writer.write(getPostDataString(postDataParams));

	                os.flush();
	                os.close();*/
	                
		           /* int responseCode = httpConnection.getResponseCode();

		            if(responseCode == HttpURLConnection.HTTP_OK){
		                server_response = readStream(httpConnection.getInputStream());
		                Log.e("CatalogClient", server_response);
		            }*/
		           
		            OutputStream out = null;

		           
		                
		                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		                out = new BufferedOutputStream(urlConnection.getOutputStream());
		                Log.e("jsonObject", ""+jsonObject.toString());
		                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
		                writer.write(jsonObject.toString());
		                writer.flush();
		                writer.close();
		                out.close();

		                urlConnection.connect();
		            Log.e("STATUS", String.valueOf(httpConnection.getResponseCode()));
	                Log.e("MSG" , httpConnection.getResponseMessage());
		        } catch (MalformedURLException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }

		        return null;
		    }

		    @Override
		    protected void onPostExecute(String s) {
		        super.onPostExecute(s);
		        Log.e("Response sss", "" + s);
		        Log.e("Response", "" + server_response);
		        if (onTaskDoneListener != null && s != null) {
		            onTaskDoneListener.onTaskDone(s);
		        } else
		            onTaskDoneListener.onError();

		    }
	   
		private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		        StringBuilder result = new StringBuilder();
		        boolean first = true;
		        for (Map.Entry<String, String> entry : params.entrySet()) {
		            if (first)
		                first = false;
		            else
		                result.append("&");

		            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
		            result.append("=");
		            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		        }

		        return result.toString();
		    }

	// Converting InputStream to String

	   private String readStream(InputStream in) {
	           BufferedReader reader = null;
	           StringBuffer response = new StringBuffer();
	           try {
	               reader = new BufferedReader(new InputStreamReader(in));
	               String line = "";
	               while ((line = reader.readLine()) != null) {
	                   response.append(line);
	               }
	           } catch (IOException e) {
	               e.printStackTrace();
	           } finally {
	               if (reader != null) {
	                   try {
	                       reader.close();
	                   } catch (IOException e) {
	                       e.printStackTrace();
	                   }
	               }
	           }
	           return response.toString();
	   }
}
