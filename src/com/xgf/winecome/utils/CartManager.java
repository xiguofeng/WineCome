package com.xgf.winecome.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xgf.winecome.entity.Goods;
import com.xgf.winecome.ui.activity.HomeActivity;
import com.xgf.winecome.ui.activity.CategoryActivity;

import android.util.Log;

public class CartManager implements Watched {

	private static ArrayList<Goods> sCartList = new ArrayList<Goods>();

	private static ArrayList<Goods> sSelectCartList = new ArrayList<Goods>();

	private static ArrayList<Goods> sDetailBuyList = new ArrayList<Goods>();

	private static HashMap<Integer, Boolean> sIsSelected = new HashMap<Integer, Boolean>();

	private static boolean sHasGoodsFlag = false;

	private static boolean sHasGoodsByDetailFlag = false;

	private List<Watcher> mWatcherlist = new ArrayList<Watcher>();
	
	/**
	 * 购物车菜单栏价格更新
	 */
	public static int getAllCartNum() {
		int number = 0;
		for (Goods goods : sCartList) {
			number = number+ (Integer.parseInt(goods.getNum()));
		}
		return number;
	}

	/**
	 * 购物车菜单栏价格更新
	 */
	public static void setCartTotalMoney() {
		double totalPay = 0;
		for (Goods goods : sSelectCartList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods.getNum()) * Double
							.parseDouble(goods.getSalesPrice()));
		}
		// ShopCartActivity.mCartNullTv.setVisibility(View.VISIBLE);
		// if (sCartList.size() > 0) {
		// ShopCartActivity.mCartNullTv.setVisibility(View.GONE);
		// }
		HomeActivity.modifyCartPayView(String.valueOf(totalPay),
				String.valueOf(sSelectCartList.size()));
	}

	/**
	 * 首页菜单栏价格更新
	 */
	public static void setTotalMoney(boolean isShow) {
		double totalPay = 0;
		for (Goods goods : sCartList) {
			totalPay = totalPay
					+ (Integer.parseInt(goods.getNum()) * Double
							.parseDouble(goods.getSalesPrice()));
		}

		if (0 == (int) totalPay) {
			isShow = false;
		}
		HomeActivity.modifyMainPayView(String.valueOf(totalPay), isShow);
	}

	/**
	 * 购物车删除
	 * 
	 * @param position
	 */
	public static void cartRemove(int position) {
		// 更新sSelectCartList
		boolean isHasSelect = false;
		for (int i = 0; i < sSelectCartList.size(); i++) {
			if (sSelectCartList.get(i).getId()
					.equals(sCartList.get(position).getId())) {
				sSelectCartList.remove(i);
				isHasSelect = true;
				break;
			}
		}
		if (isHasSelect) {
			ArrayList<Goods> tempCartSelectList = new ArrayList<Goods>();
			for (Goods goods : sSelectCartList) {
				tempCartSelectList.add(goods);
			}
			sSelectCartList.clear();
			sSelectCartList.addAll(tempCartSelectList);
		}

		// 更新sCartList
		sCartList.remove(position);
		ArrayList<Goods> tempCartList = new ArrayList<Goods>();
		for (Goods goods : sCartList) {
			tempCartList.add(goods);
		}
		sCartList.clear();
		sCartList.addAll(tempCartList);

		// 更新首页菜单价格 并不显示
		notifyWatchers();
		setTotalMoney(false);

		setCartTotalMoney();
	}

	/**
	 * 购物车选择信息刷新
	 * 
	 * @param position
	 */
	public static void cartCheckDataRefresh(Goods goods, boolean isCheck) {

		if (!isCheck) {
			for (int i = 0; i < getsSelectCartList().size(); i++) {
				if (getsSelectCartList().get(i).getId().equals(goods.getId())) {
					getsSelectCartList().remove(i);
					break;
				}
			}

			ArrayList<Goods> tempCartSelectList = new ArrayList<Goods>();
			for (Goods tempGoods : getsSelectCartList()) {
				tempCartSelectList.add(tempGoods);
			}
			getsSelectCartList().clear();
			getsSelectCartList().addAll(tempCartSelectList);
		} else {

			boolean isHas = false;
			for (int i = 0; i < getsSelectCartList().size(); i++) {
				if (getsSelectCartList().get(i).getId().equals(goods.getId())) {
					isHas = true;
					break;
				}
			}
			if (!isHas) {
				getsSelectCartList().add(goods);
			}
		}

		// if (getsSelectCartList().size() < getsCartList().size()) {
		// HomeActivity.mCheckAllIb.setChecked(false);
		// } else {
		// HomeActivity.mCheckAllIb.setChecked(true);
		// }

	}

	/**
	 * 首页商品购买更新
	 * 
	 * @param goods
	 */
	public static void cartModifyByMain(Goods goods) {
		boolean isZero = false;
		for (int i = 0; i < sCartList.size(); i++) {
			if (sCartList.get(i).getId().endsWith(goods.getId())) {
				sHasGoodsFlag = true;
				if (0 == Integer.parseInt(goods.getNum())) {
					sCartList.remove(i);
					isZero = true;
				} else {
					sCartList.set(i, goods);
				}
				break;

			}
		}
		if (!sHasGoodsFlag && !isZero) {
			sCartList.add(goods);
		}

		ArrayList<Goods> tempCartList = new ArrayList<Goods>();
		for (Goods tempGoods : sCartList) {
			tempCartList.add(tempGoods);
		}
		sCartList.clear();
		sCartList.addAll(tempCartList);

		sHasGoodsFlag = false;

		setTotalMoney(true);
	}

	/**
	 * 购物车商品更新
	 * 
	 * @param goods
	 * @param isChecked
	 */
	public static void cartModifyByCart(Goods goods, boolean isChecked) {
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

		// 更新首页菜单价格 并不显示
		notifyWatchers();
		setTotalMoney(false);

		if (isChecked) {
			setCartTotalMoney();
		}
	}

	/**
	 * 商品详情购买商品更新
	 * 
	 * @param goods
	 */
	public static boolean cartModifyByDetail(Goods goods, int addNum) {
		for (int i = 0; i < sCartList.size(); i++) {
			if (sCartList.get(i).getId().endsWith(goods.getId())) {
				sHasGoodsByDetailFlag = true;
				int beforeNum = Integer.parseInt(sCartList.get(i).getNum());
				String num = String.valueOf(beforeNum + addNum);
				//Log.e("xxx_num", num);
				goods.setNum(num);
				sCartList.set(i, goods);
				break;
			}
		}
		if (!sHasGoodsByDetailFlag) {
			goods.setNum(String.valueOf(addNum));
			sCartList.add(goods);
		}
		sHasGoodsByDetailFlag = false;

		// 更新首页菜单价格 并不显示
		notifyWatchers();
		setTotalMoney(false);
		setCartTotalMoney();
		return true;
	}

	/**
	 * 订单创建成功后 删除已经选择的购物车里的商品数据
	 */
	public static void removeCartSelect() {

		// 更新sSelectCartList
		boolean isHasSelect = false;
		for (int i = 0; i < sSelectCartList.size(); i++) {
			String id = sSelectCartList.get(i).getId();
			for (int j = 0; j < sCartList.size(); j++) {
				if (id.equals(sCartList.get(j).getId())) {
					sCartList.remove(j);
					//Log.e("xxx_c_remove", "");
				}
			}
		}

		sSelectCartList.clear();
		// 更新sCartList
		ArrayList<Goods> tempCartList = new ArrayList<Goods>();
		for (Goods goods : sCartList) {
			tempCartList.add(goods);
		}
		sCartList.clear();
		sCartList.addAll(tempCartList);

		// 更新首页菜单价格 并不显示
		notifyWatchers();
		setTotalMoney(false);

		if (isHasSelect) {
			setCartTotalMoney();
		}
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
		CategoryActivity.update();
	}

}
