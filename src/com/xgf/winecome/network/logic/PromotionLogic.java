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

import com.xgf.winecome.entity.Category;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.entity.PromotionNew;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;
import com.xgf.winecome.utils.JsonUtils;
import com.xgf.winecome.utils.OrderManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class PromotionLogic {

	public static final int NET_ERROR = 0;

	public static final int PROMOTION_ALL_LIST_GET_SUC = NET_ERROR + 1;

	public static final int PROMOTION_ALL_LIST_GET_FAIL = PROMOTION_ALL_LIST_GET_SUC + 1;

	public static final int PROMOTION_ALL_LIST_GET_EXCEPTION = PROMOTION_ALL_LIST_GET_FAIL + 1;

	public static final int PROMOTION_BANNER_LIST_GET_SUC = NET_ERROR + 1;

	public static final int PROMOTION_BANNER_LIST_GET_FAIL = PROMOTION_BANNER_LIST_GET_SUC + 1;

	public static final int PROMOTION_BANNER_LIST_GET_EXCEPTION = PROMOTION_BANNER_LIST_GET_FAIL + 1;
	
	public static final int PROMOTION_GET_SUC = NET_ERROR + 1;

	public static final int PROMOTION_GET_FAIL = PROMOTION_GET_SUC + 1;

	public static final int PROMOTION_GET_EXCEPTION = PROMOTION_BANNER_LIST_GET_FAIL + 1;

	public static void getAllPromotion(final Context context,
			final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.promotion.queryPromotionV2);
					rpc.addProperty("phone", URLEncoder.encode("", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.promotion.queryPromotionV2, envelope);
					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);

					Log.e("xxx_getAllPromotion", resultStr);
					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseAllPromotionData(obj, handler);
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

	private static void parseAllPromotionData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
				ArrayList<PromotionNew> mTempPromotionList = new ArrayList<PromotionNew>();
				JSONArray promotionListArray = jsonObject
						.getJSONArray(MsgResult.RESULT_LIST_TAG);
				int size = promotionListArray.length();
				for (int j = 0; j < size; j++) {
					JSONObject categoryJsonObject = promotionListArray
							.getJSONObject(j);
					JSONArray goodsListArray = categoryJsonObject
							.getJSONArray("items");
					ArrayList<Goods> goodsList = new ArrayList<Goods>();
					for (int k = 0; k < goodsListArray.length(); k++) {
						JSONObject goodsJsonObject = goodsListArray
								.getJSONObject(k);
						Goods goods = (Goods) JsonUtils.fromJsonToJava(
								goodsJsonObject, Goods.class);
						goodsList.add(goods);
					}

					PromotionNew promotion = (PromotionNew) JsonUtils
							.fromJsonToJava(categoryJsonObject,
									PromotionNew.class);

					ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
					promotion.setGoodsList(tempGoodsList);
					promotion.getGoodsList().addAll(goodsList);

					mTempPromotionList.add(promotion);
				}
				Message message = new Message();
				message.what = PROMOTION_ALL_LIST_GET_SUC;
				message.obj = mTempPromotionList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(PROMOTION_ALL_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(PROMOTION_ALL_LIST_GET_EXCEPTION);
		}
	}
	
	public static void getPromotionById(final Context context,
			final Handler handler,final String id) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.promotion.queryPromotionV2);

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.promotion.queryPromotionV2, envelope);
					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);

					Log.e("xxx_getPromotionById", resultStr);
					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parsePromotionByIdData(obj, handler);
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

	private static void parsePromotionByIdData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject categoryJsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
		
					JSONArray goodsListArray = categoryJsonObject
							.getJSONArray("items");
					ArrayList<Goods> goodsList = new ArrayList<Goods>();
					for (int k = 0; k < goodsListArray.length(); k++) {
						JSONObject goodsJsonObject = goodsListArray
								.getJSONObject(k);
						Goods goods = (Goods) JsonUtils.fromJsonToJava(
								goodsJsonObject, Goods.class);
						goodsList.add(goods);
					}

					PromotionNew promotion = (PromotionNew) JsonUtils
							.fromJsonToJava(categoryJsonObject,
									PromotionNew.class);

					ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
					promotion.setGoodsList(tempGoodsList);
					promotion.getGoodsList().addAll(goodsList);

				Message message = new Message();
				message.what = PROMOTION_GET_SUC;
				message.obj = promotion;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(PROMOTION_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(PROMOTION_GET_EXCEPTION);
		}
	}


}
