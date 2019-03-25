package com.gss.trackingapp.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import com.gss.trackingapp.MainActivity;
import com.gss.trackingapp.R;
import com.gss.trackingapp.model.DynamicDeviceInfo;
import com.gss.trackingapp.model.StaticDeviceInfo;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements OnTaskDoneListener{
	final static String MY_ACTION = "MY_ACTION";
	final static String URL = "http/localhost:8080/DM/staticDetails.htm";
	boolean updateUIStatus = true;
	int interval = 1000;
	Long staticId = 0l;
	String batteryHealth;
	String batteryLevel;
	final static String STATIC_ID = "static_id";
	
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences prefs = getSharedPreferences("tracking_pref", Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		Log.e("onCreate", "onCreate");
		if (prefs.getLong(STATIC_ID, 0) == 0) {
			new Handler().postAtTime(new Runnable() {

				@Override
				public void run() {
					StaticDeviceInfo staticDeviceInfo = new StaticDeviceInfo(staticId, Build.MODEL, Build.DEVICE,
							Build.MANUFACTURER, Build.BOARD, Build.BRAND, Build.SERIAL, Build.CPU_ABI, Build.CPU_ABI2,
							Build.DISPLAY, Build.HARDWARE, Build.VERSION.CODENAME,
							String.valueOf(Build.VERSION.SDK_INT));
					Log.e("run", "run method");
					new HttpGetRequest(new OnTaskDoneListener() {

						@Override
						public void onTaskDone(String responseData) {
							/*editor.putLong(STATIC_ID, 0);
							editor.commit();*/
							Log.e("responseData", ""+responseData);
						}

						@Override
						public void onError() {
							// TODO Auto-generated method stub

						}
					}, staticDeviceInfo.getStaticJson()).execute(URL);

				}
			}, 0);
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	HashMap<String, String> postDataParams;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		postDataParams= new HashMap<String, String>();
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		/*final Handler handler=new Handler();
		Runnable runnable=new Runnable() {

			@Override
			public void run() {
				postDataParams.clear();
				Intent intent = new Intent();
				intent.setAction(MainActivity.mBroadcastStringAction);
				intent.putExtra(MainActivity.DATA_CPU, cpu());
				intent.putExtra(MainActivity.DATA_RAM, ram());
				intent.putExtra(MainActivity.DATA_INTERNAL, internal());
				intent.putExtra(MainActivity.DATA_EXTERNAL, external());
				sendBroadcast(intent);
				long totalRamValue = totalRamMemorySize();
				long freeRamValue = freeRamMemorySize();
				long usedRamValue = totalRamValue - freeRamValue;
				long totalInternalValue = getTotalInternalMemorySize();
				long freeInternalValue = getAvailableInternalMemorySize();
				long usedInternalValue = totalInternalValue - freeInternalValue;
				long totalExternalValue = getTotalExternalMemorySize();
				long freeExternalValue = getAvailableExternalMemorySize();
				long usedExternalValue = totalExternalValue - freeExternalValue;
				Log.e("CPU", "" + cpu());
				Log.e("ram", "" + ram());
				Log.e("internal", "" + internal());
				Log.e("external", "" + external());
				DynamicDeviceInfo dynamicDeviceInfo=new DynamicDeviceInfo( 0,
						  staticId,
						  String.valueOf(readUsage()*100),
						  String.valueOf(totalRamValue),
						  String.valueOf(freeRamValue),
						 String.valueOf(usedRamValue),
						 String.valueOf(totalInternalValue),
						 String.valueOf(freeInternalValue),
						 String.valueOf(usedInternalValue),
						 String.valueOf(totalExternalValue),
						 String.valueOf(freeExternalValue),
						  String.valueOf(usedExternalValue),
						  isNetworkAvailable(),
						  batteryHealth,
						  batteryLevel);
				new HttpGetRequest(MyService.this,dynamicDeviceInfo.getStaticJson()).execute(URL);
				if (updateUIStatus) {
					handler.postAtTime(this, interval);
				}

			}
		};
		handler.postAtTime(runnable, interval);*/
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		updateUIStatus = false;
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}
	
	/*public class MyThread extends Thread {

		@Override
		public void run() {
			while (updateUIStatus) {
				Intent intent = new Intent();
				intent.setAction(MainActivity.mBroadcastStringAction);
				intent.putExtra(MainActivity.DATA_CPU, cpu());
				intent.putExtra(MainActivity.DATA_RAM, ram());
				intent.putExtra(MainActivity.DATA_INTERNAL, internal());
				intent.putExtra(MainActivity.DATA_EXTERNAL, external());
				sendBroadcast(intent);
				Log.e("CPU", "" + cpu());
				Log.e("ram", "" + ram());
				Log.e("internal", "" + internal());
				Log.e("external", "" + external());
				try {
					// thread to sleep for 1000 milliseconds
					Thread.sleep(2000);
				} catch (Exception e) {
					System.out.println(e);
				}
			}

		}

	}
*/
	
	String cpu() {
		StringBuilder sb = new StringBuilder();
		// sb.append("----------------------------------------------------------------\n");
		sb.append("CPU Uses Info\n");
		sb.append("readUsage = " + readUsage()*100 + "\n");
		// sb.append("----------------------------------------------------------------\n");
		return sb.toString();
	}
	
	String ram() {
		StringBuilder sb = new StringBuilder();
		String heading = "RAM Information";
		long totalRamValue = totalRamMemorySize();
		long freeRamValue = freeRamMemorySize();
		long usedRamValue = totalRamValue - freeRamValue;
		int imageIcon = R.drawable.ic_launcher;
		// sb.append("----------------------------------------------------------------\n");
		sb.append(heading + "\n");
		sb.append("totalRamValue = " + totalRamValue + "MB\n");
		sb.append("freeRamValue = " + freeRamValue + "MB\n");
		sb.append("usedRamValue = " + usedRamValue + "MB\n");
		// sb.append("----------------------------------------------------------------\n");
		return sb.toString();
	}

	String internal() {
		StringBuilder sb = new StringBuilder();
		String internalMemoryTitle = "Internal Memory Information";
		long totalInternalValue = getTotalInternalMemorySize();
		long freeInternalValue = getAvailableInternalMemorySize();
		long usedInternalValue = totalInternalValue - freeInternalValue;
		int internalIcon = R.drawable.ic_launcher;
		// sb.append("----------------------------------------------------------------\n");
		sb.append(internalMemoryTitle + "\n");
		sb.append("totalInternalValue = " + totalInternalValue + "MB\n");
		sb.append("freeInternalValue = " + freeInternalValue + "MB\n");
		sb.append("usedInternalValue = " + usedInternalValue + "MB\n");
		// sb.append("----------------------------------------------------------------\n");
		return sb.toString();
	}

	String external() {
		StringBuilder sb = new StringBuilder();
		String externalMemoryTitle = "External Memory Information";
		long totalExternalValue = getTotalExternalMemorySize();
		long freeExternalValue = getAvailableExternalMemorySize();
		long usedExternalValue = totalExternalValue - freeExternalValue;
		int externalIcon = R.drawable.ic_launcher;
		// sb.append("----------------------------------------------------------------\n");
		sb.append(externalMemoryTitle + "\n");
		sb.append("totalExternalValue = " + totalExternalValue + "MB\n");
		sb.append("freeExternalValue = " + freeExternalValue + "MB\n");
		sb.append("usedExternalValue = " + usedExternalValue + "MB\n");
		// sb.append("----------------------------------------------------------------\n");
		return sb.toString();
	}

	private long freeRamMemorySize() {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem / 1048576L;

		return availableMegs;
	}

	private long totalRamMemorySize() {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.totalMem / 1048576L;
		return availableMegs;
	}

	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return 0;
		}
	}

	public static long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return 0;
		}
	}

	public static String formatSize(long size) {
		String suffix = null;
		if (size >= 1024) {
			suffix = " KB";
			size /= 1024L;
			if (size >= 1024) {
				suffix = " MB";
				size /= 1024L;
				if (size >= 1024) {
					suffix = " GB";
					size /= 1024L;
				}
			}

		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	private float readUsage() {
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
			String load = reader.readLine();

			String[] toks = load.split(" +"); // Split on one or more spaces

			long idle1 = Long.parseLong(toks[4]);
			long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
					+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			try {
				Thread.sleep(360);
			} catch (Exception e) {
			}

			reader.seek(0);
			load = reader.readLine();
			reader.close();

			toks = load.split(" +");

			long idle2 = Long.parseLong(toks[4]);
			long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
					+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	public boolean isNetworkAvailable() {

        ConnectivityManager connectivity = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
	}
	@Override
	public void onTaskDone(String responseData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}
}
