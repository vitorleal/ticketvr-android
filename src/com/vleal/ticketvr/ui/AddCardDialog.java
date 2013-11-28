package com.vleal.ticketvr.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vleal.ticketvr.R;
import com.vleal.ticketvr.model.Card;
import com.vleal.ticketvr.sqlite.helper.DatabaseHelper;

public class AddCardDialog extends DialogFragment {

	public static AddCardDialog newInstance() {
        return new AddCardDialog();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final Dialog dialog = getDialog();
		final View view     = inflater.inflate(R.layout.fragment_dialog_addcard, container, false);
		
		dialog.setTitle(R.string.add_card);
		dialog.setCancelable(false);

		final EditText numberCard = (EditText) view.findViewById(R.id.number_card);
		final EditText nameCard   = (EditText) view.findViewById(R.id.name_card);
		Button cancelButton       = (Button)   view.findViewById(R.id.cancel_card);
		Button saveCard           = (Button)   view.findViewById(R.id.save_card);
		
		//Cancel button click
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		numberCard.addTextChangedListener(new ValidateInputLength(saveCard));
		
		//Save button click
		saveCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SaveCard(numberCard.getText().toString(), nameCard.getText().toString());
				dialog.dismiss();
			}
		});
		
		return view;
	}
	
	public long SaveCard(String cardNumber, String cardName) {
		DatabaseHelper db = new DatabaseHelper(this.getActivity());
		Card newCard      = new Card(cardNumber, cardName);
		long card_id      = db.createCard(newCard);
		
		db.closeDB();
		return card_id;
	}
}
