package com.vleal.ticketvr.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

public class ValidateInputLength implements TextWatcher {

	private Button button;
	
	public ValidateInputLength(Button button) {
		this.button = button;
	}
	
	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() == 16) {
			button.setEnabled(true);
			
		} else {
			button.setEnabled(false);
		}
    }

}
