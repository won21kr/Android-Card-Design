package com.example.cardslist.database;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cardslist.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	private static final String TAG = "DatabaseHelper";

	private Dao<CategoryModel, Integer> categoriesDao = null;
	private Dao<MaterialModel, Integer> materialsDao = null;
	private Dao<ImageModel, Integer> imagesDao = null;

	public DatabaseHelper( Context context )
	{
		super( context, "card.db", null, 7, R.raw.ormlite_config );
	}

	@Override
	public void onCreate( SQLiteDatabase sqlitedatabase, ConnectionSource connectionSource ) {
		Log.i( TAG, "onCreate()" );
		try {
			for( Class<?> clzz : DatabaseConfigUtils.classes )	{
				TableUtils.createTable( connectionSource, clzz );
			}
		} catch( SQLException e ) {
			Log.e( TAG, "Unable to create database!", e );
		}
	}

	@Override
	public void onUpgrade( SQLiteDatabase sqlitedatabase, ConnectionSource connectionSource, int oldVersion, int newVersion ) {
		Log.i( TAG, "onUpgrade" );
		try {
			for( Class<?> clzz : DatabaseConfigUtils.classes )	{
				TableUtils.dropTable( connectionSource, clzz, true );
			}
			
			onCreate( sqlitedatabase, connectionSource );
		} catch( SQLException e ) {
			Log.e( TAG, "Unable to update database from version " + oldVersion + " to " + newVersion + "!", e );
			throw new RuntimeException( e );
		}
	}

	@Override
	public void close()
	{
		super.close();

		categoriesDao = null;
		materialsDao = null;
		imagesDao = null;
	}

	public boolean clearTables() {
		final ConnectionSource connectionSource = getConnectionSource();
		try {
			for( Class<?> clzz : DatabaseConfigUtils.classes )	{
				TableUtils.clearTable( connectionSource, clzz );
			}
		} catch( SQLException e ) {
			Log.e( TAG, "Clear tables: " + e.getMessage() );
			return false;
		}
		return true;
	}

	private Dao<CategoryModel, Integer> getCategoriesDao() throws SQLException
	{
		if( categoriesDao == null )
		{
			categoriesDao = getDao( CategoryModel.class );
		}
		return categoriesDao;
	}
	
	private Dao<MaterialModel, Integer> getMaterialsDao() throws SQLException
	{
		if( materialsDao == null )
		{
			materialsDao = getDao( MaterialModel.class );
		}
		return materialsDao;
	}
	
	private Dao<ImageModel, Integer> getImagesDao() throws SQLException
	{
		if( imagesDao == null )
		{
			imagesDao = getDao( ImageModel.class );
		}
		return imagesDao;
	}
	
	public CreateOrUpdateStatus save( Model model ) {
		updateDate( model );
		try	{
			if( model instanceof CategoryModel ) {
				return getCategoriesDao().createOrUpdate( (CategoryModel) model );
			} else if( model instanceof MaterialModel )	{
				return getMaterialsDao().createOrUpdate( (MaterialModel) model );
			} else if( model instanceof ImageModel )	{
				return getImagesDao().createOrUpdate( (ImageModel) model );
			}
		} catch( SQLException e )	{
			Log.e( TAG, e.getMessage() );
		}
		return new CreateOrUpdateStatus( false, false, 0 );
	}
	
	private void updateDate( Model model ) {
		model.setLastChangedDate( Calendar.getInstance().getTime() );
	}

	public List<CategoryModel> queryRootCategories()
	{
		try	{			
			return getCategoriesDao().query( getCategoriesDao()
					.queryBuilder()
					.orderBy(CategoryModel.COLUMN_TITLE, true)
					.where().isNull( CategoryModel.COLUMN_SUPER_CATEGORY )
					.prepare() );
		} catch( SQLException e ) {
			return Collections.emptyList();
		}
	}
	
	public CategoryModel queryCategoryById( int id ) {
		try	{
			return getCategoriesDao().queryForId( id );
		} catch( SQLException e ) {
			return null;
		}
	}
}