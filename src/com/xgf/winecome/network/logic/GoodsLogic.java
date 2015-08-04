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
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;
import com.xgf.winecome.utils.JsonUtils;

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
							URLEncoder.encode(categoryID, "UTF-8"));
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
					Log.e("xxx_getCategroyList_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
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

	// {"datas":{"total":"7","list":[[{"ppid":"1","ppmc":"洋河系列","pplx":"01"},{"ppid":"2","ppmc":"五粮液系列","pplx":"01"},{"ppid":"4","ppmc":"国窖系列","pplx":"01"},{"ppid":"7","ppmc":"茅台系列","pplx":"01"},{"ppid":"9","ppmc":"泸州老窖","pplx":"01"}],[{"ppid":"3","ppmc":"长城系列","pplx":"02"},{"ppid":"5","ppmc":"张裕系列","pplx":"02"}]]},"message":"操作成功","result":"0"}
	private static void parseCategroyListData(JSONObject response,
			Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_FAIL)) {
				handler.sendEmptyMessage(CATEGROY_LIST_GET_FAIL);
			} else {

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
				ArrayList<Category> mTempCategoryList = new ArrayList<Category>();
				JSONArray categorysListArray = jsonObject
						.getJSONArray(MsgResult.RESULT_LIST_TAG);
				int size = categorysListArray.length();
				for (int i = 0; i < size; i++) {
					JSONArray categoryListArray = categorysListArray
							.getJSONArray(i);
					int categorySize = categoryListArray.length();
					if (0 == i) {
						Category category = new Category();
						category.setPpid("t_0");
						category.setPplx("t_00");
						category.setPpmc("白酒");
						mTempCategoryList.add(category);
					} else {
						Category category = new Category();
						category.setPpid("t_1");
						category.setPplx("t_10");
						category.setPpmc("葡萄酒");
						mTempCategoryList.add(category);
					}
					for (int j = 0; j < categorySize; j++) {
						JSONObject categoryJsonObject = categoryListArray
								.getJSONObject(j);

						Category category = (Category) JsonUtils
								.fromJsonToJava(categoryJsonObject,
										Category.class);
						mTempCategoryList.add(category);
					}
				}

				Message message = new Message();
				message.what = CATEGROY_LIST_GET_SUC;
				message.obj = mTempCategoryList;
				handler.sendMessage(message);
			}
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
			final Handler handler, final String ppid, final String name,
			final String pageNum, final String pageSize) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.goods.queryGoods);

					rpc.addProperty("ppid", URLEncoder.encode(ppid, "UTF-8"));
					rpc.addProperty("name", URLEncoder.encode(name, "UTF-8"));
					rpc.addProperty("pageNum",
							URLEncoder.encode(pageNum, "UTF-8"));
					rpc.addProperty("pageSize",
							URLEncoder.encode(pageSize, "UTF-8"));
					// rpc.addProperty("md5", URLEncoder.encode("1111",
					// "UTF-8"));

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
					Log.e("xxx_getGoodsByCategroy_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
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

	// {"datas":{"total":"5","list":[{"id":"10002","goodsName":"海之蓝","salesPrice":"120","marketPrice":"198","addedTime":"2015-07-01 12:38:50.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"45","level":"特级","model":"清香"},{"id":"10003","goodsName":"梦之蓝3","salesPrice":"600","marketPrice":"1024","addedTime":"2015-07-01 12:44:33.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"50","level":"优质","model":"浓香"},{"id":"10005","goodsName":"天之蓝","salesPrice":"290","marketPrice":"398","addedTime":"2015-08-02 09:25:46.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"50","level":"优质","model":"清香"},{"id":"10009","goodsName":"梦之蓝6","salesPrice":"800","marketPrice":"1000","addedTime":"2015-08-02 09:27:17.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"50","level":"特级","model":"清香"},{"id":"2222","name":"梦之蓝9","salesPrice":"388","marketPrice":"398","addedTime":"2015-08-02 10:42:01.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"","level":"优质","model":"浓香"}]},"message":"操作成功","result":"0"}
	private static void parseGoodsListData(JSONObject response, Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_FAIL)) {
				handler.sendEmptyMessage(GOODS_LIST_GET_FAIL);
			} else {

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
					mTempGoodsList.add(goods);
				}
				Message message = new Message();
				message.what = GOODS_LIST_GET_SUC;
				message.obj = mTempGoodsList;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(GOODS_LIST_GET_EXCEPTION);
		}
	}

	public static void getAllGoods(final Context context, final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.goods.queryGoods);

					// rpc.addProperty("md5", URLEncoder.encode("1111",
					// "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.goods.queryAllGoods, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_getAllGoods_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseAllGoodsListData(obj, handler);
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

	private static void parseAllGoodsListData(JSONObject response,
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
			message.what = GOODS_LIST_BY_KEY_GET_SUC;
			message.obj = mTempGoodsList;
			handler.sendMessage(message);

		} catch (JSONException e) {
			handler.sendEmptyMessage(GOODS_LIST_BY_KEY_GET_EXCEPTION);
		}
	}

}
