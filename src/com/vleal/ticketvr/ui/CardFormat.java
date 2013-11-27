package com.vleal.ticketvr.ui;

public class CardFormat {
	public static String string(String card) {
		String cardArray = card.replaceAll("\\d{4}", "$0 ").trim();
		
		return cardArray;
	}
	
	public static String clean(String card) {
		String cardClean = card.replace(" ", "");
		return cardClean;
	}
}
