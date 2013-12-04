package com.vleal.ticketvr.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.vleal.ticketvr.R;

public class TransactionListAdapter extends SimpleAdapter {
	
	private List<?> data;
	
	public TransactionListAdapter(Context context, List<Map<String, ?>> lastTransactionsList, int resource, String[] from, int[] to) {
		super(context, CreateList(lastTransactionsList), resource, from, to);
		this.data = lastTransactionsList;
	}

	private static List<? extends Map<String, ?>> CreateList(List<Map<String, ?>> lastTransactionsList) {
		return (List<? extends Map<String, ?>>) lastTransactionsList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view          = super.getView(position, convertView, parent);
		TextView textView  = (TextView) view.findViewById(R.id.listItemValue);
		String description = (String) ((Map<?, ?>) data.get(position)).get("description");
		
		if (description.trim().matches("Disponib. De Credito")) {
			Log.i("id", description);
			textView.setTextColor(getGreenColor(view));
			
		} else {
			textView.setTextColor(getRedColor(view));
		}
		
		return view;
	}

	private int getGreenColor(View view) {
		return view.getResources().getColor(R.color.green);
	}
	
	private int getRedColor(View view) {
		return view.getResources().getColor(R.color.main_red);
	}
	
}
