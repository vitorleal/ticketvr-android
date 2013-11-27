package com.vleal.ticketvr.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.vleal.ticketvr.R;
import com.vleal.ticketvr.ResultActivity;
import com.vleal.ticketvr.ui.CardFormat;
import com.vleal.ticketvr.ui.TicketUI;

public class CheckCard {
	private static Context context;
	
	public CheckCard(Context context) {
		CheckCard.setContext(context);
	}
	
	//Get set context
	public static Context getContext() {
		return context;
	}
	public static void setContext(Context context) {
		CheckCard.context = context;
	}
	
	//Check card balance
	public void balance(View view, String cardNumber) {
		final TicketUI ui           = new TicketUI(getContext());
		final ProgressDialog dialog = ui.showLoader();
			
		ui.hideKeyboard(view);
			
		TicketAPI.get(CardFormat.clean(cardNumber), null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			    	
			    try {
					//if card is valid
					if (json.has("balance")) {
						Intent intent = new Intent();
						intent.setClass(getContext(), ResultActivity.class);
							
						Bundle bundle = new Bundle();
						bundle.putString("json", json.toString());
							
						intent.putExtras(bundle);
						getContext().startActivity(intent);
							
					} else {
						ui.showToast((String) json.get("error"));
					}
				} catch (JSONException e) {
					ui.showToast(getContext().getString(R.string.connection_error));
					e.printStackTrace();
				}
			}
				
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
					
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				ui.showToast(getContext().getString(R.string.connection_error));
			}
		});
	}
}
