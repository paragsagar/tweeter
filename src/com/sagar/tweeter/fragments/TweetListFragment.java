package com.sagar.tweeter.fragments;

import java.util.ArrayList;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sagar.tweeter.R;
import com.sagar.tweeter.adapter.TweetArrayAdapter;
import com.sagar.tweeter.client.TwitterApplication;
import com.sagar.tweeter.client.TwitterClient;
import com.sagar.tweeter.listener.EndlessScrollListener;
import com.sagar.tweeter.models.Tweet;
import com.sagar.tweeter.models.User;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public abstract class TweetListFragment extends Fragment {

	TwitterClient client;
	PullToRefreshListView lvTweet;
	ArrayList<Tweet> tweets;
	ArrayAdapter<Tweet> aTweets;
	private User signedInUser;
	protected long maxTweetId = 0;

	public abstract void populateTimeline();

	public abstract void loadOfflineTweets();

	public void performAction() {
		if (isNetworkAvailable()) {
			populateTimeline();
		} else {
			Toast.makeText(getActivity(),
					"No Internet available! Loading offline data.. ",
					Toast.LENGTH_SHORT).show();
			loadOfflineTweets();
		}

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), R.layout.tweet_item,
				tweets);
		client = TwitterApplication.getRestClient();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_tweet_list, container,
				false);
		lvTweet = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
		lvTweet.setAdapter(aTweets);

		performAction();

		setupListeners();
		

		return view;

	}

	public void addAll(ArrayList<Tweet> tweetsList) {
		tweets.addAll(tweetsList);
	}



	/**
	 * Method to setup ListListeners
	 */
	private void setupListeners() {

		lvTweet.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreTweet(page);

			}
		});

		lvTweet.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				performAction();

			}
		});
	}

	// Infinite Scrolling for Tweets
	public void loadMoreTweet(int page) {
		performAction();
	}

	/**
	 * Method Checks for Network Availability
	 * @return false if network not available.
	 */
	public Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null&& activeNetworkInfo.isConnectedOrConnecting();
	}

	public void addOneTweet(Tweet tweet) {
		tweets.add(0, tweet);
		aTweets.notifyDataSetChanged();
	}

}
