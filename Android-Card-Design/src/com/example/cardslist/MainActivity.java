package com.example.cardslist;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.android.imagefetcher.ImageCache.ImageCacheParams;
import com.android.imagefetcher.ImageFetcher;
import com.example.cardslist.database.CategoryModel;
import com.example.cardslist.database.DatabaseGate;
import com.example.cardslist.database.DatabaseHelper;
import com.example.cardslist.database.ImageModel;
import com.example.cardslist.database.MaterialModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.wackernagel.android.dynamicviewpager.adapter.DynamicTabFragmentPagerAdapter;
import de.wackernagel.android.dynamicviewpager.view.LockableViewPager;

public class MainActivity extends ActionBarActivity implements DatabaseGate, ImageGate {
	private DatabaseHelper databaseHelper;
	private ImageFetcher imageFetcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getDatabaseHelper().clearTables();
		createSampleData();
		
		initImageCaching();
		
		LockableViewPager pager = (LockableViewPager) findViewById( R.id.pager );
		pager.lockSwiping();
		DynamicTabFragmentPagerAdapter adapter = new DynamicTabFragmentPagerAdapter(this, getSupportFragmentManager(), pager, getSupportActionBar() );
		adapter.addPage( "Categories", CardListFragment.class );	
	}

	private void createSampleData() {
		CategoryModel superCategory = new CategoryModel( "Dresden" );
		getDatabaseHelper().save(superCategory);
		
		CategoryModel subCategory = new CategoryModel( "Striesen", superCategory );
		getDatabaseHelper().save(subCategory);
		
		ImageModel blueImage = new ImageModel();
		blueImage.setResourceId( R.color.holo_blue_light );
		getDatabaseHelper().save(blueImage);
		
		ImageModel orangeImage = new ImageModel();
		orangeImage.setResourceId( R.color.holo_orange_light );
		getDatabaseHelper().save(orangeImage);
		
		MaterialModel firstMaterial = new MaterialModel( "Fuchsstraﬂe", superCategory );
		firstMaterial.setImage(blueImage);
		getDatabaseHelper().save(firstMaterial);
		
		MaterialModel secondeMaterial = new MaterialModel( "Augsburgerstraﬂe", subCategory );
		secondeMaterial.setImage(orangeImage);
		getDatabaseHelper().save(secondeMaterial);
	}

	@Override
	public DatabaseHelper getDatabaseHelper() {
		if( databaseHelper == null ) {
			databaseHelper = OpenHelperManager.getHelper( this, DatabaseHelper.class );
		}
		return databaseHelper;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if( databaseHelper != null ) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	@Override
	public ImageFetcher getImageFetcher() {
		return imageFetcher;
	}
	
	private void initImageCaching() {
		final int imageThumbSize = getResources().getDimensionPixelSize( R.dimen.image_size );

		final ImageCacheParams cacheParams = new ImageCacheParams( this, "thumbs" );
		cacheParams.setMemCacheSizePercent( 0.25f );
		cacheParams.setCompressQuality( 100 );

		imageFetcher = new ImageFetcher( this, imageThumbSize);
		imageFetcher.setImageFadeIn( true );
		imageFetcher.addImageCache( getSupportFragmentManager(), cacheParams );
	}
}
