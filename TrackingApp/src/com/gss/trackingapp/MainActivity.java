package com.gss.trackingapp;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
TextView infoView;
StringBuilder sb=new StringBuilder();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		infoView=(TextView)findViewById(R.id.info);
		
		
		 ListView memoryList = (ListView)findViewById(R.id.list_of_memory);

	        List<Memory> memoryDataSource = new ArrayList<Memory>();

	        String heading = "RAM Information";
	        long totalRamValue = totalRamMemorySize();
	        long freeRamValue = freeRamMemorySize();
	        long usedRamValue = totalRamValue - freeRamValue;
	        int imageIcon = R.drawable.ic_launcher;
	        sb.append("----------------------------------------------------------------\n");
	        sb.append(heading+"\n");
	        sb.append("totalRamValue = "+totalRamValue+"MB\n");
	        sb.append("freeRamValue = "+freeRamValue+"MB\n");
	        sb.append("usedRamValue = "+usedRamValue+"MB\n");
	        sb.append("----------------------------------------------------------------\n");
	        Memory mMemory = new Memory(heading, formatSize(usedRamValue) + " MB", formatSize(freeRamValue) + " MB", formatSize(totalRamValue) + " MB", imageIcon);
	        memoryDataSource.add(mMemory);

	        String internalMemoryTitle = "Internal Memory Information";
	        long totalInternalValue = getTotalInternalMemorySize();
	        long freeInternalValue = getAvailableInternalMemorySize();
	        long usedInternalValue = totalInternalValue - freeInternalValue;
	        int internalIcon = R.drawable.ic_launcher;
	        sb.append("----------------------------------------------------------------\n");
	        sb.append(internalMemoryTitle+"\n");
	        sb.append("totalInternalValue = "+totalInternalValue+"MB\n");
	        sb.append("freeInternalValue = "+freeInternalValue+"MB\n");
	        sb.append("usedInternalValue = "+usedInternalValue+"MB\n");
	        sb.append("----------------------------------------------------------------\n");
	        Memory internalMemory = new Memory(internalMemoryTitle, formatSize(usedInternalValue), formatSize(freeInternalValue), formatSize(totalInternalValue), internalIcon);
	        Log.e("internalMemory", ""+internalMemory);
	        memoryDataSource.add(internalMemory);

	        String externalMemoryTitle = "External Memory Information";
	        long totalExternalValue = getTotalExternalMemorySize();
	        long freeExternalValue = getAvailableExternalMemorySize();
	        long usedExternalValue = totalExternalValue - freeExternalValue;
	        int externalIcon = R.drawable.ic_launcher;
	        sb.append("----------------------------------------------------------------\n");
	        sb.append(externalMemoryTitle+"\n");
	        sb.append("totalExternalValue = "+totalExternalValue+"MB\n");
	        sb.append("freeExternalValue = "+freeExternalValue+"MB\n");
	        sb.append("usedExternalValue = "+usedExternalValue+"MB\n");
	        sb.append("----------------------------------------------------------------\n");
	        Memory externalMemory = new Memory(externalMemoryTitle, formatSize(usedExternalValue), formatSize(freeExternalValue), formatSize(totalExternalValue), externalIcon);
	        memoryDataSource.add(externalMemory);
	        Log.e("memoryDataSource", ""+memoryDataSource);
	     /*   MemoryAdapter memoryAdapter = new MemoryAdapter(MainActivity.this, memoryDataSource);
	        memoryList.setAdapter(memoryAdapter);*/
	        //storage();
	        isNetworkAvailable();
	        getMemory();
	        sb.append("----------------------------------------------------------------\n");
	        sb.append("CPU Uses Info\n");
			sb.append("readUsage = "+readUsage()+"MB\n");
			sb.append("----------------------------------------------------------------\n");
			infoView.setText(deviceInfo().toString()+sb.toString());
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
	
	/*private void storage(){
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long bytesAvailable;
		if (android.os.Build.VERSION.SDK_INT >= 
		    android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
		    bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
		}
		else {
		    bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
		}
		long megAvailable = bytesAvailable / (1024 * 1024);
		Log.e("","Available MB : "+megAvailable);
	}*/
	 private long freeRamMemorySize() {
	        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
	        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	        activityManager.getMemoryInfo(mi);
	        long availableMegs = mi.availMem / 1048576L;

	        return availableMegs;
	    }

	    private long totalRamMemorySize() {
	        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
	        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
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
	        if (suffix != null) resultBuffer.append(suffix);
	        return resultBuffer.toString();
	    }

	    private String returnToDecimalPlaces(long values){
	        DecimalFormat df = new DecimalFormat("#.00");
	        String angleFormated = df.format(values);
	        return angleFormated;
	    }
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void doSomethingMemoryIntensive() {

	    // Before doing something that requires a lot of memory,
	    // check to see whether the device is in a low memory state.
	    ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();

	    if (!memoryInfo.lowMemory) {
	        // Do memory intensive work ...
	    }
	}

	// Get a MemoryInfo object for the device's current memory status.
	private ActivityManager.MemoryInfo getAvailableMemory() {
	    ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
	    ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
	    activityManager.getMemoryInfo(memoryInfo);
	    sb.append("memoryInfo = "+memoryInfo.toString()+"\n");
	    return memoryInfo;
	}
	void getMemory(){
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		double availableMegs = mi.availMem / 0x100000L;
		sb.append("----------------------------------------------------------------\n");
		sb.append("TotalMegs = "+(double)mi.totalMem / 0x100000L+"MB\n");
		sb.append("availableMegs = "+availableMegs+"MB\n");
		//Percentage can be calculated for API 16+
		double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;
		sb.append("percentAvail = "+percentAvail+"MB\n");
		sb.append("----------------------------------------------------------------\n");
	}

	
	    private float readUsage() {
	        try {
	            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
	            String load = reader.readLine();

	            String[] toks = load.split(" +");  // Split on one or more spaces

	            long idle1 = Long.parseLong(toks[4]);
	            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
	                  + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	            try {
	                Thread.sleep(360);
	            } catch (Exception e) {}

	            reader.seek(0);
	            load = reader.readLine();
	            reader.close();

	            toks = load.split(" +");

	            long idle2 = Long.parseLong(toks[4]);
	            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
	                + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }

	        return 0;
	    }
	    StringBuffer deviceInfo(){
	    	StringBuffer infoBuffer = new StringBuffer();

	    	infoBuffer.append("-------------------------------------\n");
	    	infoBuffer.append("Model :" + Build.MODEL + "\n");//The end-user-visible name for the end product.
	    	infoBuffer.append("Device: " + Build.DEVICE + "\n");//The name of the industrial design.
	    	infoBuffer.append("Manufacturer: " + Build.MANUFACTURER + "\n");//The manufacturer of the product/hardware.
	    	infoBuffer.append("Board: " + Build.BOARD + "\n");//The name of the underlying board, like "goldfish".
	    	infoBuffer.append("Brand: " + Build.BRAND + "\n");//The consumer-visible brand with which the product/hardware will be associated, if any.
	    	infoBuffer.append("Serial: " + Build.SERIAL + "\n");
	    	infoBuffer.append("CPU_ABI: " + Build.CPU_ABI + "\n");
	    	infoBuffer.append("CPU_ABI2: " + Build.CPU_ABI2 + "\n");
	    	infoBuffer.append("DISPLAY: " + Build.DISPLAY + "\n");
	    	infoBuffer.append("HARDWARE: " + Build.HARDWARE + "\n");
	    	infoBuffer.append("HOST: " + Build.HOST + "\n");
	    	infoBuffer.append("ID: " + Build.ID + "\n");
	    	infoBuffer.append("Build.VERSION.CODENAME: " + Build.VERSION.CODENAME + "\n");
	    	infoBuffer.append("Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT + "\n");
	    	infoBuffer.append("Serial: " + Build.SERIAL + "\n");
	    	
	    	
	    	infoBuffer.append("-------------------------------------\n");
	    	return infoBuffer;
	    }
	    
	    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	      @Override
	      public void onReceive(Context ctxt, Intent intent) {
	        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
	        infoView.setText(deviceInfo().toString()+sb.append("\nbattery ="+String.valueOf(level) + "%"));
	        int deviceHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
	        String currentBatteryHealth="Battery health =";
	        deviceHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
	        
            if(deviceHealth == BatteryManager.BATTERY_HEALTH_COLD){
 
            	sb.append(currentBatteryHealth+" = Cold");
            }
 
            if(deviceHealth == BatteryManager.BATTERY_HEALTH_DEAD){
 
            	sb.append(currentBatteryHealth+" = Dead");
            }
 
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_GOOD){
 
            	sb.append(currentBatteryHealth+" = Good");
            }
 
            if(deviceHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT){
 
            	sb.append(currentBatteryHealth+" = OverHeat");
            }
 
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
 
            	sb.append(currentBatteryHealth+" = Over voltage");
            }
 
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN){
 
            	sb.append(currentBatteryHealth+" = Unknown");
            }
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
 
            	sb.append(currentBatteryHealth+" = Unspecified Failure");
            }
            infoView.setText(deviceInfo().toString()+sb.append("\nbattery ="+String.valueOf(level) + "%"));
        }
    };
}
