package com.xgf.winecome.ui.view;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xgf.winecome.R;

public class CustomHourglassClipLoading extends FrameLayout {

	private static final int MAX_PROGRESS = 10000;
	private ClipDrawable mTopClipDrawable;
	private ClipDrawable mBottomClipDrawable;
	private int mProgress = 0;
	private boolean running;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 如果消息是本程序发送的
			if (msg.what == 0x123) {
				mTopClipDrawable.setLevel(MAX_PROGRESS - mProgress);
				mBottomClipDrawable.setLevel(mProgress);
				//mBottomLineClipDrawable.setLevel(MAX_PROGRESS - mProgress);
			}
		}
	};

	public CustomHourglassClipLoading(Context context) {
		this(context, null, 0);
	}

	public CustomHourglassClipLoading(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomHourglassClipLoading(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.hourglass_loading, null);
		addView(view);
		ImageView imageView = (ImageView) findViewById(R.id.iv_progress_top);
		mTopClipDrawable = (ClipDrawable) imageView.getDrawable();

		ImageView imageView2 = (ImageView) findViewById(R.id.iv_progress_bottom);
		mBottomClipDrawable = (ClipDrawable) imageView2.getDrawable();

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
				mProgress += 1;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
}
