package com.vleal.ticketvr.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TicketAPI {
	private static final String BASE_URL  = "http://api.ticketvrapp.com/";
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String card, String type, String token, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(30000);
		client.get(getAbsoluteUrl(card, type, token), params, responseHandler);
	}
	
	private static String getAbsoluteUrl(String card, String type, String token) {
		if (token == null) {
			return BASE_URL + type + "/" + card;
			
		} else {
			return BASE_URL + type + "/" + card + "/" + token;
		}
	}
}