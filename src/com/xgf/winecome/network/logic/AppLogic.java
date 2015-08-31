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

import com.xgf.winecome.entity.User;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class AppLogic {

	public static final int NET_ERROR = 0;

	public static final int GET_VERSION_SUC = NET_ERROR + 1;

	public static final int GET_VERSION_FAIL = GET_VERSION_SUC + 1;

	public static final int GET_VERSION_EXCEPTION = GET_VERSION_FAIL + 1;

	public static void getVersion(final Context context, final Handler handler,
			final String versionNo) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.app.getVersion);

					rpc.addProperty("versionNo", URLEncoder.encode(versionNo, "UTF-8"));
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.app.getVersion, envelope);

					Log.e("xxx_sop", rpc.toString());

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_GetVersionData_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseGetVersionData(obj, handler);
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

	private static void parseGetVersionData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(GET_VERSION_SUC);
			} else {
				handler.sendEmptyMessage(GET_VERSION_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(GET_VERSION_EXCEPTION);
		}
	}

}
