package com.example.cardslist;

public class CardContentItem implements CardItem {
	private String content;
	private int imageResourceId;
	
	@Override
	public boolean isHeader() {
		return false;
	}

	public CardContentItem( String content, int imageResourceId ) {
		this.content = content;
		this.imageResourceId = imageResourceId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getImageResourceId() {
		return imageResourceId;
	}
	
	public void setImageResourceId(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}
}
