package com.vleal.ticketvr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String value  = intent.getStringExtra("value");
		String number = intent.getStringExtra("number");
		//String date   = intent.getStringExtra("date");
		
		TextView cardNumber = (TextView) findViewById(R.id.cardNumber);
		TextView money      = (TextView) findViewById(R.id.money);
		
		cardNumber.setText(number);
		money.setText(value);
		
		setContentView(R.layout.activity_result);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}*/

}
