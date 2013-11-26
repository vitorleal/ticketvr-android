package com.vleal.ticketvr;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class AddCardDialog extends DialogFragment {

	static AddCardDialog newInstance() {
        return new AddCardDialog();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final Dialog dialog = getDialog();
		View view           = inflater.inflate(R.layout.fragment_dialog_add_card, container, false);
		
		dialog.setTitle(R.string.add_card);
		dialog.setCancelable(false);

		//EditText numberCard = (EditText) view.findViewById(R.id.number_card);
		//EditText nameCart   = (EditText) view.findViewById(R.id.name_card);
		Button cancelButton = (Button) view.findViewById(R.id.cancel_card);
		Button saveCard = (Button) view.findViewById(R.id.save_card);
		
		//Cancel button click
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		
		//Save button click
		saveCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
        
		return view;
	}
		
}
