package com.xgf.winecome.network.logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.xgf.winecome.utils.JsonUtils;
import com.xgf.winecome.utils.OrderManager;

public class OrderLogic {

	public static final int NET_ERROR = 0;

	public static final int ORDER_CREATE_SUC = NET_ERROR + 1;

	public static final int ORDER_CREATE_FAIL = ORDER_CREATE_SUC + 1;

	public static final int ORDER_CREATE_EXCEPTION = ORDER_CREATE_FAIL + 1;

	public static final int ORDERLIST_GET_SUC = ORDER_CREATE_EXCEPTION + 1;

	public static final int ORDERLIST_GET_FAIL = ORDERLIST_GET_SUC + 1;

	public static final int ORDERLIST_GET_EXCEPTION = ORDERLIST_GET_FAIL + 1;

	public static final int ORDER_CANCEL_SUC = ORDERLIST_GET_EXCEPTION + 1;

	public static final int ORDER_CANCEL_FAIL = ORDER_CANCEL_SUC + 1;

	public static final int ORDER_CANCEL_EXCEPTION = ORDER_CANCEL_FAIL + 1;

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
							URLEncoder.encode(order.getAddress(), "UTF-8"));
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

	public static void getOrders(final Context context, final Handler handler,
			final String phone, final String pageNum, final String pageSize) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.order.queryOrders);

					rpc.addProperty("phone", URLEncoder.encode(phone, "UTF-8"));
					rpc.addProperty("pageNum",
							URLEncoder.encode(pageNum, "UTF-8"));
					rpc.addProperty("pageSize",
							URLEncoder.encode(pageSize, "UTF-8"));
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.order.queryOrders, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);

					if (!TextUtils.isEmpty(resultStr)) {

						Log.e("xxx_orders_result", resultStr.toString());
						JSONObject obj = new JSONObject(resultStr);
						parseOrdersData(obj, handler);
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

	// {"datas":{"total":"4","list":[{"phone":"1002","orderTime":"2015-08-06 16:35:26","orderStatus":"1","id":"","amount":"240","deliveryTime":"2030-00-00 00:00:00","address":"1234","payStatus":"","memo":"","items":[{"productId":"10002","productName":"海之蓝","salePrice":"120","count":"2"}]},{"phone":"1002","orderTime":"2015-08-06 16:35:47","orderStatus":"1","id":"","amount":"240","deliveryTime":"2030-00-00 00:00:00","address":"1234","payStatus":"","memo":"","items":[{"productId":"10002","productName":"海之蓝","salePrice":"120","count":"2"}]},{"phone":"1002","orderTime":"2015-08-06 16:44:35","orderStatus":"1","id":"","amount":"628","deliveryTime":"2030-00-00 00:00:00","address":"1234","payStatus":"","memo":"","items":[{"productId":"10002","productName":"海之蓝","salePrice":"120","count":"2"},{"productId":"2222","productName":"梦之蓝9","salePrice":"388","count":"1"}]},{"phone":"1002","orderTime":"2015-08-06 16:44:48","orderStatus":"1","id":"","amount":"628","deliveryTime":"2030-00-00 00:00:00","address":"1234","payStatus":"","memo":"","items":[{"productId":"10002","productName":"海之蓝","salePrice":"120","count":"2"},{"productId":"2222","productName":"梦之蓝9","salePrice":"388","count":"1"}]}]},"message":"操作成功",,"result":"0"}
	private static void parseOrdersData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_FAIL)) {
				handler.sendEmptyMessage(ORDERLIST_GET_FAIL);
			} else {
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);

				ArrayList<Order> tempOrderList = new ArrayList<Order>();
				JSONArray orderListArray = jsonObject
						.getJSONArray(MsgResult.RESULT_LIST_TAG);

				HashMap<String, Object> msgMap = new HashMap<String, Object>();

				int size = orderListArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject orderJsonObject = orderListArray
							.getJSONObject(i);
					Order order = (Order) JsonUtils.fromJsonToJava(
							orderJsonObject, Order.class);
					tempOrderList.add(order);

					ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
					JSONArray goodsArray = orderJsonObject
							.getJSONArray("items");

					for (int j = 0; j < goodsArray.length(); j++) {
						JSONObject goodsJsonObject = goodsArray
								.getJSONObject(j);
						Goods goods = new Goods();
						goods.setId(goodsJsonObject.getString("productId"));
						goods.setName(goodsJsonObject.getString("productName"));
						goods.setSalesPrice(goodsJsonObject
								.getString("salePrice"));
						goods.setNum(goodsJsonObject.getString("count"));
						tempGoodsList.add(goods);
					}
					msgMap.put(order.getId(), tempGoodsList);

				}
				msgMap.put(MsgResult.ORDER_TAG, tempOrderList);
				Log.e("xxx_123",
						""
								+ ((ArrayList<Order>) msgMap
										.get(MsgResult.ORDER_TAG)).size());

				Message message = new Message();
				message.what = ORDERLIST_GET_SUC;
				message.obj = msgMap;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDERLIST_GET_EXCEPTION);
		}
	}

	public static void cancelOrder(final Context context,
			final Handler handler, final String orderId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.order.cancelOrder);

					rpc.addProperty("orderId",
							URLEncoder.encode(orderId, "UTF-8"));
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.order.cancelOrder, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);

					if (!TextUtils.isEmpty(resultStr)) {

						Log.e("xxx_cancelOrder_result", resultStr.toString());
						JSONObject obj = new JSONObject(resultStr);
						parseCancelOrderData(obj, handler);
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

	private static void parseCancelOrderData(JSONObject response,
			Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_FAIL)) {
				handler.sendEmptyMessage(ORDER_CANCEL_FAIL);
			} else {
				String orderID = response.getString("orderId").trim();
				if (!TextUtils.isEmpty(orderID)) {
					OrderManager.setsCurrentOrderId(orderID);
					Message message = new Message();
					message.what = ORDER_CANCEL_SUC;
					message.obj = orderID;
					handler.sendMessage(message);
				} else {
					handler.sendEmptyMessage(ORDER_CANCEL_FAIL);
				}
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_CANCEL_EXCEPTION);
		}
	}

}
