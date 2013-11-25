package com.vleal.ticketvr;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

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

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

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
			
			cardNumber.addTextChangedListener(new TextWatcher(){
				@Override
		        public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s.length() == 16) {
						checkBalance.setEnabled(true);
						
					} else {
						checkBalance.setEnabled(false);
					}
		        }
				
				@Override
				public void afterTextChanged(Editable arg0) {}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		    });
			
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
					if (json.has("valid")) {
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
					showToast("Erro ao conectar");
					e.printStackTrace();
				}
		    }
			
			@Override
		    public void onFailure(Throwable t, String error) {
				if (dialog.isShowing()) {
		    		dialog.dismiss();
		        }
				
				showToast(error);
		    }
		});
	}
	
	
	//Add card Section fragment
	public static class AddCardSectionFragment extends Fragment {
		public AddCardSectionFragment() {}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_addcard, container, false);
			
			return rootView;
		}
	}
	
	
	//Async Loader
	public ProgressDialog showLoader(Context context) {
		ProgressDialog dialog = new ProgressDialog(context);
    	dialog.setCancelable(false);
    	dialog.setTitle(R.string.loader_title);
    	dialog.setMessage("carregando...");
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
	
	//Database
	/*public class CardOpenHelper extends SQLiteOpenHelper {

	    private static final int DATABASE_VERSION = 2;
	    private static final String DICTIONARY_TABLE_NAME = "cards";
	    private static final String DICTIONARY_TABLE_CREATE =
	                "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
	                KEY_WORD + " TEXT, " +
	                KEY_DEFINITION + " TEXT);";

	    DictionaryOpenHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(DICTIONARY_TABLE_CREATE);
	    }

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		}
	}*/

}
