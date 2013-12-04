package com.vleal.ticketvr.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.vleal.ticketvr.MainActivity;
import com.vleal.ticketvr.R;
import com.vleal.ticketvr.sqlite.helper.DatabaseHelper;

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
	public static void showToast(String text) {
		Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	//Hide keyboard
	public void hideKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	
	//Alert to remove card
	public void confirmDeleteCard(String error, final String cardId) {
		final Context context     = getContext();
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(error);
	    alert.setMessage(R.string.alert_remove_card);
	    
	    //YES
	    alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	DatabaseHelper db   = new DatabaseHelper(context);
	        	Long id             = Long.parseLong(cardId);
	        	
	        	db.deleteToDo(id);
	        	
	    		TicketUI.showToast(context.getString(R.string.removed_card));
	    		
	        	Intent intent = new Intent(context, MainActivity.class);
	        	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	        	intent.putExtra("removeCard", true);
	    		context.startActivity(intent);
	        }
	     });
	    
	    //NO
	    alert.setNegativeButton("N‹o", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {}
	     });
	    
	    alert.show();
	}
}
