package com.xgf.winecome.entity;

public class OrderState {

	public static String[] state = new String[] { "已下单", "已抢单", "已配送", "已收货 ",
			"已验货", "已取消 ", "已取消" };

	/** 下单 */
	public static final String ORDER_STATUS_ORDERED = "1";
	/** 抢单 */
	public static final String ORDER_STATUS_GRABBED = "2";
	/** 配送 */
	public static final String ORDER_STATUS_DELIVERY = "3";
	/** 收货 */
	public static final String ORDER_STATUS_CONFIRMED = "4";
	/** 验货 */
	public static final String ORDER_STATUS_CHECKED = "5";
	/** 取消 */
	public static final String ORDER_STATUS_CANCELED = "6";
	/** 删除 */
	public static final String ORDER_STATUS_DELETE = "7";

}
