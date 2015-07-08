package com.xgf.winecome.utils;

import java.util.ArrayList;

import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.activity.HomeFragmentActivity;

public class OrderManager {

	public static ArrayList<Goods> sOrderList = new ArrayList<Goods>();

	private static boolean sHasGoodsFlag = false;

	private static boolean sHasGoodsByDetailFlag = false;

	public static void orderModify(Goods goods) {

		for (int i = 0; i < sOrderList.size(); i++) {
			if (sOrderList.get(i).getId().endsWith(goods.getId())) {
				sHasGoodsFlag = true;
				// int appendNum = Integer.parseInt(goods.getNum());
				// int beforeNum = Integer.parseInt(sOrderList.get(i).getNum());
				// goods.setNum(String.valueOf(beforeNum + appendNum));
				sOrderList.set(i, goods);
				break;

			}
		}

		if (!sHasGoodsFlag) {
			sOrderList.add(goods);
		}
		sHasGoodsFlag = false;

		double totalPay = 0;
		for (Goods goods2 : sOrderList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods2.getNum()) * Double
							.parseDouble(goods2.getPrice()));
		}

		HomeFragmentActivity.modifyOrderView(String.valueOf(totalPay));
	}

	public static void orderModifyByDetail(Goods goods) {

		for (int i = 0; i < sOrderList.size(); i++) {
			if (sOrderList.get(i).getId().endsWith(goods.getId())) {
				sHasGoodsByDetailFlag = true;
				int appendNum = Integer.parseInt(goods.getNum());
				int beforeNum = Integer.parseInt(sOrderList.get(i).getNum());
				goods.setNum(String.valueOf(beforeNum + appendNum));
				sOrderList.set(i, goods);
				break;
			}
		}
		if (!sHasGoodsByDetailFlag) {
			sOrderList.add(goods);
		}
		sHasGoodsByDetailFlag = false;

		double totalPay = 0;
		for (Goods goods2 : sOrderList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods2.getNum()) * Double
							.parseDouble(goods2.getPrice()));
		}

		HomeFragmentActivity.modifyOrderView(String.valueOf(totalPay));
	}

	public static void showOrhHidePayBar(boolean flag) {
		HomeFragmentActivity.showOrhHidePayBar(flag);
	}

}
