package com.sagar.tweeter.activity;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.sagar.tweeter.R;
import com.sagar.tweeter.activity.ComposeTweetDialog.ComposeTweetDialogListener;
import com.sagar.tweeter.client.TwitterApplication;
import com.sagar.tweeter.client.TwitterClient;
import com.sagar.tweeter.client.TwitterJsonHttpResponseHandler;
import com.sagar.tweeter.fragments.UsersTimeLineFragment;
import com.sagar.tweeter.models.Tweet;
import com.sagar.tweeter.models.User;

public class UserProfileActivity extends FragmentActivity implements  ComposeTweetDialogListener {

	TwitterClient client;
	private ImageView ivProfileImage;
	private TextView tvUserName;
	private TextView tvScreenName;
	private TextView tvTagLine;
	private TextView tvNoOFTweets;
	private TextView tvNoOfFollowers;
	private TextView tvNoOfFollowing;
	private String screenName;
	private RelativeLayout rlProfileView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(80, 165, 230)));
		client = TwitterApplication.getRestClient();
		
		screenName = (String)getIntent().getStringExtra("screen_name");
		if(screenName.indexOf("@") >0)
			screenName = screenName.substring(screenName.indexOf("@"));
		
		ivProfileImage=(ImageView)findViewById(R.id.ivProfileImage);
		tvUserName= (TextView)findViewById(R.id.tvUserName);
		tvScreenName = (TextView)findViewById(R.id.tvScreenName);
		tvTagLine = (TextView)findViewById(R.id.tvTagLine);
		rlProfileView = (RelativeLayout)findViewById(R.id.rlProfileView);

		tvNoOFTweets = (TextView)findViewById(R.id.tvNoOFTweets);
		tvNoOfFollowers = (TextView)findViewById(R.id.tvNoOfFollowers);
		tvNoOfFollowing = (TextView)findViewById(R.id.tvNoOfFollowing);

		setupUserData();
		
		loadFragment();
		
	}

	private void loadFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UsersTimeLineFragment userTimeLineFragment = new UsersTimeLineFragment();
		// hide SHow add Remove
		ft.replace(R.id.flUserTimelineContainer,userTimeLineFragment);
		ft.addToBackStack("UsersTimeLineFragment");
		userTimeLineFragment.setScreenName(screenName);
		ft.commit();
	}

	private void setupUserData(){
		client.getUserDetails(screenName, new TwitterJsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject json) {
			User user = User.fromJson(json);
			
			tvUserName.setText(user.getName());
			tvScreenName.setText(user.getScreenName());
			tvTagLine.setText(user.getTagLine());
			tvNoOFTweets.setText(user.getNosTweets());
			tvNoOfFollowers.setText(user.getNosFollowers());
			tvNoOfFollowing.setText(user.getNosFollowing());
			
			
			ImageLoader imgLoader = ImageLoader.getInstance();
			imgLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
			
			// Load image, decode it to Bitmap and return Bitmap to callback
			imgLoader.loadImage(user.getProfileBackgroundImageUrl(), new SimpleImageLoadingListener() {
			    @SuppressWarnings("deprecation")
				@Override
			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			        // Do whatever you want with Bitmap
			    	rlProfileView.setBackgroundDrawable(new BitmapDrawable(loadedImage));
			    }
			});
			
			}
			
			@Override
			public void onStart() {
				//no implementation
			}
			
			@Override
			public Context getContext() {
				// TODO Auto-generated method stub
				return getBaseContext();
			}
		});
	}
	
	@Override
	public void onFinishComposeTweetDialogListener(Tweet tweet) {
		// TODO Auto-generated method stub
		
	}
}
