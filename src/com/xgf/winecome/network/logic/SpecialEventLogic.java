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

import com.xgf.winecome.entity.Category;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.network.config.MsgResult;
import com.xgf.winecome.network.config.RequestUrl;
import com.xgf.winecome.utils.JsonUtils;

public class SpecialEventLogic {

	public static final int NET_ERROR = 0;

	public static final int GOODS_LIST_GET_SUC = NET_ERROR + 1;

	public static final int GOODS_LIST_GET_FAIL = GOODS_LIST_GET_SUC + 1;

	public static final int GOODS_LIST_GET_EXCEPTION = GOODS_LIST_GET_FAIL + 1;

	public static void getGoodsBySalesPromotion(final Context context,
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

}
