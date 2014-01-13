package com.example.cardslist.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName = "materials" )
public class MaterialModel extends Model
{
	public static final String TABLE = "materials";
	
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_CURRENT_AMOUNT = "current_amount";
	public static final String COLUMN_MINIMAL_AMOUNT = "minimal_amount";
	public static final String COLUMN_CATEGORY = "category_id";
	public static final String COLUMN_IMAGE = "image_id";
	
	@DatabaseField( columnName = COLUMN_CATEGORY, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true )
	private CategoryModel category;
	
	@DatabaseField( columnName = COLUMN_TITLE )
	private String title;
	
	@DatabaseField( columnName = COLUMN_CURRENT_AMOUNT )
	private float current_amount;
	
	@DatabaseField( columnName = COLUMN_MINIMAL_AMOUNT )
	private float minimal_amount;
	
	@DatabaseField( columnName = COLUMN_IMAGE, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true )
	private ImageModel image;
	
	public MaterialModel()
	{
	}
	
	public MaterialModel( String title, CategoryModel category )
	{
		this( title, category, 0f, 0f );
	}
	
	public MaterialModel( String title, CategoryModel category, float current_amount, float minimal_amount )
	{
		super();
		this.title = title;
		this.category = category;
		this.current_amount = current_amount;
		this.minimal_amount = minimal_amount;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle( String title )
	{
		this.title = title;
	}
	
	public CategoryModel getCategory()
	{
		return category;
	}
	
	public void setCategory( CategoryModel category )
	{
		this.category = category;
	}
	
	public float getCurrentAmount()
	{
		return current_amount;
	}
	
	public void setCurrentAmount( float current_amount )
	{
		this.current_amount = current_amount;
	}
	
	public float getMinimalAmount()
	{
		return minimal_amount;
	}
	
	public void setMinimalAmount( float minimal_amount )
	{
		this.minimal_amount = minimal_amount;
	}
	
	public ImageModel getImage() {
		return image;
	}
	
	public void setImage(ImageModel image) {
		this.image = image;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append( TABLE ).append( "[ id=" ).append( getId() ).append( ", title=" ).append( getTitle() )
		.append( ", category=" ).append( getCategory() ).append( ", current_amount=" ).append( getCurrentAmount() )
		.append( ", minimal_amount=" ).append( getMinimalAmount() )
		.append( ", image=" ).append( getImage() ).append( "]" );
		return builder.toString();
	}
}
