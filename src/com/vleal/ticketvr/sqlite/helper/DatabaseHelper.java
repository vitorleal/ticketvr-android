package com.vleal.ticketvr.sqlite.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vleal.ticketvr.model.Card;

public class DatabaseHelper extends SQLiteOpenHelper {
	
    private static final String LOG            = "DatabaseHelper";
    private static final int DATABASE_VERSION  = 1;
    private static final String DATABASE_NAME  = "cardManager";
    private static final String TABLE_CARDS    = "cards";
    private static final String KEY_ID         = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_NUMBER     = "cardNumber";
    private static final String KEY_NAME       = "cardName";
 
    // CARD table create statement
    private static final String CREATE_TABLE_CARDS = "CREATE TABLE "
            + TABLE_CARDS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER
            + " TEXT," + KEY_NAME + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";
 
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CARDS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
		
		onCreate(db);
	}
	

	// Creating a card
	public long createCard(Card card) {
	    SQLiteDatabase db    = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    
	    values.put(KEY_NUMBER, card.getCardNumber());
	    values.put(KEY_NAME, card.getCardName());
	    values.put(KEY_CREATED_AT, getDateTime());
	 
	    long card_id = db.insert(TABLE_CARDS, null, values);
	    return card_id;
	}
	
	
	// Get list of cards
	public List<Card> getAllCards() {
	    List<Card> cards   = new ArrayList<Card>();
	    String selectQuery = "SELECT  * FROM " + TABLE_CARDS;
	 
	    Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c          = db.rawQuery(selectQuery, null);
	 
	    if (c.moveToFirst()) {
	        do {
	            Card card = new Card();
	            card.setId(c.getInt((c.getColumnIndex(KEY_ID))));
	            card.setCardNumber((c.getString(c.getColumnIndex(KEY_NUMBER))));
	            card.setCardName(c.getString(c.getColumnIndex(KEY_NAME)));
	 
	            cards.add(card);
	        } while (c.moveToNext());
	    }
	 
	    return cards;
	}
	
	
	// Get single card
	public Card getCard(long card_id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT * FROM " + TABLE_CARDS + " WHERE " + KEY_ID + " = " + card_id;
	 
	    Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null) {
	    	c.moveToFirst();
	    }
	 
	    Card card = new Card();
	    card.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	    card.setCardNumber((c.getString(c.getColumnIndex(KEY_NUMBER))));
	    card.setCardName((c.getString(c.getColumnIndex(KEY_NAME))));
	 
	    return card;
	}
	
	
	// Updating a card
	public int updateToDo(Card card) {
	    SQLiteDatabase db    = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    
	    values.put(KEY_NUMBER, card.getCardNumber());
	    values.put(KEY_NAME, card.getCardName());
	 
	    return db.update(TABLE_CARDS, values, KEY_ID + " = ?", new String[] { String.valueOf(card.getId()) });
	}
	
	
	// Deleting a card
	public void deleteToDo(long card_id) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_CARDS, KEY_ID + " = ?", new String[] { String.valueOf(card_id) });
	}

	
    // Get date time
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date                   = new Date();
        return dateFormat.format(date);
    }
    
    
    // Close database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
        	db.close();
        }
    }
}
