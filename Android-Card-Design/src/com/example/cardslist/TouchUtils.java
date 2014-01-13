package com.example.cardslist;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

public class TouchUtils {

	/**
	 * Increase the touch area of child view in its parent view with the given dimensions. 
	 * 
	 * @param parentView 
	 * 		container of child view
	 * @param childView 
	 * 		child view to increase touch area 
	 * @param left
	 * 		to increasing left size
	 * @param top
	 * 		to increasing top size
	 * @param right
	 * 		to increasing right size
	 * @param bottom
	 * 		to increasing bottom size
	 */
	public static void increaseTouchArea(final View parentView,
			final View childView, final int left, final int top,
			final int right, final int bottom) {
		parentView.post(new Runnable() {
			@Override
			public void run() {
				Rect delegateArea = new Rect();
				childView.getHitRect(delegateArea);

				delegateArea.left += left;
				delegateArea.top += top;
				delegateArea.right += right;
				delegateArea.bottom += bottom;

				TouchDelegate touchDelegate = new TouchDelegate(delegateArea,
						childView);

				if (childView.getParent() instanceof View) {
					((View) childView.getParent())
							.setTouchDelegate(touchDelegate);
				}
			}
		});
	}

}
