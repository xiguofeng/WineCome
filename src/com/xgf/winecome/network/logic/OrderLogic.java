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
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xgf.winecome.entity.Order;
import com.xgf.winecome.entity.Order.OrderState;
import com.xgf.winecome.network.config.RequestUrl;

public class OrderLogic {

	public static final int NET_ERROR = 0;

	public static final int ORDER_CREATE_SUC = NET_ERROR + 1;

	public static final int ORDER_CREATE_FAIL = ORDER_CREATE_SUC + 1;

	public static final int ORDER_CREATE_EXCEPTION = ORDER_CREATE_FAIL + 1;

	public static final int ORDERLIST_GET_SUC = ORDER_CREATE_EXCEPTION + 1;

	public static final int ORDERLIST_GET_FAIL = ORDERLIST_GET_SUC + 1;

	public static final int ORDERLIST_GET_EXCEPTION = ORDERLIST_GET_FAIL + 1;

	public static void createOrder(final Context context,
			final Handler handler, final Order order) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.order.createOrder);

					JSONObject requestJson = new JSONObject();

					requestJson.put("mobilePhone",
							URLEncoder.encode(order.getPhone(), "UTF-8"));
					requestJson.put("buyAddress",
							URLEncoder.encode(order.getBuyAddress(), "UTF-8"));
					requestJson.put("latitude",
							URLEncoder.encode(order.getLatitude(), "UTF-8"));
					requestJson.put("longitude",
							URLEncoder.encode(order.getLongitude(), "UTF-8"));
					requestJson.put("orderStatus", URLEncoder.encode(
							String.valueOf(OrderState.created), "UTF-8"));
					requestJson.put("deliveryDay", URLEncoder.encode(
							order.getProbablyWaitTime(), "UTF-8"));
					requestJson.put("invoice",
							URLEncoder.encode(order.getInvoice(), "UTF-8"));
					requestJson.put("invoiceTitle",
							URLEncoder.encode(order.getInvoiceTitle(), "UTF-8"));
					requestJson.put("invoiceContent", URLEncoder.encode(
							order.getInvoiceContent(), "UTF-8"));
					requestJson.put("payType",
							URLEncoder.encode(order.getPayType(), "UTF-8"));

					rpc.addProperty("data", requestJson.toString());
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.order.createOrder, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_resultStr", resultStr);

					if (TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseCreateOrderData(obj, handler);
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

	private static void parseCreateOrderData(JSONObject response,
			Handler handler) {
		try {
			String orderID = response.getString("orderID").trim();

			if (!TextUtils.isEmpty(orderID)) {
				Message message = new Message();
				message.what = ORDER_CREATE_SUC;
				message.obj = orderID;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ORDER_CREATE_FAIL);
			}

		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_CREATE_EXCEPTION);
		}
	}

}
