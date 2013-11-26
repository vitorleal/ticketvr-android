package com.vleal.ticketvr.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TicketAPI {
	private static final String BASE_URL  = "http://ticketvr.herokuapp.com/list/";
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String card, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(30000);
		client.get(getAbsoluteUrl(card), params, responseHandler);
	}
	
	private static String getAbsoluteUrl(String card) {
		return BASE_URL + card;
	}
}