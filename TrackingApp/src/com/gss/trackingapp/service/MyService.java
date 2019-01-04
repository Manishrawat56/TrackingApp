package com.gss.trackingapp.service;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
	 final static String MY_ACTION = "MY_ACTION";
	    private static Context context;
	    private Timer timer;
	    public  String Data;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 @Override
	 public int onStartCommand(Intent intent, int flags, int startId) {
	        // TODO Auto-generated method stub

	        MyThread myThread = new MyThread();
	        myThread.start();

	        return super.onStartCommand(intent, flags, startId);
	    }
	 public class MyThread extends Thread{

	        @Override
	        public void run() {
	            // TODO Auto-generated method stub

	            try {
	                int delay = 1000; // delay for 1 sec.
	                int period = 12 * 1000; // repeat every 120 sec.
	                timer = new Timer();
	                timer.scheduleAtFixedRate(new TimerTask() {
	                    public void run() {
	                        Calendar c = Calendar.getInstance();
	                        Data = String.valueOf((c.get(Calendar.MILLISECOND)));
	                        Intent intent = new Intent();
	                        intent.setAction(MY_ACTION);
	                        intent.putExtra("DATAPASSED", Data);
	                        sendBroadcast(intent);
	                    }
	                }, delay, period);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	            stopSelf();
	        }

	    }
}
