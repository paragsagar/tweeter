package com.sagar.tweeter.client;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "Qp2exlkhBpcBJ0uK7NNeEwWDV";       // Change this
	public static final String REST_CONSUMER_SECRET = "Wt9WFGxnDbQeuUae0fNIrYqepxGL7K2sTszpTIIJs8ezSNVRTe"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
	
	public void getHomeTimeline(long maxTweetId, JsonHttpResponseHandler resp){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("since_id","1");
		if(maxTweetId >0)
			params.put("max_id", Long.toString(maxTweetId));
		client.get(apiUrl,params,resp);
	}
	
	public void postUserStatus(String tweet, String inReplyToId, JsonHttpResponseHandler resp){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",tweet);
		if(inReplyToId !=null && inReplyToId.length()>1)
			params.put("in_reply_to_status_id",inReplyToId);
		client.post(apiUrl,params,resp);
	}

	public void getTweetById(JsonHttpResponseHandler resp,String tweetId){
		String apiUrl = getApiUrl("statuses/show.json");
		RequestParams params = new RequestParams();
		params.put("id",tweetId);
		client.get(apiUrl,params,resp);
	}

	public void getUserDetails(JsonHttpResponseHandler resp){
		String apiUrl = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		client.get(apiUrl,null,resp);
	}

}