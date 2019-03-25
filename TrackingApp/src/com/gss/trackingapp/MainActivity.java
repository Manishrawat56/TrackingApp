package com.gss.trackingapp;

import com.gss.trackingapp.model.StaticDeviceInfo;
import com.gss.trackingapp.service.MyService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
TextView deviceInfo,ramInfo,internalMemoryInfo,externalMemoryInfo,cpuInfo;
private IntentFilter mIntentFilter;
public static final String mBroadcastStringAction = "com.gss.info";
public static final String DATA_CPU = "cpu";
public static final String DATA_RAM = "ram";
public static final String DATA_INTERNAL = "internal";
public static final String DATA_EXTERNAL = "external";
final static String URL = "http://localhost:8080/devceManager/staticDetails.htm";
StringBuilder sb=new StringBuilder();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		cpuInfo=(TextView)findViewById(R.id.cpu);
		ramInfo=(TextView)findViewById(R.id.ram);
		internalMemoryInfo=(TextView)findViewById(R.id.internalMemory);
		externalMemoryInfo=(TextView)findViewById(R.id.externalMemory);
		deviceInfo=(TextView)findViewById(R.id.device);
		
	        mIntentFilter = new IntentFilter();
	        mIntentFilter.addAction(mBroadcastStringAction);
	       /* if(!isMyServiceRunning(MyService.class)){
	        	startService(new Intent(getBaseContext(), MyService.class));
	        }*/
	       /* new Handler().postAtTime(new Runnable() {

				@Override
				public void run() {
					StaticDeviceInfo staticDeviceInfo = new StaticDeviceInfo(0, Build.MODEL, Build.DEVICE,
							Build.MANUFACTURER, Build.BOARD, Build.BRAND, Build.SERIAL, Build.CPU_ABI, Build.CPU_ABI2,
							Build.DISPLAY, Build.HARDWARE, Build.VERSION.CODENAME,
							String.valueOf(Build.VERSION.SDK_INT));
					Log.e("run", "run method");
					new HttpGetRequest(new OnTaskDoneListener() {

						@Override
						public void onTaskDone(String responseData) {
							editor.putLong(STATIC_ID, 0);
							editor.commit();
							Log.e("responseData", ""+responseData);
						}

						@Override
						public void onError() {
							// TODO Auto-generated method stub

						}
					}, staticDeviceInfo.getStaticJson()).execute(URL);

				}
			}, 0);
*/
	}
	 @Override
	    public void onResume() {
	        super.onResume();
	        registerReceiver(mReceiver, mIntentFilter);
	    }
	 private boolean isMyServiceRunning(Class<?> serviceClass) {
		    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (serviceClass.getName().equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
		}
	
	/* private void startServiceByAlarm(Context context){
	        // Get alarm manager.
	        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

	        // Create intent to invoke the background service.
	        Intent intent = new Intent(context, MyService.class);
	        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	        long startTime = System.currentTimeMillis();
	        long intervalTime = 5*1000;

	        String message = "Start service use repeat alarm. ";

	        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

	        Log.d(BootDeviceReceiver.TAG_BOOT_BROADCAST_RECEIVER, message);

	        // Create repeat alarm.
	        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
	    }*/
	 
	 private BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	cpuInfo.setText(intent.getStringExtra(DATA_CPU));
	    		ramInfo.setText(intent.getStringExtra(DATA_RAM));
	    		internalMemoryInfo.setText(intent.getStringExtra(DATA_INTERNAL));
	    		externalMemoryInfo.setText(intent.getStringExtra(DATA_EXTERNAL));
	    		//deviceInfo.setText(deviceInfo());
	           /* mTextView.setText(mTextView.getText()
	                    + "Broadcast From Service: \n");
	            if (intent.getAction().equals(mBroadcastStringAction)) {
	                mTextView.setText(mTextView.getText()
	                        + intent.getStringExtra("Data") + "\n\n");
	            } else if (intent.getAction().equals(mBroadcastIntegerAction)) {
	                mTextView.setText(mTextView.getText().toString()
	                        + intent.getIntExtra("Data", 0) + "\n\n");
	            } else if (intent.getAction().equals(mBroadcastArrayListAction)) {
	                mTextView.setText(mTextView.getText()
	                        + intent.getStringArrayListExtra("Data").toString()
	                        + "\n\n");
	                Intent stopIntent = new Intent(MainActivity.this,
	                        BroadcastService.class);
	                stopService(stopIntent);
	            }*/
	        }
	    };
	 
	    @Override
	    protected void onPause() {
	        unregisterReceiver(mReceiver);
	        super.onPause();
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
	 public void startService(View view) {
		 
	        StaticDeviceInfo staticDeviceInfo = new StaticDeviceInfo(0, Build.MODEL, Build.DEVICE,
					Build.MANUFACTURER, Build.BOARD, Build.BRAND, Build.SERIAL, Build.CPU_ABI, Build.CPU_ABI2,
					Build.DISPLAY, Build.HARDWARE, Build.VERSION.CODENAME,
					String.valueOf(Build.VERSION.SDK_INT));

			new SendDeviceDetails().execute(URL, staticDeviceInfo.getStaticJson().toString());
	   }

	   // Method to stop the service
	   public void stopService(View view) {
	      stopService(new Intent(getBaseContext(), MyService.class));
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

	    private StringBuffer deviceInfo(){
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
	    	infoBuffer.append("Internet is connected : " + isNetworkAvailable() + "\n");
	    	
	    	infoBuffer.append("-------------------------------------\n");
	    	return infoBuffer;
	    }
	    
	    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	      @Override
	      public void onReceive(Context ctxt, Intent intent) {
	        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
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
            deviceInfo.setText(deviceInfo().toString()+sb.append("\nbattery ="+String.valueOf(level) + "%"));
        }
    };
}
