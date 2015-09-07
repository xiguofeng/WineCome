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
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;
import com.xgf.winecome.utils.JsonUtils;
import com.xgf.winecome.utils.OrderManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

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

	public static final int CATEGROY_GOODS_LIST_GET_SUC = GOODS_LIST_BY_KEY_GET_EXCEPTION + 1;

	public static final int CATEGROY_GOODS_LIST_GET_FAIL = CATEGROY_GOODS_LIST_GET_SUC + 1;

	public static final int CATEGROY_GOODS_LIST_GET_EXCEPTION = CATEGROY_GOODS_LIST_GET_FAIL + 1;

	public static final int AUTH_QR_CODE_SUC = CATEGROY_GOODS_LIST_GET_EXCEPTION + 1;

	public static final int AUTH_QR_CODE_FAIL = AUTH_QR_CODE_SUC + 1;

	public static final int AUTH_QR_CODE_EXCEPTION = AUTH_QR_CODE_FAIL + 1;

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
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

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

			} else {
				handler.sendEmptyMessage(CATEGROY_LIST_GET_FAIL);
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

	// {"datas":{"total":"5","list":[{"id":"10002","goodsName":"海之蓝","salesPrice":"120","marketPrice":"198","addedTime":"2015-07-01
	// 12:38:50.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"45","level":"特级","model":"清香"},{"id":"10003","goodsName":"梦之蓝3","salesPrice":"600","marketPrice":"1024","addedTime":"2015-07-01
	// 12:44:33.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"50","level":"优质","model":"浓香"},{"id":"10005","goodsName":"天之蓝","salesPrice":"290","marketPrice":"398","addedTime":"2015-08-02
	// 09:25:46.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"50","level":"优质","model":"清香"},{"id":"10009","goodsName":"梦之蓝6","salesPrice":"800","marketPrice":"1000","addedTime":"2015-08-02
	// 09:27:17.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"50","level":"特级","model":"清香"},{"id":"2222","name":"梦之蓝9","salesPrice":"388","marketPrice":"398","addedTime":"2015-08-02
	// 10:42:01.0","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/","area":"","degree":"","level":"优质","model":"浓香"}]},"message":"操作成功","result":"0"}
	private static void parseGoodsListData(JSONObject response, Handler handler) {
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
				message.what = GOODS_LIST_GET_SUC;
				message.obj = mTempGoodsList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(GOODS_LIST_GET_FAIL);
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
							RequestUrl.goods.queryAllGoods);

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

	public static void getCategroyAndGoodsList(final Context context,
			final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.goods.queryAllGoods);

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
					Log.e("xxx_getCategroyAndGoodsList_resultStr", resultStr);

					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseCategroyAndGoodsListData(obj, handler);
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

	// {"message":"操作成功","datas":{"total":7,"list":[[{"ppmc":"洋河系列","ppid":"1","plist":[{"id":"10002","salesPrice":"120","model":"清香","addedTime":"2015-07-01
	// 12:38:50.0","marketPrice":"198","level":"特级","degree":"45","area":"","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/2015821128311.jpg","name":"海之蓝","images":[{"url":"http://www.diyifw.com:8080/jll/upload/2015821143291.jpg"}]},{"id":"10003","salesPrice":"600","model":"浓香","addedTime":"2015-07-01
	// 12:44:33.0","marketPrice":"1024","level":"优质","degree":"50","area":"","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/2015841459481.jpg","name":"梦之蓝3","images":[{"url":"http://www.diyifw.com:8080/jll/upload/2015821143521.jpg"}]},{"id":"10005","salesPrice":"290","model":"清香","addedTime":"2015-08-02
	// 09:25:46.0","marketPrice":"398","level":"优质","degree":"50","area":"","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/201584150281.jpg","name":"天之蓝","images":[{"url":"http://www.diyifw.com:8080/jll/upload/2015821144361.jpg"}]},{"id":"10009","salesPrice":"800","model":"清香","addedTime":"2015-08-02
	// 09:27:17.0","marketPrice":"1000","level":"特级","degree":"50","area":"","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/201584150521.jpg","name":"梦之蓝6","images":[{"url":"http://www.diyifw.com:8080/jll/upload/2015821144541.jpg"}]},{"id":"2222","salesPrice":"388","model":"浓香","addedTime":"2015-08-02
	// 10:42:01.0","marketPrice":"398","level":"优质","degree":"","area":"","desc":"","iconUrl":"","name":"梦之蓝9","images":[{"url":""}]}],"pplx":"01"},{"ppmc":"五粮液系列","ppid":"2","plist":[{"id":"10004","salesPrice":"700","model":"浓香","addedTime":"2015-07-01
	// 12:52:37.0","marketPrice":"800","level":"特级","degree":"50","area":"","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/201584150111.jpg","name":"五粮液","images":[{"url":"http://www.diyifw.com:8080/jll/upload/2015821144151.jpg"}]}],"pplx":"01"},{"ppmc":"国窖系列","ppid":"4","pplx":"01"},{"ppmc":"茅台系列","ppid":"7","plist":[{"id":"10010","salesPrice":"1200","model":"浓香","addedTime":"2015-08-02
	// 09:28:53.0","marketPrice":"2000","level":"特级","degree":"52","area":"","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/20158415181.jpg","name":"茅台","images":[{"url":"http://www.diyifw.com:8080/jll/upload/2015821145121.jpg"}]}],"pplx":"01"},{"ppmc":"泸州老窖","ppid":"9","plist":[{"id":"10020","salesPrice":"300","model":"浓香","addedTime":"2015-08-02
	// 09:33:39.0","marketPrice":"500","level":"优质","degree":"52","area":"","desc":"","iconUrl":"http://www.diyifw.com:8080/jll/upload/201584151231.jpg","name":"泸州老窖","images":[{"url":"http://www.diyifw.com:8080/jll/upload/2015821145271.jpg"}]}],"pplx":"01"}],[{"ppmc":"长城系列","ppid":"3","plist":[],"pplx":"02"},{"ppmc":"张裕系列","ppid":"5","plist":[],"pplx":"02"}]]},"result":"0"}
	private static void parseCategroyAndGoodsListData(JSONObject response,
			Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
				ArrayList<Category> tempCategoryList = new ArrayList<Category>();
				JSONArray categorysListArray = jsonObject
						.getJSONArray(MsgResult.RESULT_LIST_TAG);
				HashMap<String, Object> msgMap = new HashMap<String, Object>();
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
						tempCategoryList.add(category);
					} else {
						Category category = new Category();
						category.setPpid("t_1");
						category.setPplx("t_10");
						category.setPpmc("葡萄酒");
						tempCategoryList.add(category);
					}

					for (int j = 0; j < categorySize; j++) {
						JSONObject categoryJsonObject = categoryListArray
								.getJSONObject(j);
						// Category category = new Category();
						// category.setPpid(categoryJsonObject.getString("ppid"));
						// category.setPpmc(categoryJsonObject.getString("ppmc"));
						// category.setPplx(categoryJsonObject.getString("pplx"));

						Category category = (Category) JsonUtils
								.fromJsonToJava(categoryJsonObject,
										Category.class);

						tempCategoryList.add(category);

						ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
						JSONArray goodsArray = categoryJsonObject
								.getJSONArray("plist");

						for (int k = 0; k < goodsArray.length(); k++) {
							JSONObject goodsJsonObject = goodsArray
									.getJSONObject(k);

							Goods goods = (Goods) JsonUtils.fromJsonToJava(
									goodsJsonObject, Goods.class);
							goods.setNum("0");
							tempGoodsList.add(goods);
						}
						msgMap.put(category.getPpid(), tempGoodsList);
					}
				}
				msgMap.put("Category", tempCategoryList);

				Message message = new Message();
				message.what = CATEGROY_GOODS_LIST_GET_SUC;
				message.obj = msgMap;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(CATEGROY_GOODS_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CATEGROY_GOODS_LIST_GET_EXCEPTION);
		}
	}

	public static void authQrCodeCode(final Context context,
			final Handler handler, final String qrcode) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE,
							RequestUrl.goods.authProduct);

					rpc.addProperty("qrcode",
							URLEncoder.encode(qrcode, "UTF-8"));
					rpc.addProperty("orderId",
							OrderManager.getsCurrentOrderId());

					AndroidHttpTransport ht = new AndroidHttpTransport(
							RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/"
							+ RequestUrl.goods.authProduct, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);

					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseSendAuthCodeData(obj, handler);
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

	// {"datas":"{}","message":"扫描鉴真失败，没有商品信息","result":"10009"}
	private static void parseSendAuthCodeData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(AUTH_QR_CODE_SUC);
			} else {
				handler.sendEmptyMessage(AUTH_QR_CODE_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(AUTH_QR_CODE_EXCEPTION);
		}
	}

}
