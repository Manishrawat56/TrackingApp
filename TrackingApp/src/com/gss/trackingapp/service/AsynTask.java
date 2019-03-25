package com.gss.trackingapp.service;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.transform.Result;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsynTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {
        HttpHandler httpHandler=new HttpHandler();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("phone", "8888888888");
        param.put("token", "1234567890");

        String result = httpHandler.sendPostRequest("\n" +
                "GIVEN_API", param);
        Log.i("result", result);
        return result;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("stauts", s);
        String status = null;
        try {
            JSONObject mainObject = new JSONObject(s);
            status = mainObject.getString("status");

            Log.i("output", status);
           /* if(status.equals("true")){
            Toast.makeText(mContext," successfully",Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(mContext,"Please try again",Toast.LENGTH_SHORT).show();
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }


     

    }

}
