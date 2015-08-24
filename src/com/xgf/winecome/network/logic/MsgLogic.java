package com.xgf.winecome.network.logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xgf.winecome.entity.NotifyMsg;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;
import com.xgf.winecome.utils.JsonUtils;

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

	// {"datas":{"total":1,"list":[{"content":"尊贵的客户,您的订单已被接单","id":"142","phone":"17712888306","readTime":"","status":"1","msgId":"142","msgTime":"2015-08-24 14:40:50.0","msgType":"1","orderId":"NO.DD201508000474"}]},"message":"操作成功","result":"0"}
	// {"datas":{"total":1,"list":[{"content":"尊贵的客户,您已下单成功!","id":"138","phone":"17712888306","readTime":"","status":"1","msgId":"138","msgTime":"2015-08-24 14:34:46.0","msgType":"1","orderId":"NO.DD201508000473"}]},"message":"操作成功","result":"0"}
	// {"datas":{"total":0},"message":"操作成功","result":"0"}

	private static void parsePushMsgData(JSONObject response, Handler handler) {
		try {
			Log.e("xxx_msg_push_resultStr", response.toString());
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
				String total = jsonObject.getString("total").trim();
				ArrayList<NotifyMsg> tempNotifyMsgList = new ArrayList<NotifyMsg>();
				if (!TextUtils.isEmpty(total) && Integer.parseInt(total) > 0) {
					JSONArray notifyMsgArray = jsonObject
							.getJSONArray(MsgResult.RESULT_LIST_TAG);
					for (int i = 0; i < notifyMsgArray.length(); i++) {
						JSONObject msgJsonObject = notifyMsgArray
								.getJSONObject(i);
						NotifyMsg notifyMsg = (NotifyMsg) JsonUtils
								.fromJsonToJava(msgJsonObject,
										NotifyMsg.class);
						tempNotifyMsgList.add(notifyMsg);
					}
				}
				if (tempNotifyMsgList.size() == 0) {
					tempNotifyMsgList = null;
				}
				Message message = new Message();
				message.what = MSG_GET_SUC;
				message.obj = tempNotifyMsgList;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(MSG_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(MSG_GET_EXCEPTION);
		}
	}

}
