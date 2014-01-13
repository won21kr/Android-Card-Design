package com.example.cardslist;

public class CardHeaderItem implements CardItem {
	private String headerTitle;
	private String actionTitle;
	
	@Override
	public boolean isHeader() {
		return true;
	}
	
	public CardHeaderItem( String title ) {
		headerTitle = title;
		actionTitle = null;
	}
	
	public CardHeaderItem( String title, String action ) {
		headerTitle = title;
		actionTitle = action;
	}

	public String getHeaderTitle() {
		return headerTitle;
	}
	
	public String getActionTitle() {
		return actionTitle;
	}
	
	public boolean hasAction() {
		return (actionTitle != null);
	}
}
