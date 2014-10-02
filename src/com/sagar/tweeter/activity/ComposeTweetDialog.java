/**
 * 
 */
package com.sagar.tweeter.activity;

import org.json.JSONObject;

import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sagar.tweeter.R;
import com.sagar.tweeter.client.TwitterApplication;
import com.sagar.tweeter.client.TwitterClient;
import com.sagar.tweeter.models.Tweet;
import com.sagar.tweeter.models.User;

/**
 * @author sagpa03
 *
 */
public class ComposeTweetDialog extends DialogFragment implements OnClickListener,OnKeyListener {
	
	Tweet tweet;
	User user ;
	private ImageView ivProfileImage;
	private TextView tvUserName;
	private TextView tvUserScreenName;
	private EditText etTweetBody;
	private ImageView ivTweetBtn ;
	private TextView tvCharRemaining;
	private int charRemaining;
	
	public ComposeTweetDialog ()
	{
	}
	
	public static ComposeTweetDialog newInstance(User user,Tweet tweet,long replyToTweet) {
		ComposeTweetDialog dialog = new ComposeTweetDialog();
		Bundle args = new Bundle();
		args.putString("title", "Compose Tweet");
		args.putSerializable("user", user);
		args.putSerializable("tweet", tweet);
		dialog.setArguments(args);
		return dialog;
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_compose_tweet, container, false);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		String title = getArguments().getString("title", "Compose New Tweet");
		tweet = (Tweet)getArguments().getSerializable("tweet");
		if(tweet!=null && tweet.getUser() !=null)
			title = "Reply to Tweet";
		getDialog().setTitle(title);
		user = (User)getArguments().getSerializable("user");
		
		setupViewWidgets(view);
		
		
				
		return view;
	}

	private void setupViewWidgets(View view) {
		
		ivProfileImage = (ImageView) view.findViewById(R.id.ivComposeUserPorfileImage);
		tvUserName = (TextView)view.findViewById(R.id.tvComposeUserName);
		tvUserScreenName = (TextView)view.findViewById(R.id.tvComposeScreenName);
		etTweetBody 	= (EditText)view.findViewById(R.id.etNewTweetBody);
		tvCharRemaining = (TextView)view.findViewById(R.id.tvCharRemaining);
		ivTweetBtn = (ImageView)view.findViewById(R.id.ivTweetButton);
		
		
		ivTweetBtn.setOnClickListener(this);
		etTweetBody.setOnKeyListener(this);
		
		ivProfileImage.setImageResource(0);
		ImageLoader imgLoader = ImageLoader.getInstance();
		imgLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
		
		tvUserName.setText(user.getName());
		tvUserScreenName.setText(user.getScreenName());
		if(tweet!=null && tweet.getUser()!=null && tweet.getUser().getScreenName()!=null)// in case of a reply tweet
			etTweetBody.setText(tweet.getUser().getScreenName());
	}
	


	@Override
	public void onClick(View v) {
		Toast.makeText(getActivity(),"Tweeting "+ etTweetBody.getText().toString(), Toast.LENGTH_SHORT).show();
		TwitterClient client = TwitterApplication.getRestClient();
		String replyToId = null;
		if(tweet!=null && tweet.getUid() >0)
		{
			replyToId = Long.toString(tweet.getUid());
		}
		
		client.postUserStatus( etTweetBody.getText().toString(), replyToId, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(json);
				Log.d("DEBUG",json.toString());
				tweet = Tweet.fromJson(json);
				ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
				listener.onFinishComposeTweetDialogListener(tweet);
				getDialog().dismiss();
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
	
	public interface ComposeTweetDialogListener{
		
		public void onFinishComposeTweetDialogListener(Tweet tweet);
	}


	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		int total = 140;
		EditText etEditText = (EditText)view;
		setCharRemaining(total-etEditText.getText().length());
		tvCharRemaining.setText(getCharRemaining());
		return false;
	}

	public String getCharRemaining() {
		return charRemaining+" chars left";
	}

	public void setCharRemaining(int charRemaining) {
		this.charRemaining = charRemaining;
	}
}
