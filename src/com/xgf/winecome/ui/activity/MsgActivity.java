package com.xgf.winecome.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.MessageItem;
import com.xgf.winecome.network.logic.PromotionLogic;
import com.xgf.winecome.network.logic.UserLogic;
import com.xgf.winecome.ui.adapter.MsgAdapter;
import com.xgf.winecome.ui.view.CustomProgressDialog2;
import com.xgf.winecome.utils.ActivitiyInfoManager;

public class MsgActivity extends Activity implements OnClickListener {

	public static final int SUC = 0;

	public static final int FAIL = SUC + 1;

	private final Context mContext = MsgActivity.this;

	private ImageView mBackIv;

	private ListView mMsgLv;
	private MsgAdapter mMsgAdapter;
	private ArrayList<MessageItem> mMsgList = new ArrayList<MessageItem>();

	protected CustomProgressDialog2 mCustomProgressDialog;
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.MSG_LIST_GET_SUC: {
				if(null!=msg.obj){
					mMsgList.clear();
					mMsgList.addAll((Collection<? extends MessageItem>) msg.obj);
					mMsgAdapter.notifyDataSetChanged();
				}
				break;

			}
			case UserLogic.MSG_LIST_GET_FAIL: {
				break;
			}
			case UserLogic.MSG_LIST_GET_EXCEPTION: {
				break;
			}
			default:
				break;
			}
			if (null != mCustomProgressDialog) {
				mCustomProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg);
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		if (!ActivitiyInfoManager.activitityMap.containsKey(ActivitiyInfoManager.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap.put(ActivitiyInfoManager.getCurrentActivityName(mContext), this);
		}
		initView();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void initView() {
		mBackIv = (ImageView) findViewById(R.id.msg_back_iv);
		mBackIv.setOnClickListener(this);

		mMsgLv = (ListView) findViewById(R.id.msg_lv);
		mMsgAdapter = new MsgAdapter(mContext, mMsgList);
		mMsgLv.setAdapter(mMsgAdapter);

		mMsgLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO
				// Intent intent = new Intent(mContext, Html5Activity.class);
				// intent.putExtra("url", mMsgList.get(position).getUrl());
				// mContext.startActivity(intent);
			}
		});
	}

	private void initData() {
		mCustomProgressDialog.show();
		UserLogic.queryMessage(mContext, mHandler,"17712888306", "1", "15");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
