package com.sagar.tweeter.fragments;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sagar.tweeter.client.TwitterJsonHttpResponseHandler;
import com.sagar.tweeter.models.Tweet;

public class HomeLineFragment extends TweetListFragment {

	/**
	 * Method to fire a HTTP request and update twitter feeds.
	 */
	public void populateTimeline()
	{

			client.getHomeTimeline(maxTweetId, new TwitterJsonHttpResponseHandler(){
				
				@Override
				public void onSuccess(JSONArray json) {
					tweets.addAll(Tweet.fromJsonArray(json));
					aTweets.notifyDataSetChanged();
					maxTweetId  = tweets.get(tweets.size()-1).getUid();
					lvTweet.onRefreshComplete();//Complete the refresh
				}
				
				public void onRefreshComplete(){
					lvTweet.onRefreshComplete();//Complete the refresh
				}

				@Override
				public Context getContext() {
					// TODO Auto-generated method stub
					return getActivity();
				}
			});

	}
	
	public  void loadOfflineTweets() {
		// TODO Auto-generated method stub
		tweets.addAll(Tweet.getAllTweets());
		aTweets.notifyDataSetChanged();
		maxTweetId  = 0;
		lvTweet.onRefreshComplete();//Complete the refresh
	}


}
