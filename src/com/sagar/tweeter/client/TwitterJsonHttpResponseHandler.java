package com.sagar.tweeter.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class TwitterJsonHttpResponseHandler extends JsonHttpResponseHandler {
	
	public void onRefreshComplete(){}// Call Back for Complete the refresh
	
	
	public ProgressDialog dialog;
	
	public abstract Context getContext();//{ return null;};

	@Override
	public void onStart() {
		super.onStart();
		dialog = new ProgressDialog(getContext());
		dialog.setMessage("Loading, please wait");
		dialog.setTitle("Connecting server");
		dialog.show();
		dialog.setCancelable(false);
	}
	
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		super.onFinish();
		if(dialog !=null)
			dialog.cancel();
	}
	
	@Override
	public void onFailure(Throwable e, String s) {
		// TODO Auto-generated method stub
		Log.d("DEBUG", e.toString());
		Log.d("DEBUG", s);
		onRefreshComplete();// Call Back for Complete the refresh
	}
	
	@Override
	protected void handleFailureMessage(Throwable e, String s) {
		// TODO Auto-generated method stub
		super.handleFailureMessage(e, s);
		onRefreshComplete();// Call Back for Complete the refresh
		Log.d("DEBUG", e.toString());
		Log.d("DEBUG", s);
	}

}
