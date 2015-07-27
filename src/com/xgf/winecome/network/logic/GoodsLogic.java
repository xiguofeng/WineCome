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

import com.xgf.winecome.entity.Category;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.network.config.RequestUrl;

public class GoodsLogic {

	public static final int NET_ERROR = 0;

	public static final int CATEGROY_LIST_GET_SUC = NET_ERROR + 1;

	public static final int CATEGROY_LIST_GET_FAIL = CATEGROY_LIST_GET_SUC + 1;

	public static final int CATEGROY_LIST_GET_EXCEPTION = CATEGROY_LIST_GET_FAIL + 1;

	public static final int GOODS_LIST_GET_SUC = CATEGROY_LIST_GET_EXCEPTION + 1;

	public static final int GOODS_LIST_GET_FAIL = GOODS_LIST_GET_SUC + 1;

	public static final int GOODS_LIST_GET_EXCEPTION = GOODS_LIST_GET_FAIL + 1;

	public static final int GOODS_LIST_BY_KEY_GET_SUC = GOODS_LIST_GET_EXCEPTION + 1;

	public static final int GOODS_LIST_BY_KEY_GET_FAIL = GOODS_LIST_BY_KEY_GET_SUC + 1;

	public static final int GOODS_LIST_BY_KEY_GET_EXCEPTION = GOODS_LIST_BY_KEY_GET_FAIL + 1;

	public static void getCategroyList(final Context context,
			final Handler handler, final String categoryID) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.goods.queryGoodsCategory);

					rpc.addProperty("topCategory",
							URLEncoder.encode("白酒", "UTF-8"));
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.goods.queryGoodsCategory, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_resultStr", resultStr);

					if (TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseCategroyListData(obj, handler);
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

	private static void parseCategroyListData(JSONObject response,
			Handler handler) {
		try {
			ArrayList<Category> mTempGoodsCategoryList = new ArrayList<Category>();
			JSONArray goodsCategoryListArray = response
					.getJSONArray("goodsCategorys");

			int size = goodsCategoryListArray.length();
			JSONObject ggnewsListJsonObject = new JSONObject();
			for (int i = 0; i < size; i++) {
				ggnewsListJsonObject = goodsCategoryListArray.getJSONObject(i);

				Category category = new Category();
				String categoryID = ggnewsListJsonObject
						.getString("categoryID").trim();
				String categoryName = ggnewsListJsonObject.getString(
						"categoryName").trim();
				mTempGoodsCategoryList.add(category);
			}

			Message message = new Message();
			message.what = CATEGROY_LIST_GET_SUC;
			message.obj = mTempGoodsCategoryList;
			handler.sendMessage(message);

		} catch (JSONException e) {
			handler.sendEmptyMessage(CATEGROY_LIST_GET_EXCEPTION);
		}
	}

	public static void getGoodsByKey(final Context context,
			final Handler handler, final String keyword, final int limit) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.goods.queryGoodsCategory);

					rpc.addProperty("topCategory",
							URLEncoder.encode("白酒", "UTF-8"));
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.goods.queryGoodsCategory, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_resultStr", resultStr);

					if (TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseGoodsListByKeyData(obj, handler);
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

	private static void parseGoodsListByKeyData(JSONObject response,
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
				goods.setBrief(goodsBrief);
				goods.setPrice(String.valueOf(goodsPrice));
				mTempGoodsList.add(goods);
			}

			Message message = new Message();
			message.what = GOODS_LIST_BY_KEY_GET_SUC;
			message.obj = mTempGoodsList;
			handler.sendMessage(message);

		} catch (JSONException e) {
			handler.sendEmptyMessage(GOODS_LIST_BY_KEY_GET_EXCEPTION);
		}
	}

	public static void getGoodsByCategroy(final Context context,
			final Handler handler, final String ppid, final String pageNum,
			final String pageSize) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.goods.queryGoods);

					rpc.addProperty("ppid", URLEncoder.encode("2", "UTF-8"));
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
							+ RequestUrl.goods.queryGoods, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_resultStr", resultStr);

					if (TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseGoodsListData(obj, handler);
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

	private static void parseGoodsListData(JSONObject response, Handler handler) {
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
				goods.setBrief(goodsBrief);
				goods.setPrice(String.valueOf(goodsPrice));
				mTempGoodsList.add(goods);
			}

			Message message = new Message();
			message.what = GOODS_LIST_BY_KEY_GET_SUC;
			message.obj = mTempGoodsList;
			handler.sendMessage(message);

		} catch (JSONException e) {
			handler.sendEmptyMessage(GOODS_LIST_BY_KEY_GET_EXCEPTION);
		}
	}

}
