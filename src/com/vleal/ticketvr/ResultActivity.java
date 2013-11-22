package com.vleal.ticketvr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		Bundle intent = getIntent().getExtras();
		String string = intent.getString("json");
		
		try {
			JSONObject json      = new JSONObject(string);
			JSONObject balance   = (JSONObject) json.get("balance");
			JSONArray scheduling = (JSONArray)  json.get("scheduling");
			
			String value         = balance.getString("value");
			String number        = balance.getString("number");
			
			TextView cardNumber  = (TextView) findViewById(R.id.cardNumber);
			TextView money       = (TextView) findViewById(R.id.money);
			
			cardNumber.setText("Cart‹o: "+ number);
			money.setText("R$ "+ value);
			
			if (scheduling != null && scheduling.length() > 0) {
				JSONObject schedualJson = (JSONObject) scheduling.get(0);
				String schedualDate     = (String) schedualJson.get("date");
				String schedualdesc     = (String) schedualJson.get("description");
				String schedualValue    = (String) schedualJson.get("value");
				
				FrameLayout greenBox    = (FrameLayout) findViewById(R.id.greenBox);
				TextView nextDeposit    = (TextView) findViewById(R.id.nextDeposit);
				TextView descDesdposit  = (TextView) findViewById(R.id.descDeposit);
				TextView valueDesposit  = (TextView) findViewById(R.id.valueDeposit);
				
				nextDeposit.setText("Pr—ximo dep—sito: "+ schedualDate);
				descDesdposit.setText(schedualdesc);
				valueDesposit.setText("R$ "+ schedualValue);
				greenBox.setVisibility(View.VISIBLE);
			}
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		setTitle(R.string.your_balance);
	}
	
	public String FormatCard(String card) {
		String[] cardArray = TextUtils.split(card, "\\d{1,4}");
		Log.i("array", "" + cardArray);
		String cardFormat  = TextUtils.join(" ", cardArray);
		Log.i("card", "" + cardArray.toString());
		
		return cardFormat;
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}*/

}
