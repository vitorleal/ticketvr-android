package com.vleal.ticketvr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

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
			JSONArray arrayList  = (JSONArray)  json.get("list");
			
			String value         = balance.getString("value");
			String number        = balance.getString("number");
			String date          = balance.getString("date");
			
			TextView cardNumber  = (TextView) findViewById(R.id.cardNumber);
			TextView money       = (TextView) findViewById(R.id.money);
			
			cardNumber.setText("Cart‹o: "+ FormatCard(number));
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
				
				nextDeposit.setText("Pr—ximo dep—sito: "+ schedualDate);
				descDesdposit.setText(schedualdesc);
				valueDesposit.setText("R$ "+ schedualValue);
				greenBox.setVisibility(View.VISIBLE);
				greenLine.setVisibility(View.VISIBLE);
			}
			
			if (arrayList != null && arrayList.length() > 0) {
				ListView list                  = (ListView) findViewById(R.id.list);
				List<Map<String, ?>> lastTransactionsList = new ArrayList<Map<String , ?>>();
				
				for(int i = 0; i < arrayList.length(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					JSONObject listItem     = arrayList.getJSONObject(i);
			   		String itemValue        = listItem.optString("value");
			   		String itemDate         = listItem.optString("date");
			   		String itemDesc         = listItem.optString("description");
			   		
			   		map.put("value", "R$ " + itemValue);
			   		map.put("date", itemDate);
			   		map.put("description", Captalize(itemDesc));
			   		
			   		lastTransactionsList.add(map);
			  	}
				
				list.setAdapter(new MyListAdapter(this, lastTransactionsList, R.layout.list_usage,
						new String[] { "date", "value", "description" }, 
						new int[]    { R.id.listItemDate, R.id.listItemValue , R.id.listItemDescription }));
			}
			
			setTitle(getString(R.string.your_balance) + " - " + date);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
	}

	//Format string as card 0000 0000 0000 0000
	public String FormatCard(String card) {
		String cardArray = card.replaceAll("\\d{4}", "$0 ").trim();
		
		return cardArray;
	}

	//Captalize
	public String Captalize(String text) {
		String[] textSplit    = text.toLowerCase().split(" ");
		String textCaptalized = "";
		
		for(String w: textSplit) {
			w = w.substring(0, 1).toUpperCase() + w.substring(1);
			textCaptalized += " " + w;
		}

		return textCaptalized.trim().replaceAll("[-]$", "");
	}
}
