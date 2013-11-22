package com.vleal.ticketvr;

import java.lang.reflect.Array;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		Intent intent = getIntent();
		String value  = intent.getStringExtra("value");
		String number = intent.getStringExtra("number");
		
		TextView cardNumber = (TextView) findViewById(R.id.cardNumber);
		TextView money      = (TextView) findViewById(R.id.money);
		
		cardNumber.setText(number);
		money.setText(value);
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
