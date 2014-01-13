package com.example.cardslist.database;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public abstract class Model
{
	public static final String ID_COLUMN = "_id";
	public static final String CREATION_DATE_COLUMN = "created";
	public static final String EDIT_DATE_COLUMN = "changed";
	
	@DatabaseField( generatedId = true, columnName = ID_COLUMN )
	private int id;
	
	@DatabaseField( columnName = CREATION_DATE_COLUMN, dataType = DataType.DATE_LONG )
	private Date creationDate;
	
	@DatabaseField( columnName = EDIT_DATE_COLUMN, dataType = DataType.DATE_LONG )
	private Date lastChangedDate;
	
	public Model()
	{
		creationDate = new Date(); 
		lastChangedDate = creationDate;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId( int id )
	{
		this.id = id;
	}
	
	public Date getCreationDate()
	{
		return creationDate;
	}
	
	public void setCreationDate( Date creationDate )
	{
		this.creationDate = creationDate;
	}
	
	public Date getLastChangedDate()
	{
		return lastChangedDate;
	}
	
	public void setLastChangedDate( Date lastChangedDate )
	{
		this.lastChangedDate = lastChangedDate;
	}
}
