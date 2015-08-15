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
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;
import com.xgf.winecome.utils.JsonUtils;

public class IntegralGoodsLogic {

	public static final int NET_ERROR = 0;
	
	public static final int INTEGRAL_TOTAL_GET_SUC = NET_ERROR + 1;

	public static final int INTEGRAL_TOTAL_GET_FAIL = INTEGRAL_TOTAL_GET_SUC + 1;

	public static final int INTEGRAL_TOTAL_GET_EXCEPTION = INTEGRAL_TOTAL_GET_FAIL + 1;

	public static final int INTEGRAL_GOODS_LIST_GET_SUC = INTEGRAL_TOTAL_GET_EXCEPTION + 1;

	public static final int INTEGRAL_GOODS_LIST_GET_FAIL = INTEGRAL_GOODS_LIST_GET_SUC + 1;

	public static final int INTEGRAL_GOODS_LIST_GET_EXCEPTION = INTEGRAL_GOODS_LIST_GET_FAIL + 1;

	public static final int INTEGRAL_GOODS_LIST_BY_KEY_GET_SUC = INTEGRAL_GOODS_LIST_GET_EXCEPTION + 1;

	public static final int INTEGRAL_GOODS_LIST_BY_KEY_GET_FAIL = INTEGRAL_GOODS_LIST_BY_KEY_GET_SUC + 1;

	public static final int INTEGRAL_GOODS_LIST_BY_KEY_GET_EXCEPTION = INTEGRAL_GOODS_LIST_BY_KEY_GET_FAIL + 1;

	public static final int CATEGROY_INTEGRAL_GOODS_LIST_GET_SUC = INTEGRAL_GOODS_LIST_BY_KEY_GET_EXCEPTION + 1;

	public static final int CATEGROY_INTEGRAL_GOODS_LIST_GET_FAIL = CATEGROY_INTEGRAL_GOODS_LIST_GET_SUC + 1;

	public static final int CATEGROY_INTEGRAL_GOODS_LIST_GET_EXCEPTION = CATEGROY_INTEGRAL_GOODS_LIST_GET_FAIL + 1;

	public static void getAllIntegralGoods(final Context context,
			final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.integral.queryAllOnIntegralMall);

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.integral.queryAllOnIntegralMall,
							envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_llIntegralINTEGRAL_GOODS_resultStr", resultStr);

					if (TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseIntegralGoodsListData(obj, handler);
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

	private static void parseIntegralGoodsListData(JSONObject response,
			Handler handler) {
		try {
			ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();
			JSONArray goodsListArray = response.getJSONArray("goodsList");

			int size = goodsListArray.length();
			JSONObject goodsJsonObject = new JSONObject();
			for (int i = 0; i < size; i++) {
				goodsJsonObject = goodsListArray.getJSONObject(i);

				Goods goods = new Goods();
				String goodsID = goodsJsonObject.getString("goodsID").trim();
				String goodsName = goodsJsonObject.getString("goodsName")
						.trim();
				String goodsBrief = goodsJsonObject.getString("goodsBrief")
						.trim();
				Double goodsPrice = goodsJsonObject.getDouble("price");

				goods.setId(goodsID);
				goods.setName(goodsName);
				mTempGoodsList.add(goods);
			}

			Message message = new Message();
			message.what = INTEGRAL_GOODS_LIST_BY_KEY_GET_SUC;
			message.obj = mTempGoodsList;
			handler.sendMessage(message);

		} catch (JSONException e) {
			handler.sendEmptyMessage(INTEGRAL_GOODS_LIST_BY_KEY_GET_EXCEPTION);
		}
	}

	public static void getIntegralGoodsByRange(final Context context,
			final Handler handler, final String range, final String keyword,
			final String pageNum, final String pageSize) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.integral.queryProductOnIntegralMall);

					rpc.addProperty("range", URLEncoder.encode(range, "UTF-8"));
					rpc.addProperty("keyword",
							URLEncoder.encode(keyword, "UTF-8"));
					rpc.addProperty("pageNum",
							URLEncoder.encode(pageNum, "UTF-8"));
					rpc.addProperty("pageSize",
							URLEncoder.encode(pageSize, "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.integral.queryProductOnIntegralMall,
							envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_IntegralGoodsByRange_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseIntegralGoodsData(obj, handler);
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

	private static void parseIntegralGoodsData(JSONObject response,
			Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
				ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();
				JSONArray goodsListArray = jsonObject
						.getJSONArray(MsgResult.RESULT_LIST_TAG);
				Log.e("xxx_parseGoodsListData_0", "goodsListArray-------->"
						+ goodsListArray.toString());
				int size = goodsListArray.length();
				for (int j = 0; j < size; j++) {
					JSONObject categoryJsonObject = goodsListArray
							.getJSONObject(j);

					Goods goods = (Goods) JsonUtils.fromJsonToJava(
							categoryJsonObject, Goods.class);
					goods.setNum("0");
					mTempGoodsList.add(goods);
				}
				Message message = new Message();
				message.what = INTEGRAL_GOODS_LIST_GET_SUC;
				message.obj = mTempGoodsList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(INTEGRAL_GOODS_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(INTEGRAL_GOODS_LIST_GET_EXCEPTION);
		}
	}

	public static void exchange(final Context context, final Handler handler,
			final String phone, final String productId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.integral.exchange);
					
					JSONObject requestJson = new JSONObject();
					requestJson.put("phone",
							URLEncoder.encode(phone, "UTF-8"));
					requestJson.put("productId",
							URLEncoder.encode(productId, "UTF-8"));
					Log.e("xxx_order_json", requestJson.toString());
					rpc.addProperty("data", requestJson.toString());
					rpc.addProperty("md5", URLEncoder.encode("123", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.integral.exchange, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_exchange_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseExchangeData(obj, handler);
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

	private static void parseExchangeData(JSONObject response,
			Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
				ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();
				JSONArray goodsListArray = jsonObject
						.getJSONArray(MsgResult.RESULT_LIST_TAG);
				Log.e("xxx_parseGoodsListData_0", "goodsListArray-------->"
						+ goodsListArray.toString());
				int size = goodsListArray.length();
				for (int j = 0; j < size; j++) {
					JSONObject categoryJsonObject = goodsListArray
							.getJSONObject(j);

					Goods goods = (Goods) JsonUtils.fromJsonToJava(
							categoryJsonObject, Goods.class);
					goods.setNum("0");
					mTempGoodsList.add(goods);
				}
				Message message = new Message();
				message.what = INTEGRAL_GOODS_LIST_GET_SUC;
				message.obj = mTempGoodsList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(INTEGRAL_GOODS_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(INTEGRAL_GOODS_LIST_GET_EXCEPTION);
		}
	}
	
	public static void getExchangeHistroy(final Context context, final Handler handler,
			final String phone, final String productId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.integral.queryIntegralConsume);
					
					JSONObject requestJson = new JSONObject();
					requestJson.put("phone",
							URLEncoder.encode(phone, "UTF-8"));
					requestJson.put("productId",
							URLEncoder.encode(productId, "UTF-8"));
					Log.e("xxx_order_json", requestJson.toString());
					rpc.addProperty("data", requestJson.toString());
					rpc.addProperty("md5", URLEncoder.encode("123", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.integral.queryIntegralConsume, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_ExchangeHistroy_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseExchangeHistroyData(obj, handler);
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

	private static void parseExchangeHistroyData(JSONObject response,
			Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
				ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();
				JSONArray goodsListArray = jsonObject
						.getJSONArray(MsgResult.RESULT_LIST_TAG);
				Log.e("xxx_parseGoodsListData_0", "goodsListArray-------->"
						+ goodsListArray.toString());
				int size = goodsListArray.length();
				for (int j = 0; j < size; j++) {
					JSONObject categoryJsonObject = goodsListArray
							.getJSONObject(j);

					Goods goods = (Goods) JsonUtils.fromJsonToJava(
							categoryJsonObject, Goods.class);
					goods.setNum("0");
					mTempGoodsList.add(goods);
				}
				Message message = new Message();
				message.what = INTEGRAL_GOODS_LIST_GET_SUC;
				message.obj = mTempGoodsList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(INTEGRAL_GOODS_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(INTEGRAL_GOODS_LIST_GET_EXCEPTION);
		}
	}

}
