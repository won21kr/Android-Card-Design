package com.example.cardslist.database;

import android.graphics.Color;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName = "images" )
public class ImageModel extends Model {
	public static final String TABLE = "images";
	
	public static final String COLUMN_RESOURCE_ID = "resource_id";
	public static final String COLUMN_WEB_URL = "web_url";
	public static final String COLUMN_COLOR = "color";
	
	@DatabaseField( columnName = COLUMN_RESOURCE_ID )
	private int resourceId;
	
	@DatabaseField( columnName = COLUMN_WEB_URL )
	private String webUrl;
	
	@DatabaseField( columnName = COLUMN_COLOR )
	private int color;
	
	public ImageModel() {
		
	}
	
	public int getResourceId() {
		return resourceId;
	}
	
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	
	public boolean isResourceId() {
		return (resourceId > 0);
	}
	
	public String getWebUrl() {
		return webUrl;
	}
	
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	
	public boolean isWebUrl() {
		return (webUrl != null);
	}
	
	public int getColor() {
		return color;
	}
	
	/**
	 * Parse color to Integer and if this fails it use a TRANSPARENT color.
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		try {
			this.color = Color.parseColor( color );
		} catch( IllegalArgumentException e) {
			this.color = Color.TRANSPARENT;
		}
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public boolean isColor() {
		return (color > 0);
	}
	
	/**
	 * Hierarchy is ResourceId, WebUrl, Color.
	 * 
	 * @return first existing value
	 */
	public Object getImage() {
		if( isResourceId() ) {
			return getResourceId();
		} else if( isWebUrl() ) {
			return getWebUrl();
		} else {
			return getColor();
		}
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append( TABLE ).append( "( " )
			.append( COLUMN_RESOURCE_ID ).append( "=" ).append( getResourceId() ).append(",")
			.append( COLUMN_WEB_URL ).append( "=" ).append( getWebUrl() ).append(",")
			.append( COLUMN_COLOR ).append( "=" ).append( getColor() )
			.append( " )" ).toString();
	}
}
