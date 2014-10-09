package com.sagar.tweeter.adapter;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sagar.tweeter.R;
import com.sagar.tweeter.activity.ComposeTweetDialog;
import com.sagar.tweeter.activity.TweetDetailActivity;
import com.sagar.tweeter.activity.UserProfileActivity;
import com.sagar.tweeter.client.TwitterApplication;
import com.sagar.tweeter.client.TwitterClient;
import com.sagar.tweeter.models.Tweet;
import com.sagar.tweeter.models.User;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	ViewHolder viewHolder = null;
	public TweetArrayAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
		
	}
	
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final Tweet tweet = getItem(position);
		
		
		if(view == null)
		{
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent,false);
			
			viewHolder.ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
			viewHolder.tvUserName		= (TextView) view.findViewById(R.id.tvUserName);
			viewHolder.tvUserScreenName	= (TextView) view.findViewById(R.id.tvUserScreenName);
			viewHolder.tvBody 			= (TextView) view.findViewById(R.id.tvTweetBody);
			viewHolder.tvTime			= (TextView) view.findViewById(R.id.tvTime);
			viewHolder.tvReplyTweet		= (TextView) view.findViewById(R.id.tvReplyTweet);
			viewHolder.tvRetweet		= (TextView) view.findViewById(R.id.tvRetweet);
			viewHolder.tvLikeCount 		= (TextView) view.findViewById(R.id.tvLikeCount);
			viewHolder.ivMediaPhoto 	= (ImageView)view.findViewById(R.id.ivMediaPhoto);
			viewHolder.vvMediaVideo		= (VideoView)view.findViewById(R.id.vvMediaVideo);
			
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder)view.getTag();
			viewHolder.ivProfileImage.setImageResource(0);
			viewHolder.ivMediaPhoto.setImageResource(0);
		}
		

		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivProfileImage );
		viewHolder.tvUserName.setText( tweet.getUser().getName());
		viewHolder.tvUserScreenName.setText( tweet.getUser().getScreenName());
		viewHolder.tvBody.setText( tweet.getBody());
		viewHolder.tvTime.setText( tweet.getCreatedAt());
		viewHolder.tvRetweet.setText(" " + tweet.getRetweetCount());
		viewHolder.tvReplyTweet.setText( "");
		viewHolder.tvLikeCount.setText(" " + tweet.getLikeCount());

		if(tweet.getMedia()!= null && tweet.getMedia().getType() !=null && tweet.getMedia().getType().equals("photo")){
			if(tweet.getMedia().getUrl() !=null)
				imgLoader.displayImage(tweet.getMedia().getUrl(), viewHolder.ivMediaPhoto );  
		}
		else
		{
			//load Video
		}
		
		setupWidgetListeners(tweet);
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// Start Detail Activity
				Intent i = new Intent(getContext(),TweetDetailActivity.class);
				i.putExtra("tweet",tweet);
				getContext().startActivity(i);
			}
		});
			
		
		
		return view;
	}


	/**
	 * @param tweet
	 */
	private void setupWidgetListeners(final Tweet tweet) {
		final TwitterClient client = TwitterApplication.getRestClient();
		
		viewHolder.tvRetweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Retweeted !! ", Toast.LENGTH_SHORT).show();
				
				client.postStatusRetweet(Long.toString(tweet.getUid()),new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject json) {
						// TODO Auto-generated method stub
						Tweet newTweet = Tweet.fromJson(json);
						viewHolder.tvRetweet.setText(" "+newTweet.getRetweetCount());
						
					}
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
					}
				});
				
			}

		});
		
		viewHolder.tvLikeCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Liked ..!! ", Toast.LENGTH_SHORT).show();
				
				client.postStatusRetweet(Long.toString(tweet.getUid()),new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject json) {
						// TODO Auto-generated method stub
						Tweet newTweet = Tweet.fromJson(json);
						viewHolder.tvRetweet.setText(" "+newTweet.getRetweetCount());
						
					}
				});
				
			}

		});
		
		viewHolder.ivProfileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Liked ..!! ", Toast.LENGTH_SHORT).show();
				
				tweet.getUser().getScreenName();
				Intent i = new Intent(getContext(),UserProfileActivity.class);
				i.putExtra("screen_name", tweet.getUser().getScreenName());
				getContext().startActivity(i);
			}

		});		
		
		viewHolder.tvReplyTweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(getContext(), "reply tweet to .. "+ viewHolder.tvUserScreenName.getText(), Toast.LENGTH_SHORT).show();
				
				FragmentManager fm =  ((FragmentActivity)getContext()).getSupportFragmentManager();
				User signedInUser = getSignedInUser();
				
			      ComposeTweetDialog composeTweetDialog = ComposeTweetDialog.newInstance(signedInUser, tweet, tweet.getUid());
			      composeTweetDialog.show(fm, "dialog_compose_tweet");
				
			}

		});
		
	}
	

	/**
	 * @return
	 */
	private User getSignedInUser() {
		SharedPreferences preferences  = getContext().getSharedPreferences("TWEETER", 0);
		User signedInUser = new User();
		signedInUser.setName(preferences.getString("signed_in_user_name", "Parag Sagar"));
		signedInUser.setScreenName(preferences.getString("signed_in_user_screen_name", "sagar_parag"));
		signedInUser.setProfileImageUrl(preferences.getString("signed_in_user_profile_image_url", ""));
		return signedInUser;
	}	
	

	//View holder pattern for fast accessing (Caching) of the widgets
	public class ViewHolder{
		
		public TextView tvUserScreenName;
		public TextView tvRetweet;
		public TextView tvUserName;
		public ImageView ivProfileImage;
		public VideoView vvMediaVideo;
		public ImageView ivMediaPhoto;
		public TextView tvLikeCount;
		public TextView tvReplyTweet;
		public TextView tvTime;
		public TextView tvBody;
	}

	
}

