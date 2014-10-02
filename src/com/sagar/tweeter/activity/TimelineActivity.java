package com.sagar.tweeter.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sagar.tweeter.R;
import com.sagar.tweeter.activity.ComposeTweetDialog.ComposeTweetDialogListener;
import com.sagar.tweeter.adapter.TweetArrayAdapter;
import com.sagar.tweeter.client.TwitterApplication;
import com.sagar.tweeter.client.TwitterClient;
import com.sagar.tweeter.listener.EndlessScrollListener;
import com.sagar.tweeter.models.Tweet;
import com.sagar.tweeter.models.User;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends FragmentActivity implements ComposeTweetDialogListener {

	TwitterClient client ;
	PullToRefreshListView lvTweet;
	ArrayList<Tweet> tweets;
	ArrayAdapter<Tweet> aTweets;
	private User signedInUser;
	private long maxTweetId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(80, 165, 230)));
		

		tweets = new ArrayList<Tweet>();
		lvTweet = (PullToRefreshListView) findViewById(R.id.lvTweets);
		aTweets = new TweetArrayAdapter(this, R.layout.tweet_item,tweets);
		lvTweet.setAdapter(aTweets);
		
		client = TwitterApplication.getRestClient();
		populateTimeline();
		
		setupListeners();
		getUserDetails();

		// Tweet tweet = (Tweet)getIntent().getSerializableExtra("tweet");
		// if(tweet!= null)
		// tweets.add(0,tweet );
	}
	
	/** 
	 * method to get logged in User Details 
	 */
	private void getUserDetails() {
		
		client.getUserDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				signedInUser = User.fromJson(json);
				saveUserIntoPreferences();
			}

		});
		// 
		
	}
	
	/**
	 * 
	 */
	private void saveUserIntoPreferences() {
		SharedPreferences preferences  = getSharedPreferences("TWEETER", 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("signed_in_user_name",signedInUser.getName() );
		editor.putString("signed_in_user_screen_name",signedInUser.getScreenName() );
		editor.putString("signed_in_user_profile_image_url",signedInUser.getProfileImageUrl() );
		editor.commit();
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.compose_menu, menu);
		return super.onCreateOptionsMenu(menu);
	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (R.id.miCompose == item.getItemId()) {
			showComposeTweetDialog();
		}
		return false;
	}
	
	  private void showComposeTweetDialog() {
	      FragmentManager fm = getSupportFragmentManager();
	      ComposeTweetDialog composeTweetDialog = ComposeTweetDialog.newInstance(signedInUser, new Tweet(),0l);
	      composeTweetDialog.show(fm, "dialog_compose_tweet");
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
				populateTimeline();
				
			}
		});
	}
	
	public void loadMoreTweet(int page){
		populateTimeline();
	}

	/**
	 * Method to fire a HTTP request and update twitter feeds.
	 */
	public void populateTimeline()
	{
		if(isNetworkAvailable())
		{
			client.getHomeTimeline(maxTweetId, new JsonHttpResponseHandler(){
				
				@Override
				public void onSuccess(JSONArray json) {
					
//					Log.d("DEBUG", json.toString());
					tweets.addAll(Tweet.fromJsonArray(json));
					aTweets.notifyDataSetChanged();
					maxTweetId  = tweets.get(tweets.size()-1).getUid();
					lvTweet.onRefreshComplete();//Complete the refresh
				}
				
				
				@Override
				public void onFailure(Throwable e, String s) {
					// TODO Auto-generated method stub
					Log.d("DEBUG", e.toString());
					Log.d("DEBUG", s);
				}
				
			});
		}
		else
		{
			Toast.makeText(getBaseContext(), "No Internet available! Loading offline data.. ", Toast.LENGTH_SHORT).show();
			loadOfflineTweets();
		}
	}

	private void loadOfflineTweets() {
		// TODO Auto-generated method stub
		tweets.addAll(Tweet.getAllTweets());
		aTweets.notifyDataSetChanged();
		maxTweetId  = 0;
		lvTweet.onRefreshComplete();//Complete the refresh
	}

	@Override
	public void onFinishComposeTweetDialogListener(Tweet tweet) {
		//Finish
		tweets.add(0, tweet);
		aTweets.notifyDataSetChanged();
		
	}
	
	public Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
}
