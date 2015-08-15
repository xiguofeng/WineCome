

package com.xgf.winecome.ui.view.materialmenu;

import com.xgf.winecome.ui.view.materialmenu.MaterialMenuDrawable.Stroke;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

/**
 * A helper class for implementing {@link MaterialMenuDrawable} as an
 * {@link android.app.ActionBar} icon.
 * <p/>
 * In order to preserve default ActionBar icon click state call
 * {@link MaterialMenuBase#setNeverDrawTouch(boolean)}. Otherwise, adjust your
 * theme to disable pressed background color by setting
 * <code>android:actionBarItemBackground</code> to null and use
 * <code>android:actionButtonStyle</code>,
 * <code>android:actionOverflowButtonStyle</code> to enable other menu icon
 * backgrounds.
 * <p/>
 * Disables ActionBar Up arrow and replaces default drawable using
 * {@link ActionBar#setIcon(android.graphics.drawable.Drawable)}
 */
public class MaterialMenuIcon extends MaterialMenuBase {

	public MaterialMenuIcon(Activity activity, int color, Stroke stroke) {
		super(activity, color, stroke);
	}

	public MaterialMenuIcon(Activity activity, int color, Stroke stroke, int transformDuration) {
		super(activity, color, stroke, transformDuration);
	}

	public MaterialMenuIcon(Activity activity, int color, Stroke stroke, int transformDuration, int pressedDuration) {
		super(activity, color, stroke, transformDuration, pressedDuration);
	}

	@Override
	protected View getActionBarHomeView(Activity activity) {
		Resources resources = activity.getResources();
		return activity.getWindow().getDecorView().findViewById(resources.getIdentifier("android:id/home", null, null));
	}

	@Override
	protected View getActionBarUpView(Activity activity) {
		Resources resources = activity.getResources();
		ViewGroup actionBarView = (ViewGroup) activity.getWindow().getDecorView().findViewById(resources.getIdentifier("android:id/action_bar", null, null));
		View homeView = actionBarView.getChildAt(actionBarView.getChildCount() > 1 ? 1 : 0);
		return homeView.findViewById(resources.getIdentifier("android:id/up", null, null));
	}

	@Override
	protected boolean providesActionBar() {
		return true;
	}

	@Override
	@TargetApi(14)
	protected void setActionBarSettings(Activity activity) {
		ActionBar actionBar = activity.getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setIcon(getDrawable());
	}
}
