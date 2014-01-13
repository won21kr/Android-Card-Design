package com.example.cardslist.database;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class DatabaseConfigUtils extends OrmLiteConfigUtil
{
	public static final Class<?>[] classes = new Class[]
	{
	    CategoryModel.class,
	    MaterialModel.class,
	    ImageModel.class
	};
	
	public static void main( String[] args ) throws SQLException, IOException
	{
		writeConfigFile( "ormlite_config.txt", classes );
	}
}