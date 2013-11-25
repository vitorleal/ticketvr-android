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
import android.widget.SimpleAdapter;
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
			
			cardNumber.setText("Cart‹o: "+ number);
			money.setText("R$ "+ value);
			
			if (scheduling != null && scheduling.length() > 0) {
				JSONObject schedualJson = (JSONObject) scheduling.get(0);
				String schedualDate     = (String) schedualJson.get("date");
				String schedualdesc     = (String) schedualJson.get("description");
				String schedualValue    = (String) schedualJson.get("value");
				
				FrameLayout greenBox    = (FrameLayout) findViewById(R.id.greenBox);
				TextView nextDeposit    = (TextView)    findViewById(R.id.nextDeposit);
				TextView descDesdposit  = (TextView)    findViewById(R.id.descDeposit);
				TextView valueDesposit  = (TextView)    findViewById(R.id.valueDeposit);
				
				nextDeposit.setText("Pr—ximo dep—sito: "+ schedualDate);
				descDesdposit.setText(schedualdesc);
				valueDesposit.setText("R$ "+ schedualValue);
				greenBox.setVisibility(View.VISIBLE);
			}
			
			if (arrayList != null && arrayList.length() > 0) {
				ListView list                  = (ListView) findViewById(R.id.list);
				List<Map> lastTransactionsList = new ArrayList<Map>();
				
				for(int i = 0; i < arrayList.length(); i++) {
					Map map             = new HashMap();
					JSONObject listItem = arrayList.getJSONObject(i);
			   		String itemValue    = listItem.optString("value");
			   		String itemDate     = listItem.optString("date");
			   		String itemDesc     = listItem.optString("description");
			   		
			   		map.put("value", itemValue);
			   		map.put("date", itemDate);
			   		map.put("description", itemDesc);
			   		
			   		lastTransactionsList.add(map);
			  	}
				
				SimpleAdapter simpleAdapter = new SimpleAdapter(this, extracted(lastTransactionsList),
						R.layout.list_usage, 
						new String[] { "date", "value", "description" }, 
						new int[] { R.id.listItemDate, R.id.listItemValue , R.id.listItemDescription });
				list.setAdapter(simpleAdapter);
			}
			
			setTitle(getString(R.string.your_balance) + " - " + date);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
	}

	private List<? extends Map<String, ?>> extracted(List<Map> lastTransactionsList) {
		return (List<? extends Map<String, ?>>) lastTransactionsList;
	}
	
	private HashMap<String, String> createListItem(String name, String value) {
		HashMap<String, String> itemNameValue = new HashMap<String, String>();
		itemNameValue.put(name, value);
		return itemNameValue;
	}

	
	/*public String FormatCard(String card) {
		String[] cardArray = TextUtils.split(card, "\\d{1,4}");
		Log.i("array", "" + cardArray);
		String cardFormat  = TextUtils.join(" ", cardArray);
		Log.i("card", "" + cardArray.toString());
		
		return cardFormat;
	}*/

}
