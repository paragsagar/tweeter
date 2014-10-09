package com.sagar.tweeter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7753006302172162426L;
	@Column(name="name")
	private String name;
	@Column(name="uid",index=true)
	private long uid;
	@Column(name="screen_name")
	private String screenName;
	@Column(name="profile_image_url")
	private String profileImageUrl;

	private String tagLine;//description
	private String nosTweets;//statuses_count
	private String nosFollowers;//followers_count
	private String nosFollowing; //favourites_count
	private String profileBackgroundImageUrl;
	
	/**
	 * It construct a User object out of a JSON object.
	 * 
	 * @param json
	 * @return
	 */
	public static User fromJson(JSONObject json) {
		User user = new User();
		try {
			user.setName(json.getString("name"));
			user.setProfileImageUrl(json.getString("profile_image_url"));
			user.setScreenName(json.getString("screen_name"));
			user.setUid(json.getLong("id"));
			user.setTagLine(json.getString("description"));
			user.setNosTweets(json.getString("statuses_count"));
			user.setNosFollowers(json.getString("followers_count"));
			user.setNosFollowing(json.getString("favourites_count"));
			user.setProfileBackgroundImageUrl(json.getString("profile_background_image_url"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		if(screenName.indexOf('@') < 0)
			this.screenName = "@"+screenName;
		else
			this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getNosTweets() {
		return nosTweets;
	}

	public void setNosTweets(String nosTweets) {
		this.nosTweets = nosTweets;
	}

	public String getNosFollowers() {
		return nosFollowers;
	}

	public void setNosFollowers(String nosFollowers) {
		this.nosFollowers = nosFollowers;
	}

	public String getNosFollowing() {
		return nosFollowing;
	}

	public void setNosFollowing(String nosFollowing) {
		this.nosFollowing = nosFollowing;
	}

	public String getTagLine() {
		return tagLine;
	}

	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}

	public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
		this.profileBackgroundImageUrl = profileBackgroundImageUrl;
	}

}
