package com.example.cardslist.loader;

import java.util.List;

import android.content.Context;

import com.example.cardslist.database.DatabaseGate;
import com.example.cardslist.database.Model;

public abstract class ModelLoader<T extends Model> extends ObjectLoader<List<T>> {
	private DatabaseGate databaseGate;
	
	public ModelLoader(Context context, DatabaseGate databaseGate) {
		super(context);
		this.databaseGate = databaseGate;
	}

	@Override
	public List<T> loadInBackground() {
		return loadInBackground(databaseGate);
	}
	
	public abstract List<T> loadInBackground( DatabaseGate databaseGate );
	
	protected List<T> sortResult( List<T> result ) {
		return result;
	}
}