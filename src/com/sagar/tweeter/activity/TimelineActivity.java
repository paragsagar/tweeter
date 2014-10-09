package com.sagar.tweeter.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sagar.tweeter.R;
import com.sagar.tweeter.activity.ComposeTweetDialog.ComposeTweetDialogListener;
import com.sagar.tweeter.client.TwitterApplication;
import com.sagar.tweeter.client.TwitterClient;
import com.sagar.tweeter.fragments.HomeLineFragment;
import com.sagar.tweeter.fragments.MentionsFragment;
import com.sagar.tweeter.fragments.TweetListFragment;
import com.sagar.tweeter.listener.FragmentTabListener;
import com.sagar.tweeter.models.Tweet;
import com.sagar.tweeter.models.User;

import eu.erikw.PullToRefreshListView;

public class TimelineActivity extends FragmentActivity implements ComposeTweetDialogListener {

	TwitterClient client ;
	private User signedInUser;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(80, 165, 230)));
		getUserDetails();
		setupTabs();

	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
//			.setIcon(R.drawable.reply_hover)
			.setTag("HomeTimelineFragment")
			.setTabListener(new FragmentTabListener<HomeLineFragment>(R.id.flContainer, this, "HomeTimelineFragment",HomeLineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
//			.setIcon(R.drawable.retweet_on)
			.setTag("MentionsTimelineFragment")
			.setTabListener(new FragmentTabListener<MentionsFragment>(R.id.flContainer, this, "MentionsFragment",MentionsFragment.class));

		actionBar.addTab(tab2);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.compose_menu, menu);
		return super.onCreateOptionsMenu(menu);
	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//MenuItem Compose
		if (R.id.miSearch == item.getItemId()) {
//			showComposeTweetDialog();
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			String searchQuery="";
		}
		//MenuItem Compose
		else if (R.id.miCompose == item.getItemId()) {
			showComposeTweetDialog();
		}
		//MenuItem Compose
		else if (R.id.miProfile== item.getItemId()) {
			Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this,UserProfileActivity.class);
			i.putExtra("screen_name", "sagar_parag");
			startActivity(i);
			
		}
		return false;
	}
	
	  private void showComposeTweetDialog() {
	      FragmentManager fm = getSupportFragmentManager();
	      ComposeTweetDialog composeTweetDialog = ComposeTweetDialog.newInstance(signedInUser, new Tweet(),0l);
	      composeTweetDialog.show(fm, "dialog_compose_tweet");
	  }

		@Override
		public void onFinishComposeTweetDialogListener(Tweet tweet) {
			//Finish
			
			((TweetListFragment) getSupportFragmentManager().findFragmentByTag((String) getActionBar().getSelectedTab().getTag())).addOneTweet(tweet);;
			
		}
		
		/**
		 * method to get logged in User Details
		 */
		private void getUserDetails() {

			client = TwitterApplication.getRestClient();
			client.getSignedInUserDetails(new JsonHttpResponseHandler() {
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
		 * Saves Signed in User into Android Preferences
		 */
		private void saveUserIntoPreferences() {
			SharedPreferences preferences = getSharedPreferences("TWEETER", 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("signed_in_user_name", signedInUser.getName());
			editor.putString("signed_in_user_screen_name",signedInUser.getScreenName());
			editor.putString("signed_in_user_profile_image_url",signedInUser.getProfileImageUrl());
			editor.commit();
		}
		

	
}
