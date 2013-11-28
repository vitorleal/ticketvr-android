package com.vleal.ticketvr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vleal.ticketvr.api.CheckCard;
import com.vleal.ticketvr.ui.CardFormat;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_result);
		
		Bundle intent = getIntent().getExtras();
		String string = intent.getString("json");
		
		try {
			JSONObject json      = new JSONObject(string);
			JSONObject balance   = (JSONObject) json.get("balance");
			JSONArray scheduling = (JSONArray)  json.get("scheduling");
			String token         = (String) json.get("token");

			String value         = balance.getString("value");
			String number        = balance.getString("number");
			String date          = balance.getString("date");
			ListView list        = (ListView) findViewById(R.id.list);
			
			TextView cardNumber  = (TextView) findViewById(R.id.cardNumber);
			TextView money       = (TextView) findViewById(R.id.money);
			ProgressBar loader   = (ProgressBar) findViewById(R.id.loader_list);
			
			cardNumber.setText("Cart�o: "+ CardFormat.string(number));
			money.setText("R$ "+ value);
			
			if (scheduling != null && scheduling.length() > 0) {
				JSONObject schedualJson = (JSONObject) scheduling.get(0);
				String schedualDate     = (String) schedualJson.get("date");
				String schedualdesc     = (String) schedualJson.get("description");
				String schedualValue    = (String) schedualJson.get("value");
				
				FrameLayout greenBox    = (FrameLayout) findViewById(R.id.greenBox);
				View greenLine          = (View)        findViewById(R.id.greenLine);
				TextView nextDeposit    = (TextView)    findViewById(R.id.nextDeposit);
				TextView descDesdposit  = (TextView)    findViewById(R.id.descDeposit);
				TextView valueDesposit  = (TextView)    findViewById(R.id.valueDeposit);
				
				nextDeposit.setText("Pr�ximo dep�sito: "+ schedualDate);
				descDesdposit.setText(schedualdesc);
				valueDesposit.setText("R$ "+ schedualValue);
				greenBox.setVisibility(View.VISIBLE);
				greenLine.setVisibility(View.VISIBLE);
			}
			
			CheckCard checkCard = new CheckCard(this);
			checkCard.list(list, loader, number, token);
			
			setTitle(getString(R.string.your_balance) + " - " + date);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
	}
}
