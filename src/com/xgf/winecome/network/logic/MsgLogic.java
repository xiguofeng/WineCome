package com.xgf.winecome.network.logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;

public class MsgLogic {

	public static final int NET_ERROR = 0;

	public static final int MSG_GET_SUC = NET_ERROR + 1;

	public static final int MSG_GET_FAIL = MSG_GET_SUC + 1;

	public static final int MSG_GET_EXCEPTION = MSG_GET_FAIL + 1;

	public static void getPushMsg(final Context context, final Handler handler,
			final String phone) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.msg.pushMsg);

					rpc.addProperty("phone", URLEncoder.encode(phone, "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/" + RequestUrl.msg.pushMsg,
							envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_msg_push_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parsePushMsgData(obj, handler);
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private static void parsePushMsgData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(MSG_GET_SUC);
			} else {
				handler.sendEmptyMessage(MSG_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(MSG_GET_EXCEPTION);
		}
	}

}
