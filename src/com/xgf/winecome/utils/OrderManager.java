package com.xgf.winecome.utils;

import java.util.ArrayList;

import com.xgf.winecome.entity.Order;

public class OrderManager {

	public static String sCurrentOrderId;

	public static ArrayList<Order> sOrderList = new ArrayList<Order>();

	public static Order sCurrentOrder = new Order();

	public static ArrayList<Order> getsOrderList() {
		return sOrderList;
	}

	public static void setsOrderList(ArrayList<Order> sOrderList) {
		OrderManager.sOrderList = sOrderList;
	}

	public static Order getsCurrentOrder() {
		return sCurrentOrder;
	}

	public static void setsCurrentOrder(Order sCurrentOrder) {
		OrderManager.sCurrentOrder = sCurrentOrder;
	}

	public static String getsCurrentOrderId() {
		return sCurrentOrderId;
	}

	public static void setsCurrentOrderId(String sCurrentOrderId) {
		OrderManager.sCurrentOrderId = sCurrentOrderId;
	}

}
