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

import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.Order;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;
import com.xgf.winecome.utils.OrderManager;

public class OrderLogic {

	public static final int NET_ERROR = 0;

	public static final int ORDER_CREATE_SUC = NET_ERROR + 1;

	public static final int ORDER_CREATE_FAIL = ORDER_CREATE_SUC + 1;

	public static final int ORDER_CREATE_EXCEPTION = ORDER_CREATE_FAIL + 1;

	public static final int ORDERLIST_GET_SUC = ORDER_CREATE_EXCEPTION + 1;

	public static final int ORDERLIST_GET_FAIL = ORDERLIST_GET_SUC + 1;

	public static final int ORDERLIST_GET_EXCEPTION = ORDERLIST_GET_FAIL + 1;

	public static void createOrder(final Context context,
			final Handler handler, final Order order,
			final ArrayList<Goods> goodsList) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.order.createOrder);

					JSONObject requestJson = new JSONObject();

					requestJson.put("phone",
							URLEncoder.encode(order.getPhone(), "UTF-8"));
					requestJson.put("address",
							URLEncoder.encode(order.getBuyAddress(), "UTF-8"));
					requestJson.put("latitude",
							URLEncoder.encode(order.getLatitude(), "UTF-8"));
					requestJson.put("longitude",
							URLEncoder.encode(order.getLongitude(), "UTF-8"));
					requestJson.put("deliveryTime", "30");
					requestJson.put("invoice",
							URLEncoder.encode(order.getInvoice(), "UTF-8"));
					requestJson.put("invoiceTitle",
							URLEncoder.encode(order.getInvoiceTitle(), "UTF-8"));
					requestJson.put("invoiceContent", URLEncoder.encode(
							order.getInvoiceContent(), "UTF-8"));
					requestJson.put("payType",
							URLEncoder.encode(order.getPayType(), "UTF-8"));

					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < goodsList.size(); i++) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("productId", goodsList.get(i).getId());
						jsonObject.put("count", goodsList.get(i).getNum());
						jsonArray.put(jsonObject);
					}

					requestJson.put("items", jsonArray);

					Log.e("xxx_order_json", requestJson.toString());

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

					if (!TextUtils.isEmpty(resultStr)) {

						Log.e("xxx_order_result", resultStr.toString());
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

	// {"message":"操作成功","datas":"{}","result":"0","orderId":"NO.DD2015080032"}
	// {"datas":{"orderId":"NO.DD2015080003"},"message":"操作成功","result":"0"}}
	private static void parseCreateOrderData(JSONObject response,
			Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_FAIL)) {
				handler.sendEmptyMessage(ORDER_CREATE_FAIL);
			} else {
				String orderID = response.getString("orderId").trim();
				if (!TextUtils.isEmpty(orderID)) {
					OrderManager.setsCurrentOrderId(orderID);
					Message message = new Message();
					message.what = ORDER_CREATE_SUC;
					message.obj = orderID;
					handler.sendMessage(message);
				} else {
					handler.sendEmptyMessage(ORDER_CREATE_FAIL);
				}
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_CREATE_EXCEPTION);
		}
	}

}
