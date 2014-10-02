package com.sagar.tweeter.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sagar.tweeter.R;
import com.sagar.tweeter.client.TwitterApplication;
import com.sagar.tweeter.client.TwitterClient;
import com.sagar.tweeter.models.Tweet;

public class TweetDetailActivity extends Activity implements OnClickListener {

	private ImageView ivProfileImage;
	private TextView tvUserName;
	private TextView tvUserScreenName;
	private TextView tvTweetBody;
	private ImageView ivMediaPhoto;
	private VideoView vvMediaVideo;
	private EditText etTweetBody;
	private ImageView ivTweetBtn ;
	private Tweet tweet;
	private TextView tvRetweet;
	private TextView tvLIke;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_detail);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(80, 165, 230)));
		tweet = (Tweet)getIntent().getSerializableExtra("tweet");
		
		loadUIResources();
	}



	//Matching of UI widgets 
	private void loadUIResources() {
		ivProfileImage = (ImageView) findViewById(R.id.ivDetailProfilePhoto);
		tvUserName = (TextView) findViewById(R.id.tvDetailUserName);
		tvUserScreenName = (TextView) findViewById(R.id.tvDetailUserScreenName);
		tvTweetBody = (TextView) findViewById(R.id.tcDetailTweetBody);
		tvRetweet = (TextView) findViewById(R.id.tvDetailRetweet);
		tvLIke = (TextView) findViewById(R.id.tvDetailLike);
		ivMediaPhoto = (ImageView) findViewById(R.id.ivDetailMediaPhoto);
		vvMediaVideo = (VideoView) findViewById(R.id.vvDetailMediaVideo);

		etTweetBody 	= (EditText)findViewById(R.id.etReplyTweetBody);
		ivTweetBtn = (ImageView)findViewById(R.id.ivComposeReplyBtn);

		ivTweetBtn.setOnClickListener(this);
		
		tvUserName.setText(tweet.getUser().getName());
		tvUserScreenName.setText(tweet.getUser().getScreenName());
		tvTweetBody.setText(tweet.getBody());
		tvRetweet.setText(" "+tweet.getRetweetCount());
		tvLIke.setText(" "+tweet.getLikeCount());
		
		ivMediaPhoto.setImageResource(0);
		ivMediaPhoto.setImageResource(0);
		
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		
		
		if(tweet.getMedia()!= null && tweet.getMedia().getType() !=null && tweet.getMedia().getType().equals("photo")){
			if(tweet.getMedia().getUrl() !=null)
				imgLoader.displayImage(tweet.getMedia().getUrl(), ivMediaPhoto );  
		}
		else
		{
			//load Video vvMediaVideo
			vvMediaVideo.setVisibility(0);
		}
		
		
	}



	@Override
	public void onClick(View v) {

		Toast.makeText(getApplication(),"Tweeting : "+ etTweetBody.getText().toString(), Toast.LENGTH_SHORT).show();
		TwitterClient client = TwitterApplication.getRestClient();
		
		
		client.postUserStatus( etTweetBody.getText().toString(),Long.toString(tweet.getUid()), new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(json);
				Log.d("DEBUG",json.toString());
				tweet = Tweet.fromJson(json);
				Intent intent = new Intent(TweetDetailActivity.this,TimelineActivity.class);
				intent.putExtra("tweet",tweet);
				startActivity(intent);
			}
			@Override
			public void onFailure(Throwable e, String s) {
				// TODO Auto-generated method stub
				Log.d("DEBUG",e.getStackTrace().toString());
				Log.d("DEBUG",s);
				super.onFailure(e, s);
			}
		});
		
		
	
		
	}
	
}
