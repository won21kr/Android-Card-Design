package com.example.cardslist.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class ObjectLoader<T> extends AsyncTaskLoader<T> {
	private T result;

	private T getResult() {
		return result;
	}

	private void setResult(T result) {
		this.result = result;
	}

	private boolean hasResult() {
		return (result != null);
	}

	public ObjectLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(T data) {
		if (isReset()) {
			if (hasResult()) {
				onReleaseResources(data);
			}
			return;
		}

		T oldData = data;
		setResult(data);

		if (isStarted()) {
			super.deliverResult(data);
		}

		if (oldData != null) {
			onReleaseResources(oldData);
		}
	}

	@Override
	protected void onStartLoading() {
		if (hasResult()) {
			deliverResult(getResult());
			return;
		}

		if (takeContentChanged() || !hasResult()) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(T data) {
		super.onCanceled(data);
		onReleaseResources(data);
	}

	@Override
	protected void onReset() {
		onStopLoading();

		if (hasResult()) {
			onReleaseResources(result);
			setResult(null);
		}
	}

	protected void onReleaseResources(T data) {
	}
}