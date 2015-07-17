package com.xgf.winecome.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.activity.HomeActivity;

public class CartManager {

	public static ArrayList<Goods> sCartList = new ArrayList<Goods>();

	public static ArrayList<Goods> sSelectCartList = new ArrayList<Goods>();

	private static HashMap<Integer, Boolean> sIsSelected = new HashMap<Integer, Boolean>();

	private static boolean sHasGoodsFlag = false;

	private static boolean sHasGoodsByDetailFlag = false;

	public static void cartModify(Goods goods) {

		for (int i = 0; i < sCartList.size(); i++) {
			if (sCartList.get(i).getId().endsWith(goods.getId())) {
				sHasGoodsFlag = true;
				// int appendNum = Integer.parseInt(goods.getNum());
				// int beforeNum = Integer.parseInt(sOrderList.get(i).getNum());
				// goods.setNum(String.valueOf(beforeNum + appendNum));
				sCartList.set(i, goods);
				break;

			}
		}

		if (!sHasGoodsFlag) {
			sCartList.add(goods);
		}
		sHasGoodsFlag = false;

		double totalPay = 0;
		for (Goods goods2 : sCartList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods2.getNum()) * Double
							.parseDouble(goods2.getPrice()));
		}

		HomeActivity.modifyPayView(String.valueOf(totalPay));
	}

	public static void cartModifyByOther(Goods goods) {

		for (int i = 0; i < sCartList.size(); i++) {
			if (sCartList.get(i).getId().endsWith(goods.getId())) {
				sHasGoodsByDetailFlag = true;
				int appendNum = Integer.parseInt(goods.getNum());
				int beforeNum = Integer.parseInt(sCartList.get(i).getNum());
				goods.setNum(String.valueOf(beforeNum + appendNum));
				sCartList.set(i, goods);
				break;
			}
		}
		if (!sHasGoodsByDetailFlag) {
			sCartList.add(goods);
		}
		sHasGoodsByDetailFlag = false;

		double totalPay = 0;
		for (Goods goods2 : sCartList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods2.getNum()) * Double
							.parseDouble(goods2.getPrice()));
		}

		HomeActivity.modifyPayView(String.valueOf(totalPay));
	}

	public static void showOrhHidePayBar(boolean flag) {
		HomeActivity.showOrhHidePayBarByCart(flag);
		
	}

	public static ArrayList<Goods> getsCartList() {
		return sCartList;
	}

	public static void setsCartList(ArrayList<Goods> sCartList) {
		CartManager.sCartList = sCartList;
	}

	public static ArrayList<Goods> getsSelectCartList() {
		return sSelectCartList;
	}

	public static void setsSelectCartList(ArrayList<Goods> sSelectCartList) {
		CartManager.sSelectCartList = sSelectCartList;
	}

	public static HashMap<Integer, Boolean> getsIsSelected() {
		return sIsSelected;
	}

	public static void setsIsSelected(HashMap<Integer, Boolean> sIsSelected) {
		CartManager.sIsSelected = sIsSelected;
	}

}
