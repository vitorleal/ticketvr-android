package com.vleal.ticketvr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vleal.ticketvr.api.CheckCard;
import com.vleal.ticketvr.model.Card;
import com.vleal.ticketvr.sqlite.helper.DatabaseHelper;
import com.vleal.ticketvr.ui.AddCardDialog;
import com.vleal.ticketvr.ui.CardFormat;
import com.vleal.ticketvr.ui.CardListAdapter;
import com.vleal.ticketvr.ui.ValidateInputLength;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		if (!intent.hasExtra("card")) {
			overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
		}

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
				addNewCard(null);
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
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

	
	//Sections pager adapter
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			
			if (position == 0) {
				fragment = new AddCardSectionFragment();
				
			} else {
				fragment = new CheckBalanceSectionFragment();
			}
			
			Bundle args = new Bundle();
			fragment.setArguments(args);
			
			return fragment;
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
			final EditText cardNumber = (EditText) view.findViewById(R.id.cardNumber);
			Button checkBalance       = (Button)   view.findViewById(R.id.checkButton);
			
			cardNumber.addTextChangedListener(new ValidateInputLength(checkBalance));
			
			//on button click
			checkBalance.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String number       = cardNumber.getText().toString();
					CheckCard checkCard = new CheckCard(v.getContext());
					checkCard.balance(v, number, null);
				}
			});
			
			super.onViewCreated(view, savedInstanceState);
		}
	}
	
	
	//AddCard Section fragment
	public static class AddCardSectionFragment extends Fragment {
		public AddCardSectionFragment() {}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_addcard, container, false);
			getCardsList(rootView);
			
			return rootView;
		}
	}
	
	
	//AddCard dialog fragment
	public void addNewCard(View view) {
		DialogFragment cardDialog = AddCardDialog.newInstance();
		cardDialog.show(getFragmentManager(), null);
	}

	
	//Get all cards and fill the card list
	public static void getCardsList(View rootView) {
		DatabaseHelper db   = new DatabaseHelper(rootView.getContext());
		List<Card> cards    = db.getAllCards();
		LinearLayout noCard = (LinearLayout) rootView.findViewById(R.id.no_card);
		ListView cardsList  = (ListView)     rootView.findViewById(R.id.cards_list);
		
		db.closeDB();
		
		if (cards.isEmpty()) {
			noCard.setVisibility(View.VISIBLE);
			cardsList.setVisibility(View.GONE);
			
		} else {
			noCard.setVisibility(View.GONE);
			cardsList.setVisibility(View.VISIBLE);
			
			fillCardsList(cards, cardsList, rootView);
		}
	}
	
	
	//Fill the card list
	public static void fillCardsList(List<Card> cards, ListView cardsList, View view) {
		final Context context              = (Context) view.getContext();
		List<Map<String, ?>> cardListArray = new ArrayList<Map<String , ?>>();
		
		for(int i = 0; i < cards.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			
	   		String cardNumber = (String) cards.get(i).getCardNumber();
	   		String cardName   = (String) cards.get(i).getCardName();
	   		long id           = (long) cards.get(i).getId(); 
	   		
	   		map.put("id",         String.valueOf(id));
	   		map.put("cardNumber", CardFormat.string(cardNumber));
	   		map.put("cardName",   cardName);
	   		
	   		cardListArray.add(map);
	  	}
		
		cardsList.setAdapter(new CardListAdapter(context, cardListArray, R.layout.card_list_item,
			new String[] { "cardNumber", "cardName", "id" },  new int[] { R.id.card_number, R.id.card_name, R.id.card_id }));
		
		//on item click
		cardsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				TextView cardNumberView = (TextView) view.findViewById(R.id.card_number);
				String cardNumber       = (String)   cardNumberView.getText();
				TextView cardIdView     = (TextView) view.findViewById(R.id.card_id);
				String cardId           = (String) cardIdView.getText().toString();
				
				CheckCard checkCard     = new CheckCard(view.getContext());
				checkCard.balance(cardNumberView, cardNumber, cardId);
			}
		});
	}
}
