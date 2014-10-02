package com.sagar.tweeter.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name="tweets")
public class Tweet extends Model implements Serializable{

	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = -1286640479346841992L;

	@Column(name="body")
	private String body;
	@Column(name="uid",index=true)
	private long uid;
	@Column(name="createdAt")
	private String createdAt;
	@Column(name="user")
	private User user;
	@Column(name="retweet_count")
	private int retweetCount;
	@Column(name="like_count")
	private int likeCount;
	
	private Media media;

	
	public static Tweet fromJson(JSONObject json) {
		Tweet tweet = new Tweet();
		try {
			tweet.body = json.getString("text");
			tweet.uid = json.getLong("id");
			tweet.createdAt = json.getString("created_at");
			tweet.user = User.fromJson(json.getJSONObject("user"));
			tweet.retweetCount = json.getInt("retweet_count");
			tweet.likeCount = json.getInt("favorite_count");
			//!photoJSON.isNull("location")
			if(!json.isNull("entities") && !json.getJSONObject("entities").isNull("media"))
				tweet.setMedia(Media.fromJSON(json.getJSONObject("entities").getJSONArray("media")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweet;
	}

	public static ArrayList<Tweet> fromJsonArray(JSONArray json) {

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		ActiveAndroid.beginTransaction();
		try {
			for (int i = 0; i < json.length(); i++) {
				try {
					Tweet tweet = Tweet.fromJson(json.getJSONObject(i));
					tweets.add(tweet);
					tweet.save();

				} catch (JSONException e) {
					// Error parsing json
					
					e.printStackTrace();
				}

			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
		return tweets;
	}

	/**
	 * This method load tweets for off line mode.
	 * @return
	 */
	public static ArrayList<Tweet> getAllTweets(){
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		return tweets;
	}
	
	public static Tweet getTweetsById(String uid){
		return new Select().from(Tweet.class).where("uid = ?", uid).executeSingle();
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the uid
	 */
	public long getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(long uid) {
		this.uid = uid;
	}

	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		
		Date date = new Date();
		//Mon Sep 29 23:51:54 +0000 2014
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.US);
		
		try {
			date = sdf.parse(createdAt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return " "+DateUtils.getRelativeTimeSpanString(date.getTime(),System.currentTimeMillis(),DateUtils.FORMAT_ABBREV_TIME).toString();
		
		
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the retweetCount
	 */
	public int getRetweetCount() {
		return retweetCount;
	}

	/**
	 * @param retweetCount
	 *            the retweetCount to set
	 */
	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	/**
	 * @return the likeCount
	 */
	public int getLikeCount() {
		return likeCount;
	}

	/**
	 * @param likeCount
	 *            the likeCount to set
	 */
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	/**
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media the media to set
	 */
	public void setMedia(Media media) {
		this.media = media;
	}
	
	@Override
	public String toString() {
		
		String s = body;
		return s;
	}
}
