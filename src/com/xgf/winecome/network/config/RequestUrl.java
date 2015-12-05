package com.xgf.winecome.network.config;

/**
 * remote request url
 */
public class RequestUrl {

	// public static final String NAMESPACE = "http://www.diyifw.com";
	//
	// public static final String HOST_URL =
	// "http://www.diyifw.com:8080/jll/services/JllService";

	public static final String NAMESPACE = "http://139.196.15.154";

	public static final String HOST_URL = "http://139.196.15.154/jll/services/JllService";

	// public static final String NAMESPACE_TEST = "http://139.196.15.154";
	//
	// public static final String HOST_URL_TEST =
	// "http://139.196.15.154/jll/services/JllService";

	// public static final String HOST_URL =
	// "http://192.168.1.101:8080/com.igou.server";

	public interface promotion {
		/**
		 * 根据分类查询促销信息
		 */
		public String queryPromotionV2 = "queryPromotionV2";

		/**
		 * 根据id查询促销信息
		 */
		public String getPromotionProductsV2 = "getPromotionProductsV2";
	}

	public interface app {
		/**
		 * 连接 获取最新的版本号
		 */
		public String getVersion = "version";

	}

	public interface msg {
		/**
		 * 连接 获取推送
		 */
		public String pushMsg = "pushMsg";

		/**
		 * 获取消息历史
		 */
		public String queryMessage = "queryMessage";
	}

	public interface account {

		/**
		 * 登陆
		 */
		public String login = "login";

		/**
		 * 验证码
		 */
		public String sendAuthCode = "sendAuthCode";

	}

	public interface goods {

		/**
		 * 查询全部商品
		 */
		public String queryAllGoods = "queryAllProductV2";

		/**
		 * 查询商品种类
		 */
		public String queryGoods = "queryProduct";

		public String queryGoodsCategory = "queryProductCategory";

		/**
		 * 查询商品（关键字）
		 */
		public String queryGoodsByKey = "/goods/queryGoodsByKeyword";

		/**
		 * 查询促销酒
		 */
		public String queryPromProduct = "queryPromProduct";

		/**
		 * 查询促销酒
		 */
		public String authProduct = "authProduct";

		/**
		 * banner
		 */
		public String promotionPage = "promotionPage";
	}

	public interface integral {

		/**
		 * 查询全部积分商品
		 */
		public String queryAllOnIntegralMall = "queryAllOnIntegralMall";

		/**
		 * 根据条件查询积分商品
		 */
		public String queryProductOnIntegralMall = "queryProductOnIntegralMall";

		/**
		 * 积分兑换
		 */
		public String exchange = "exchangeV2";

		/**
		 * 积分兑换历史
		 */
		public String queryIntegralConsume = "queryIntegralConsume";

		/**
		 * 积分订单
		 */
		public String queryExchangeOrder = "queryExchangeOrder";

		/**
		 * 获取总积分
		 */
		public String queryTotal = "getIntegral";

	}

	public interface order {

		public String createOrder = "createOrder";

		/**
		 * 查询订单
		 */
		public String queryOrders = "queryOrder";

		/**
		 * 取消订单
		 */
		public String cancelOrder = "cancelOrder";

		/**
		 * 设置支付方式
		 */
		public String setOrderPayType = "payWay";

		/**
		 * 设置预支付方式
		 */
		public String setOrderPrePayType = "prePayWay";

		/**
		 * 支付结果确认
		 */
		public String payResultCheck = "payResultCheck";

		/**
		 * 银联流水号获取
		 */
		public String getUnionPayTn = "getTn";

	}

	public interface comment {

		public String commentOrder = "commentOrder";

	}

}
