package com.example.cardslist.database;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName = "categories" )
public class CategoryModel extends Model
{
	public static final String TABLE = "categories";
	
	public static final String COLUMN_SUPER_CATEGORY = "parent_id";
	public static final String COLUMN_TITLE = "title";
	
	@DatabaseField( columnName = COLUMN_SUPER_CATEGORY, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true )
	private CategoryModel parent;
	
	@ForeignCollectionField
	private ForeignCollection<CategoryModel> childs;
	
	@ForeignCollectionField
	private ForeignCollection<MaterialModel> materials;
	
	@DatabaseField( columnName = COLUMN_TITLE )
	private String title;
	
	CategoryModel()
	{		
	}
	
	public CategoryModel( final String title )
	{
		super();
		this.title = title;
	}
	
	public CategoryModel( final String title, final CategoryModel parent )
	{
		this( title );
		this.parent = parent;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle( String title )
	{
		this.title = title;
	}
	
	public CategoryModel getSuperCategory()
	{
		return parent;
	}
	
	public void setSuperCategory( CategoryModel superCategory )
	{
		this.parent = superCategory;
	}
	
	public ForeignCollection<CategoryModel> getSubCategories()
	{
		return childs;
	}
	
	public void setSubCategories( ForeignCollection<CategoryModel> subCategories )
	{
		this.childs = subCategories;
	}
	
	public ForeignCollection<MaterialModel> getMaterials()
	{
		return materials;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append( TABLE ).append( "[ id=" ).append( getId() ).append( ", title=" ).append( getTitle() )
		.append( ", parent=" ).append( getSuperCategory() ).append( ", childs=" ).append( getSubCategories() ).append( "]" );
		return builder.toString();
	}
}
