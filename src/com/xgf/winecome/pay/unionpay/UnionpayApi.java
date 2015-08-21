package com.xgf.winecome.pay.unionpay;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class UnionpayApi {

	public static final int PLUGIN_VALID = 0;
	public static final int PLUGIN_NOT_INSTALLED = -1;
	public static final int PLUGIN_NEED_UPGRADE = 2;

	public static final int SERIAL_NUMBER_GET_SUC = 0;
	public static final int SERIAL_NUMBER_GET_FAIL = 1;
	public static final int SERIAL_NUMBER_GET_EXCEPTION = 2;

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private final String mMode = "01";
	private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";

	private Activity mActivity;

	private Handler mHandler;

	public UnionpayApi() {

	}

	public UnionpayApi(Activity activity, Handler handler) {

	}

	/*************************************************
	 * 步骤1：从网络开始,获取交易流水号即TN
	 ************************************************/
	public void getTn(final Activity activity, final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				String tn = null;
				InputStream is;
				try {
					String url = TN_URL_01;

					URL myURL = new URL(url);
					URLConnection ucon = myURL.openConnection();
					ucon.setConnectTimeout(120000);
					is = ucon.getInputStream();
					int i = -1;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((i = is.read()) != -1) {
						baos.write(i);
					}

					tn = baos.toString();
					is.close();
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				Message message = new Message();
				message.what = SERIAL_NUMBER_GET_SUC;
				message.obj = tn;
				handler.sendMessage(message);
			}
		}).start();

	}

	/*************************************************
	 * 步骤2：通过银联工具类启动支付插件
	 ************************************************/
	public void pay(Activity activity, Handler handler, String tn) {
		UPPayAssistEx.startPayByJAR(activity, PayActivity.class, null, null,
				tn, mMode);
	}

}
