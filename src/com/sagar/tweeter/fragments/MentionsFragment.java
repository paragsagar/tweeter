package com.sagar.tweeter.fragments;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sagar.tweeter.client.TwitterJsonHttpResponseHandler;
import com.sagar.tweeter.models.Tweet;

public class MentionsFragment extends TweetListFragment {

	/**
	 * Method to fire a HTTP request and update twitter feeds.
	 */
	public void populateTimeline()
	{
			client.getMentionsTimeline(maxTweetId, new TwitterJsonHttpResponseHandler(){
				
				@Override
				public void onSuccess(JSONArray json) {
					tweets.addAll(Tweet.fromJsonArray(json));
					aTweets.notifyDataSetChanged();
					maxTweetId  = tweets.get(tweets.size()-1).getUid();
					lvTweet.onRefreshComplete();//Complete the refresh
				}

				@Override
				public void onRefreshComplete() {
					// TODO Auto-generated method stub
					lvTweet.onRefreshComplete();//Complete the refresh
				}
				
				@Override
				public Context getContext() {
					// TODO Auto-generated method stub
					return getActivity();
				}
				
			});

	}
	
	public void loadOfflineTweets() {
		// TODO Auto-generated method stub
		tweets.addAll(Tweet.getAllTweets());
		aTweets.notifyDataSetChanged();
		maxTweetId  = 0;
		lvTweet.onRefreshComplete();//Complete the refresh
	}


}
