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

import com.xgf.winecome.network.HttpUtils;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;

public class CommentLogic {
	public static final int NET_ERROR = 0;

	public static final int COMMENT_ADD_SUC = NET_ERROR + 1;

	public static final int COMMENT_ADD_FAIL = COMMENT_ADD_SUC + 1;

	public static final int COMMENT_ADD_EXCEPTION = COMMENT_ADD_FAIL + 1;

	public static void addComment(final Context context, final Handler handler,
			final String orderId, final String comment) {
		if (HttpUtils.checkNetWorkInfo(context)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
								RequestUrl.comment.commentOrder);

						rpc.addProperty("orderId",
								URLEncoder.encode(orderId, "UTF-8"));
						rpc.addProperty("comment",
								URLEncoder.encode(comment, "UTF-8"));
						rpc.addProperty("md5",
								URLEncoder.encode("1111", "UTF-8"));

						AndroidHttpTransport ht = new AndroidHttpTransport(
								RequestUrl.HOST_URL);

						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
								SoapEnvelope.VER11);

						envelope.bodyOut = rpc;
						envelope.dotNet = true;
						envelope.setOutputSoapObject(rpc);

						ht.call(RequestUrl.NAMESPACE + "/"
								+ RequestUrl.comment.commentOrder, envelope);

						SoapObject so = (SoapObject) envelope.bodyIn;

						String resultStr = (String) so.getProperty(0);
						Log.e("xxx_commentOrder_resultStr", resultStr);

						if (TextUtils.isEmpty(resultStr)) {
							JSONObject obj = new JSONObject(resultStr);
							parseData(obj, handler);
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
		} else {
			handler.sendEmptyMessage(NET_ERROR);
		}
	}

	private static void parseData(JSONObject result, Handler handler) {
		try {

			if (result.getString(MsgResult.RESULT_TAG).equals(
					MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(COMMENT_ADD_SUC);
			} else {
				handler.sendEmptyMessage(COMMENT_ADD_FAIL);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(COMMENT_ADD_EXCEPTION);
		}
	}

}
