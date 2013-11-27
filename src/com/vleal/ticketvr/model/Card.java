package com.vleal.ticketvr.model;

import com.vleal.ticketvr.ui.CardFormat;

public class Card {
	int id;
	String cardNumber;
	String cardName;
	
	public Card() {}
	
	public Card(String cardNumber, String cardName) {
		this.cardNumber = CardFormat.string(cardNumber);
		this.cardName   = cardName;
	}
	
	//Setters
	public void setId(int id) {
        this.id = id;
    }
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	//Getters
	public long getId() {
		return this.id;
	}
	public String getCardNumber() {
		return this.cardNumber;
	}
	public String getCardName() {
		return this.cardName;
	}
}
