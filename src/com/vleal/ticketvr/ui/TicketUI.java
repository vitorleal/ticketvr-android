package com.vleal.ticketvr.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.vleal.ticketvr.R;

public class TicketUI {
	private static Context context;

	 public TicketUI(Context context) {
		 TicketUI.setContext(context);
	}
	
	// Get/set context
	public static Context getContext() {
		return context;
	}
	public static void setContext(Context context) {
		TicketUI.context = context;
	}
	
	// Asynchronous Loader
	public ProgressDialog showLoader() {
		ProgressDialog dialog = new ProgressDialog(context);
	    dialog.setCancelable(false);
	    dialog.setTitle(R.string.loader_title);
	    dialog.setMessage(getContext().getString(R.string.loading));
	    dialog.show();
	        
	    return dialog;
	}
	
	// Toast
	public void showToast(String text) {
		Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	//Hide keyboard
	public void hideKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
