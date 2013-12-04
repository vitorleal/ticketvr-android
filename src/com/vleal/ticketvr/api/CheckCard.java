package com.vleal.ticketvr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.vleal.ticketvr.R;
import com.vleal.ticketvr.ResultActivity;
import com.vleal.ticketvr.ui.CardFormat;
import com.vleal.ticketvr.ui.TicketUI;
import com.vleal.ticketvr.ui.TransactionListAdapter;

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
	public void balance(View view, String cardNumber, final String cardId) {
		final TicketUI ui           = new TicketUI(getContext());
		final ProgressDialog dialog = ui.showLoader();
			
		ui.hideKeyboard(view);
			
		TicketAPI.get(CardFormat.clean(cardNumber), "card", null, null, new JsonHttpResponseHandler() {
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
						bundle.putString("cardId", cardId);
							
						intent.putExtras(bundle);
						getContext().startActivity(intent);
							
					} else {
						String error = (String) json.get("error");

						if (cardId != null) {
							TicketUI ui = new TicketUI(getContext());
							ui.confirmDeleteCard(error, cardId);
							
						} else {
							TicketUI.showToast(error);
						}
					}
				} catch (JSONException e) {
					TicketUI.showToast(getContext().getString(R.string.connection_error));
					e.printStackTrace();
				}
			}
				
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
					
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				TicketUI.showToast(getContext().getString(R.string.connection_error));
			}
		});
	}
	
	public void list(final ListView list, final ProgressBar loader, String cardNumber, String token) {
		TicketAPI.get(CardFormat.clean(cardNumber), "listonly", token, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				
				loader.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				
			    try {
					//if has list
					if (json.has("list")) {
						JSONArray arrayList = (JSONArray) json.get("list");
						
						if (arrayList != null && arrayList.length() > 0) {
							List<Map<String, ?>> lastTransactionsList = new ArrayList<Map<String , ?>>();
							
							for(int i = 0; i < arrayList.length(); i++) {
								Map<String, String> map = new HashMap<String, String>();
								JSONObject listItem     = arrayList.getJSONObject(i);
						   		String itemValue        = listItem.optString("value");
						   		String itemDate         = listItem.optString("date");
						   		String itemDesc         = listItem.optString("description");
						   		
						   		map.put("value", "R$ " + itemValue);
						   		map.put("date", itemDate);
						   		map.put("description", Capitalize(itemDesc));
						   		
						   		lastTransactionsList.add(map);
						  	}
							
							list.setAdapter(new TransactionListAdapter(getContext(), lastTransactionsList, R.layout.list_item,
								new String[] { "date", "value", "description" }, 
								new int[]    { R.id.listItemDate, R.id.listItemValue , R.id.listItemDescription }));
						}
							
					} else {
						TicketUI.showToast((String) json.get("error"));
						loader.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					TicketUI.showToast(getContext().getString(R.string.connection_error));
					loader.setVisibility(View.GONE);
					e.printStackTrace();
				}
			}
				
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
				loader.setVisibility(View.GONE);
				TicketUI.showToast(getContext().getString(R.string.connection_error));
			}
		});	
	}
	
	//Capitalize
	public String Capitalize(String text) {
		Locale l              = Locale.getDefault();
		String[] textSplit    = text.toLowerCase(l).split(" ");
		String textCaptalized = "";
			
		for(String w: textSplit) {
			w = w.substring(0, 1).toUpperCase(l) + w.substring(1);
			textCaptalized += " " + w;
		}

		return textCaptalized.trim().replaceAll("[-]$", "");
	}
}
