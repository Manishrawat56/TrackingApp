package com.gss.trackingapp.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class DynamicDeviceInfo {
	private long dynamicId;
	private long staticId;
	private String cpuUsed;
	private String ramTotal;
	private String ramFree;
	private String ramUse;
	private String internalTotal;
	private String internalFree;
	private String internalUse;
	private String externalTotal;
	private String externalFree;
	private String externalUse;
	private boolean isConnected;
	private String batteryHealth;
	private String batteryLevel;
	private int version;
	private String strCreatedAuthor;
	private Date createdDate;
	private String strUpdatedAuthor;
	private Date updatedDate;
	public DynamicDeviceInfo( long dynamicId,
	 long staticId,
	 String cpuUsed,
	 String ramTotal,
	 String ramFree,
	 String ramUse,
	 String internalTotal,
	 String internalFree,
	 String internalUse,
	 String externalTotal,
	 String externalFree,
	 String externalUse,
	 boolean isConnected,
	 String batteryHealth,
	 String batteryLevel){
		this. dynamicId =dynamicId;
		this. staticId =staticId;
		this. cpuUsed =cpuUsed;
		this. ramTotal =ramTotal;
		this. ramFree =ramFree;
		this. ramUse =ramUse;
		this. internalTotal =internalTotal;
		this. internalFree =internalFree;
		this. internalUse =internalUse;
		this. externalTotal =externalTotal;
		this. externalFree =externalFree;
		this. externalUse =externalUse;
		this. isConnected =isConnected;
		this. batteryHealth =batteryHealth;
		this. batteryLevel =batteryLevel;
	}
	public JSONObject getStaticJson( ){
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("dynamicId", dynamicId);
			jsonObject.put("staticId", staticId);
			jsonObject.put("cpuUsed", cpuUsed);
			jsonObject.put("ramTotal", ramTotal);
			jsonObject.put("ramFree", ramFree);
			jsonObject.put("ramUse", ramUse);
			jsonObject.put("internalTotal", internalTotal);
			jsonObject.put("internalFree", internalFree);
			jsonObject.put("internalUse", internalUse);
			jsonObject.put("externalTotal", externalTotal);
			jsonObject.put("externalFree", externalFree);
			jsonObject.put("externalUse", externalUse);
			jsonObject.put("isConnected", isConnected);
			jsonObject.put("batteryHealth", batteryHealth);
			jsonObject.put("batteryLevel", batteryLevel);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}
}
