package com.gss.trackingapp.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class StaticDeviceInfo {
	private long staticId;
	private String phoneModel;
	private String device;
	private String manufacturer;
	private String board;
	private String brand;
	private String serial;
	private String cpu_ABI1;
	private String cpu_ABI2;
	private String displayName;
	private String hardware;
	private String versionCodeName;
	private String versionSdkInt;
	private Long extNum1;
	private Long extNum2;
	private String extStr1;
	private String extStr2;
	private boolean extBol1;
	private boolean extBol2;
	private Date extDate1;
	private Date extDate2;
	private int version;
	private String strCreatedAuthor;
	private Date createdDate;
	private String strUpdatedAuthor;
	private Date updatedDate;
	public StaticDeviceInfo(long staticId,
			 String phoneModel,
			 String device,
			 String manufacturer,
			 String board,
			 String brand,
			 String serial,
			 String cpu_ABI1,
			 String cpu_ABI2,
			 String displayName,
			 String hardware,
			 String versionCodeName,
			 String versionSdkInt){
		this. staticId= staticId;
		 this.phoneModel= phoneModel;
		 this. device=device;
		 this. manufacturer =manufacturer;
		 this. board =board;
		 this. brand =brand;
		 this. serial =serial;
		 this. cpu_ABI1 =cpu_ABI1;
		 this. cpu_ABI2 =cpu_ABI2;
		 this. displayName =displayName;
		 this. hardware =hardware;
		 this. versionCodeName =versionCodeName;
		 this. versionSdkInt =versionSdkInt;
		
	}
	public JSONObject getStaticJson( ){
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("staticId", staticId);
			jsonObject.put("device", device);
			jsonObject.put("manufacturer", manufacturer);
			jsonObject.put("board", board);
			jsonObject.put("brand", brand);
			jsonObject.put("serial", serial);
			jsonObject.put("cpu_ABI1", cpu_ABI1);
			jsonObject.put("cpu_ABI2", cpu_ABI2);
			jsonObject.put("displayName", displayName);
			jsonObject.put("hardware", hardware);
			jsonObject.put("versionCodeName", versionCodeName);
			jsonObject.put("versionSdkInt", versionSdkInt);
			jsonObject.put("device", device);
			jsonObject.put("extNum1", 0);
			jsonObject.put("extNum2", 0);
			jsonObject.put("extStr1", null);
			jsonObject.put("extStr2", null);
			jsonObject.put("extBol1", null);
			jsonObject.put("extBol2", null);
			jsonObject.put("extDate1", null);
			jsonObject.put("extDate2", null);
			jsonObject.put("device", null);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}
}
