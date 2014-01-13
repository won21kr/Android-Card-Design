package com.example.cardslist;

import it.gmariotti.android.example.colorpicker.calendarstock.ColorPickerDialog;
import it.gmariotti.android.example.colorpicker.calendarstock.ColorPickerSwatch;
import it.gmariotti.android.example.colorpicker.calendarstock.Utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cardslist.database.CategoryModel;
import com.example.cardslist.database.DatabaseGate;
import com.example.cardslist.loader.ObjectLoader;

import de.timroes.android.listview.EnhancedListView;
import de.timroes.android.listview.EnhancedListView.OnDismissCallback;
import de.timroes.android.listview.EnhancedListView.SwipeDirection;
import de.timroes.android.listview.EnhancedListView.UndoStyle;
import de.timroes.android.listview.EnhancedListView.Undoable;

public class CardListFragment extends Fragment implements LoaderCallbacks<List<CardItem>> {
	private static int CATEGORY_LOADER = 1;
	
	private EnhancedListView listView;
	private CardAdapter adapter;
	private ImageGate imageGate;
	private DatabaseGate dbGate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_card_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initView();
		initAdapter();
		
		listView.setAdapter( adapter );
		
		addSwipeDeletion();
		addPopUpMenu();
		addImage();
		loadData();
	}
	
	private void loadData() {
		getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
	}

	@Override
	public void onAttach(Activity activity) {		
		if( activity instanceof DatabaseGate ) {
			dbGate = (DatabaseGate) activity ;
		} else {
			throw new IllegalArgumentException( "Activity musst implements DatabaseGate." );
		}
		
		if( activity instanceof ImageGate ) {
			imageGate = (ImageGate) activity ;
		} else {
			throw new IllegalArgumentException( "Activity musst implements ImageGate." );
		}
		
		super.onAttach(activity);
	}
	
	@Override
	public void onDestroy() {		
		dbGate = null;
		imageGate = null;
		listView = null;
		adapter = null;
		
		super.onDestroy();
	}

	private void initView() {
		listView = (EnhancedListView) getView().findViewById(R.id.cardListView);
	}
	
	private void initAdapter() {
		adapter = new CardAdapter( getActivity() );
	}
	
	private void addPopUpMenu() {
		adapter.setCardPopupMenu( R.menu.card_popup_menu, new CardAdapter.OnCardMenuItemClickListener() {
			@Override
			public boolean onCardMenuItemClick(CardItem cardItem, MenuItem menuItem, int position) {
				switch( menuItem.getItemId() ) {
					case R.id.action_toast:
						Toast.makeText( getActivity(), "CardItem: " + cardItem, Toast.LENGTH_SHORT ).show();
						return true;	
					default:
						return false;
				}
			}
		} );
	}
	
	private void addImage() {
		adapter.setOnCardImageClickListener( new CardAdapter.OnCardImageClickListener() {			
			@Override
			public void onCardImageClick( final CardItem cardItem, final View view, final int position ) {
				// dialog settings
				int dialogColumns = 4;
				int dialogTitle = R.string.color_picker_default_title;
				int dialogSelectedColor = getActualColor( cardItem );
				int[] dialogColors = Utils.ColorUtils.colorChoice( getActivity() );
				int dialogMode = Utils.isTablet( getActivity() ) ? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL;
				
				// dialog creation
				final ColorPickerDialog colorDialog = ColorPickerDialog.newInstance(
			              dialogTitle, 
			              dialogColors,
			              dialogSelectedColor,
			              dialogColumns,
			              dialogMode );
				
				// dialog callback
				colorDialog.setOnColorSelectedListener( new ColorPickerSwatch.OnColorSelectedListener(){
	                @Override
	                public void onColorSelected(int color) {
	                	( ( CardContentItem ) cardItem ).setImageResourceId( color );
	                	adapter.notifyDataSetChanged();
	                }
				});
				
				// show dialog
				colorDialog.show( getFragmentManager(), "colorPicker" );
			}
		} );
		
		// Add special image strategy to use the image caching mechanisms.
		adapter.setOnSetImageStategy( new CardAdapter.OnSetImageStategy() {
			@Override
			public void onSetImageView(CardItem cardItem, ImageView imageView, int position) {
				if( !cardItem.isHeader() ) {
					imageGate.getImageFetcher().loadImage( ( (CardContentItem) cardItem ).getImageResourceId(), imageView);
				}
			}
		} );
	}
	
	private void addSwipeDeletion() {
		listView.setUndoStyle( UndoStyle.COLLAPSED_POPUP );
		
		listView.setSwipeDirection( SwipeDirection.BOTH );
		listView.setDismissCallback( new OnDismissCallback() {			
			@Override
			public Undoable onDismiss( final EnhancedListView listView, final int position) {
				final CardItem item =  (CardItem) adapter.getItem(position);
                adapter.remove( position );
                return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                        adapter.insert(position, item);
                    }
                };
			}
		} );
		listView.enableSwipeToDismiss();
	}
	
	protected int getActualColor( CardItem cardItem ) {
		CardContentItem item = ( CardContentItem ) cardItem;
		try {
			return getResources().getColor( item.getImageResourceId() );
		} catch( Resources.NotFoundException e ) {
			// it's a direct color code
			return item.getImageResourceId();
		}
		
	}
	
	@Override
    public void onStop() {
		// clear undo history
        if(listView != null) {
        	listView.discardUndo();
        }
        super.onStop();
    }

	@Override
	public Loader<List<CardItem>> onCreateLoader(int id, Bundle arguments) {
		if( id == CATEGORY_LOADER) {
			return new ObjectLoader<List<CardItem>>( getActivity() ) {
				@Override
				public List<CardItem> loadInBackground() {					
					List<CardItem> result = new ArrayList<CardItem>();
					for( CategoryModel model : dbGate.getDatabaseHelper().queryRootCategories() ) {
						result.add( new CardContentItem( model.getTitle(), R.color.holo_blue_light ) );
					}
					return result;
				}								
			};
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<List<CardItem>> loader, List<CardItem> result) {
		adapter.insert(result);
	}

	@Override
	public void onLoaderReset( Loader<List<CardItem>> loader ) {
		adapter.clear();
	}
}
