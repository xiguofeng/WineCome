package com.xgf.winecome.network.config;

/**
 * remote request url
 */
public class RequestUrl {

	public static final String NAMESPACE = "http://www.diyifw.com";

	public static final String HOST_URL = "http://www.diyifw.com:8080/jll/services/JllService";

	// public static final String HOST_URL =
	// "http://192.168.1.101:8080/com.igou.server";

	public interface connect {
		/**
		 * 连接 获取推送
		 */
		public String connect = "/user/connect";

	}

	public interface account {

		/**
		 * 登陆
		 */
		public String login = "login";

	}

	public interface goods {

		/**
		 * 查询商品种类
		 */
		public String queryGoods = "queryProduct";

		public String queryGoodsCategory = "queryProductCategory";

		/**
		 * 查询商品（关键字）
		 */
		public String queryGoodsByKey = "/goods/queryGoodsByKeyword";

	}

	public interface order {

		public String createOrder = "createOrder";

		/**
		 * 非标商品的创建订单请求
		 */
		public String buyCreateOrder2 = "/order/create2";

		/**
		 * 买家订单
		 */
		public String queryBuyerOrder = "/order/queryBuyerOrder";
		/**
		 * 卖家订单
		 */
		public String querySellerOrder = "/order/querySellerOrder";

	}

}
