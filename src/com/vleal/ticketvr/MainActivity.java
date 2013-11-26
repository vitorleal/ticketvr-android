package com.vleal.ticketvr;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.vleal.ticketvr.api.TicketAPI;
import com.vleal.ticketvr.ui.AddCardDialog;
import com.vleal.ticketvr.ui.ValidateInputLength;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager            = (ViewPager) findViewById(R.id.pager);
		
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
				.setText(mSectionsPagerAdapter.getPageTitle(i))
				.setTabListener(this));
		}
	}

	//Inflate the menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Handle the menu item click
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add_card:
				AddNewCard(null);
				return true;

			case R.id.action_settings:
				//showHelp();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	
	//Sections pager adapter
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				Fragment fragment = new AddCardSectionFragment();
				Bundle args       = new Bundle();
				fragment.setArguments(args);
				return fragment;
				
			} else {
				Fragment fragment = new CheckBalanceSectionFragment();
				Bundle args       = new Bundle();
				//args.putInt(CheckBalanceSectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			
			switch (position) {
			case 0:
				return getString(R.string.my_cards).toUpperCase(l);
				
			case 1:
				return getString(R.string.no_save).toUpperCase(l);
			}
			return null;
		}
	}
	
	
	//Check balance section fragment
	public static class CheckBalanceSectionFragment extends Fragment {
		public CheckBalanceSectionFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_check_balance, container, false);
			
			return rootView;
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			EditText cardNumber       = (EditText) view.findViewById(R.id.cardNumber);
			final Button checkBalance = (Button)   view.findViewById(R.id.checkButton);
			
			cardNumber.addTextChangedListener(new ValidateInputLength(checkBalance));
			
			super.onViewCreated(view, savedInstanceState);
		}
	}
	
	//Check card balance
	public void checkMyBalance(View view) {
		EditText cardField          = (EditText) findViewById(R.id.cardNumber); 
		String cardNumber           = cardField.getText().toString();
		final ProgressDialog dialog = showLoader(this);
		
		hideKeyboard(view);
		
		TicketAPI.get(cardNumber, null, new JsonHttpResponseHandler() {
			@Override
		    public void onSuccess(JSONObject json) {
		    	if (dialog.isShowing()) {
		    		dialog.dismiss();
		        }
		    	
		    	try {
					//if card is valid
					if (json.has("balance")) {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), ResultActivity.class);
						
						Bundle bundle = new Bundle();
						bundle.putString("json", json.toString());
						
						intent.putExtras(bundle);
					    startActivity(intent);
						
					} else {
						showToast((String) json.get("error"));
					}
				} catch (JSONException e) {
					showToast(getString(R.string.connection_error));
					e.printStackTrace();
				}
		    }
			
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
				
				if (dialog.isShowing()) {
		    		dialog.dismiss();
		        }
				
				showToast(getString(R.string.connection_error));
			}
		});
	}
	
	
	//AddCard Section fragment
	public static class AddCardSectionFragment extends Fragment {
		public AddCardSectionFragment() {}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_addcard, container, false);
			
			return rootView;
		}
	}
	
	
	//AddCard dialog fragment
	public void AddNewCard(View view) {
		DialogFragment cardDialog = AddCardDialog.newInstance();
		cardDialog.show(getFragmentManager(), null);
	}
	
	
	//Async Loader
	public ProgressDialog showLoader(Context context) {
		ProgressDialog dialog = new ProgressDialog(context);
    	dialog.setCancelable(false);
    	dialog.setTitle(R.string.loader_title);
    	dialog.setMessage(getString(R.string.loading));
        dialog.show();
        
        return dialog;
	}
	
	//Hide keyboard
	protected void hideKeyboard(View view) {
	    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	//Toast
	public void showToast(String text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

}
