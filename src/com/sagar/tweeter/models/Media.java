package com.sagar.tweeter.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Media implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5942526277719902858L;
	private String type;
	private long uid ;
	private String url;
//	private String previewUrl;
	
	
	public static Media fromJSON(JSONArray jArray) throws JSONException 
	{
		JSONObject json = (JSONObject) jArray.get(0);
		Media media = new Media();
		
		try{
			media.setType(json.getString("type"));
			media.setUid(json.getLong("id"));
			media.setUrl(json.getString("media_url"));
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return media;
	}
	
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the uid
	 */
	public long getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(long uid) {
		this.uid = uid;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
