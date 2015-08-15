
package com.xgf.winecome.ui.view;

import com.xgf.winecome.R;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CustomClipLoading extends FrameLayout {

	private static final int MAX_PROGRESS = 10000;
	private ClipDrawable mClipDrawable;
	private int mProgress = 0;
	private boolean running;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 如果消息是本程序发送的
			if (msg.what == 0x123) {
				mClipDrawable.setLevel(mProgress);
			}
		}
	};

	
	public CustomClipLoading(Context context) {
		this(context, null, 0);
	}

	public CustomClipLoading(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomClipLoading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.custom_loading, null);
		addView(view);
		ImageView imageView = (ImageView) findViewById(R.id.iv_progress);
		mClipDrawable = (ClipDrawable) imageView.getDrawable();
		
		Thread s = new Thread(r);
		s.start();
	}

	public void stop() {
		mProgress = 0;
		running = false;
	}

	Runnable r = new Runnable() {
		@Override
		public void run() {
			running = true;
			while (running) {
				handler.sendEmptyMessage(0x123);
				if (mProgress > MAX_PROGRESS) {
					mProgress = 0;
				}
				mProgress += 100;
				try {
					Thread.sleep(18);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
}
