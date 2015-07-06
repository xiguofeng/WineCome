package com.xgf.winecome.utils;

import java.util.ArrayList;

import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.activity.MainActivity;

public class OrderManager {

	public static ArrayList<Goods> sOrderList = new ArrayList<Goods>();

	public static void nowOrderModify(Goods goods) {

		for (int i = 0; i < sOrderList.size(); i++) {
			if (sOrderList.get(i).getId().endsWith(goods.getId())) {
				// int appendNum = Integer.parseInt(goods.getNum());
				// int beforeNum = Integer.parseInt(sOrderList.get(i).getNum());
				// goods.setNum(String.valueOf(beforeNum + appendNum));
				sOrderList.set(i, goods);
			}
		}
		sOrderList.add(goods);

		double totalPay = 0;
		for (Goods goods2 : sOrderList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods2.getNum()) * Double
							.parseDouble(goods2.getPrice()));
		}

		MainActivity.modifyOrderView(String.valueOf(totalPay));
	}

	public static void showOrhHidePayBar(boolean flag) {
		MainActivity.showOrhHidePayBar(flag);
	}

}
