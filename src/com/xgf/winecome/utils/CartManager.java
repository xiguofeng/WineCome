package com.xgf.winecome.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.xgf.winecome.entity.Category;
import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.activity.HomeActivity;
import com.xgf.winecome.ui.activity.MainActivity;

public class CartManager implements Watched {

	public static ArrayList<Goods> sCartList = new ArrayList<Goods>();

	public static ArrayList<Goods> sSelectCartList = new ArrayList<Goods>();

	public static ArrayList<Goods> sDetailBuyList = new ArrayList<Goods>();

	private static HashMap<Integer, Boolean> sIsSelected = new HashMap<Integer, Boolean>();

	private static boolean sHasGoodsFlag = false;

	private static boolean sHasGoodsByDetailFlag = false;

	private List<Watcher> mWatcherlist = new ArrayList<Watcher>();

	public static void getTotalMoney() {
		double totalPay = 0;
		for (Goods goods : sCartList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods.getNum()) * Double
							.parseDouble(goods.getSalesPrice()));
		}
		HomeActivity.modifyCartPayView(String.valueOf(totalPay),
				String.valueOf(sCartList.size()));
	}

	public static void cartRemove(int position) {
		Log.e("xxx_123", "" + sCartList.size());
		sCartList.remove(position);
		Log.e("xxx_123-1", "" + sCartList.size());
		ArrayList<Goods> tempCartList = new ArrayList<Goods>();
		for (Goods goods : sCartList) {
			tempCartList.add(goods);
		}
		sCartList.clear();
		sCartList.addAll(tempCartList);
		Log.e("xxx_123-2", "" + sCartList.size());

		double totalPay = 0;
		for (Goods goods : sCartList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods.getNum()) * Double
							.parseDouble(goods.getSalesPrice()));
		}
		notifyWatchers();
		//HomeActivity.modifyMainPayView(String.valueOf(totalPay));
		HomeActivity.modifyCartPayView(String.valueOf(totalPay),
				String.valueOf(sCartList.size()));
	}

	public static void cartModifyByMain(Goods goods) {
		for (int i = 0; i < sCartList.size(); i++) {
			if (sCartList.get(i).getId().endsWith(goods.getId())) {
				sHasGoodsFlag = true;
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
							.parseDouble(goods2.getSalesPrice()));
		}

		HomeActivity.modifyMainPayView(String.valueOf(totalPay));
	}

	public static void cartModifyByCart(Goods goods) {
		for (int i = 0; i < sCartList.size(); i++) {
			if (sCartList.get(i).getId().endsWith(goods.getId())) {
				sHasGoodsFlag = true;
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
							.parseDouble(goods2.getSalesPrice()));
		}

		notifyWatchers();
		HomeActivity.modifyCartPayView(String.valueOf(totalPay),
				String.valueOf(sCartList.size()));
	}

	public static void cartModifyByDetail(Goods goods) {

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
							.parseDouble(goods2.getSalesPrice()));
		}

		notifyWatchers();
		HomeActivity.modifyCartPayView(String.valueOf(totalPay),
				String.valueOf(sCartList.size()));
	}

	public static void showOrhHidePayBar(boolean flag) {
		HomeActivity.showOrhHideMainPayBar(flag);

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

	public static ArrayList<Goods> getsDetailBuyList() {
		return sDetailBuyList;
	}

	public static void setsDetailBuyList(ArrayList<Goods> sDetailBuyList) {
		CartManager.sDetailBuyList = sDetailBuyList;
	}

	@Override
	public void addWatcher(Watcher watcher) {
		mWatcherlist.add(watcher);
	}

	@Override
	public void removeWatcher(Watcher watcher) {

	}

	@Override
	public void notifyWatchers(String str) {
		for (Watcher watcher : mWatcherlist) {
			watcher.update(str);
		}
	}

	public static void notifyWatchers() {
		MainActivity.update();
	}

}
