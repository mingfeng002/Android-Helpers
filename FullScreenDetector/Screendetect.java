package com.pvy.batterybarpro.service;

/*************************************************************************
 *  Dummy Layout for detecting the statusbar vissibility on GB and below
 *  
 *  Created by pvyParts
 *  
 *  NOTE** May or may-not be a stupid way of doing it but hey it works....
 *  
 *  ON SDK 11 and above use OnSystemUiVisibilityChangeListener()
 *  
 *  FS_Bool True if the StatusBar is hiden eg Fullscreen App is Running
 *	
 *	OnFullScreenListener fired if layout size changes by over-riding the onLayout()
 */

 /* Usage from TEAM Battery Bar Pro
   
   			mDetector = new Screendetect(this);
			Resources res = getResources();
			WindowManager.LayoutParams paramtester = new WindowManager.LayoutParams(
					(int) res.getDimension(R.dimen.detector_stat),
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSPARENT);
			// Fix for the canot click install on sideloaded apps bug
			// Dont use fill screen, set to one side with a few pixels wide
			// 
			paramtester.gravity = Gravity.RIGHT | Gravity.TOP; 
			wm.addView(mDetector, paramtester);

			int sdkbuild = Build.VERSION.SDK_INT;
			if (sdkbuild < 11 || Settings.getBoolean("battery_bar_alt", false)){
			// my fullscreen detector.
			mDetector.setOnFullScreenListener(new OnFullScreenListener() {
				@Override
				public void fsChanged(boolean FS_Bool) {
					// TODO rethink this to be better.... bit hacky...
					if (FS_Bool) {
						Log.d("battbardetect", "FS go vissible");
						FS_vissible();
					} else if (!FS_Bool) {
						Log.d("battbardetect", "FS go hidden");
						FS_hidden();
					}
				}
			});
			} else {
				//HC or ICS detection built in FTW
				mDetector.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener(){

					@Override
					public void onSystemUiVisibilityChange(int vissible) {
						// TODO test test test
						// View Sources flags
						// SYSTEM_UI_FLAG_LOW_PROFILE - dimm
						// SYSTEM_UI_FLAG_HIDE_NAVIGATION - hide
						// SYSTEM_UI_FLAG_VISIBLE
						
						if (vissible == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION){
							// hide bar
							FS_vissible();
						} else if (vissible == View.SYSTEM_UI_FLAG_LOW_PROFILE){
							// TODO hide / dim maybe
							FS_vissible();
						} else if (vissible == View.SYSTEM_UI_FLAG_VISIBLE){
							// show bar
							FS_hidden();
						}
					}
				});
				
			}
	
*/

import com.pvy.batterybarpro.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.widget.RelativeLayout;

public class Screendetect extends RelativeLayout {

	private String FLAG = "LegacyFSDetect";
			
	private OnFullScreenListener OnFullScreenListener;

	private Context mContext;
	
	private boolean FS_Bool;

	private boolean mAttached;

	public Screendetect(Context context) {
		super(context);
		mContext = context;
	}

	public Screendetect(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}

	public Screendetect(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (!mAttached) {
			mAttached = true;
		}
	}
	
	// this does the magic
	@Override
	protected void onLayout (boolean changed, int l, int t, int r, int b){
		Log.d(FLAG, "changed - "+changed);
		// TODO this.setBackgroundColor(Color.argb(80, 0, 255, 255));
		if (changed){
			Log.d(FLAG, "screen - " + t + " x " + this.getHeight());
			 onFSChanged();
		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mAttached) {
			mAttached = false;
		}
	}
	
	public boolean getFS()
	{
		return FS_Bool;
	}
	
	// do some math... and update the listeners ect ect
	
	private void onFSChanged()
	{
		
		Log.d(FLAG, "FS changed");
		
		if(OnFullScreenListener != null){			 
				Rect dim = new Rect();
				getWindowVisibleDisplayFrame(dim);
				Log.d(FLAG, dim.bottom + " "+dim.top + " "+dim.right+ " "+dim.left);
			 if( dim.top == 0){
				Log.d(FLAG, "screen FS true - " + dim.top);
				 FS_Bool = true;
			 } else {
				Log.d(FLAG, "screen FS false  - " + dim.top);
				 FS_Bool = false;
			 }
			
			 OnFullScreenListener.fsChanged(getFS());
		}
	}

	
	public void setOnFullScreenListener(OnFullScreenListener listener)
	{
		this.OnFullScreenListener = listener;
	}
	
	public interface OnFullScreenListener
	{
		public void fsChanged(boolean FS_Bool);
	}

}


