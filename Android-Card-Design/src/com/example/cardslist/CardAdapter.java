package com.example.cardslist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CardAdapter extends BaseAdapter {
	
	private static final int CARD_ITEM = 0;
	private static final int CARD_HEADER = 1;

	private LayoutInflater inflater;
	private int touchDimension;
	private int cardMenuResourceId;
	private OnCardMenuItemClickListener onCardMenuItemClickListener;	
	private OnCardImageClickListener onCardImageClickListener;
	private OnSetImageStategy onSetImageStategy;
	
	private ArrayList<CardItem> data;
	private Context context;
	
	public CardAdapter( Context context ) {
		this( context, Collections.<CardItem>emptyList() );
	}
	
	public CardAdapter( Context context, List<CardItem> data ) {
		inflater = LayoutInflater.from( context );
		touchDimension = (int) context.getResources().getDimension(R.dimen.card_padding);
		this.data = new ArrayList<CardItem>( data );
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void remove( int position ) {
		data.remove(position);
		notifyDataSetChanged();
	}
	
	public void remove( CardItem item ) {
		data.remove(item);
		notifyDataSetChanged();
	}
	
	public void insert( int position, CardItem item ) {
		data.add(position, item);
		notifyDataSetChanged();
	}
	
	public void insert( CardItem item ) {
		data.add(item);
		notifyDataSetChanged();
	}
	
	public void insert( List<CardItem> items ) {
		data.addAll(items);
		notifyDataSetChanged();
	}
	
	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
		
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public boolean isEnabled(int position) {
		CardItem item = getCardItem(position);
		return item.isHeader() ? ( ( CardHeaderItem ) item ).hasAction() : true;
	}
	
	@Override
	public int getItemViewType(int position) {
		return getCardItem(position).isHeader() ? CARD_HEADER : CARD_ITEM;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( getItemViewType(position) == CARD_ITEM ) {
			return getContentView(position, convertView, parent);
		} else {
			return getHeaderView(position, convertView, parent);
		}
	}
	
	public class HeaderViewHolder {
		TextView title;
		TextView action;
	}
	
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder header;
		if(convertView == null) {
			convertView = inflater.inflate( R.layout.card_list_item_header, parent, false );
			header = new HeaderViewHolder();
			header.title = (TextView) convertView.findViewById( R.id.amountText );
			header.action = (TextView) convertView.findViewById( R.id.textView2 );
			convertView.setTag( header );
		} else {
			header = (HeaderViewHolder) convertView.getTag();
		}
		
		CardHeaderItem item = (CardHeaderItem) getCardItem(position);
		header.title.setText( item.getHeaderTitle() );
		if( item.hasAction() ) {
			header.action.setText( item.getActionTitle() );
			header.action.setVisibility( View.VISIBLE );
		} else {
			header.action.setVisibility( View.GONE );
		}		
		return convertView;
	}
	
	public class ViewHolder {
		View content;
		TextView text;
		TextView secondLine;
		ImageView image;
		ImageButton button;
	}
	
	public View getContentView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if( convertView == null ) {
			convertView = inflater.inflate( R.layout.card_list_item, parent, false );
			holder = new ViewHolder();
			holder.content = convertView.findViewById( R.id.content );
			holder.text = (TextView) convertView.findViewById( R.id.text1 );
			holder.image = (ImageView) convertView.findViewById( R.id.image );
			holder.button = (ImageButton) convertView.findViewById( R.id.button );
			convertView.setTag( holder );
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		CardContentItem item = (CardContentItem) getCardItem(position);
		holder.text.setText( item.getContent() );
		
		TouchUtils.increaseTouchArea(holder.content, holder.button, touchDimension, touchDimension, touchDimension, touchDimension);
			
		if( hasSetImageStrategy() ) {
			onSetImageStategy.onSetImageView( item, holder.image, position );
		} else {
			holder.image.setImageBitmap( BitmapFactory.decodeResource( getContext().getResources(), ( (CardContentItem) item ).getImageResourceId(), null ) );
		}
		
		if( hasOnCardImageClickListener() ) {
			holder.image.setOnClickListener( createImageClickListener( item, position ) );
		}
		
		if( hasCardPopupMenu() ) {
			holder.button.setOnClickListener( createPopupMenuClickListener( item, position ) );
		} else {
			holder.button.setVisibility( View.GONE );
		}
		return convertView;
	}
	
	private OnClickListener createImageClickListener( final CardItem item, final int position) {
		return new OnClickListener() {
			@Override
			public void onClick(View view) {
				onCardImageClickListener.onCardImageClick(item, view, position);
			}			
		};
	}

	private OnClickListener createPopupMenuClickListener( final CardItem cardItem, final int position) {
		return new OnClickListener() {
			@Override
			public void onClick(View view) {
				PopupMenu menu = new PopupMenu( getContext(), view );
				menu.getMenuInflater().inflate( cardMenuResourceId , menu.getMenu() );
				menu.setOnMenuItemClickListener( new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						return onCardMenuItemClickListener.onCardMenuItemClick(cardItem, menuItem, position);
					}
				} );
				menu.show();
			}			
		};
	}
	
	private boolean hasCardPopupMenu() {
		return cardMenuResourceId > 0 && onCardMenuItemClickListener != null;
	}
	
	public void setCardPopupMenu( final int popupMenuResourceId, final OnCardMenuItemClickListener menuListener ) {
		this.cardMenuResourceId = popupMenuResourceId;
		this.onCardMenuItemClickListener = menuListener;
	}
	
	private boolean hasOnCardImageClickListener() {
		return onCardImageClickListener != null;
	}
	
	public void setOnCardImageClickListener( final OnCardImageClickListener onCardImageClickListener) {
		this.onCardImageClickListener = onCardImageClickListener;
	}
	
	private boolean hasSetImageStrategy() {
		return onSetImageStategy != null;
	}
	
	public void setOnSetImageStategy(OnSetImageStategy onSetImageStategy) {
		this.onSetImageStategy = onSetImageStategy;
	}
	
	// Interfaces
	
	public interface OnCardMenuItemClickListener{		
		public boolean onCardMenuItemClick( CardItem cardItem, MenuItem menuItem, int position );
	}
	
	public interface OnCardImageClickListener{		
		public void onCardImageClick( CardItem cardItem, View view, int position );
	}
	
	public interface OnSetImageStategy{		
		public void onSetImageView( CardItem cardItem, ImageView view, int position );
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}
	
	public CardItem getCardItem(int position) {
		return (CardItem) getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
