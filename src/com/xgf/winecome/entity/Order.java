package com.xgf.winecome.entity;

import java.io.Serializable;

public class Order implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	public enum OrderState {
		/**
		 * 订单已创建(对应买家下单)
		 */
		created("创建"),

		/**
		 * 卖家已抢单(对应卖家抢单)
		 */
		challenged("已成交"),

		/**
		 * 货物已投递(对应卖家订单确认)
		 */
		delivered("已送达"),

		/**
		 * 交易完成，对应买家订单确认
		 */
		success("已确认"),

		/**
		 * 订单超时，未交易成功，对应30分钟内没有卖家抢单， 交易失败
		 */
		timeout("超时");

		String state;

		OrderState(String state) {
			this.state = state;
		}

		public String getState() {
			return state;
		}
	}

	private String id;

	private String goodsName;

	private String goodsId;

	private String goodsBrief;

	private String goodsPrice;

	private String goodsImgUrl;

	private String buyNum;

	private String buyUserName;

	private String buyAddress;

	private String phone;

	private String buyPrice;

	private String latitude;

	private String longitude;

	private String totalPrice;

	private String probablyWaitTime;

	private String time;

	private String state;

	private String invoice;

	private String invoiceTitle;

	private String invoiceContent;

	private String payType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsBrief() {
		return goodsBrief;
	}

	public void setGoodsBrief(String goodsBrief) {
		this.goodsBrief = goodsBrief;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsImgUrl() {
		return goodsImgUrl;
	}

	public void setGoodsImgUrl(String goodsImgUrl) {
		this.goodsImgUrl = goodsImgUrl;
	}

	public String getBuyUserName() {
		return buyUserName;
	}

	public void setBuyUserName(String buyUserName) {
		this.buyUserName = buyUserName;
	}

	public String getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(String buyNum) {
		this.buyNum = buyNum;
	}

	public String getBuyAddress() {
		return buyAddress;
	}

	public void setBuyAddress(String buyAddress) {
		this.buyAddress = buyAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getProbablyWaitTime() {
		return probablyWaitTime;
	}

	public void setProbablyWaitTime(String probablyWaitTime) {
		this.probablyWaitTime = probablyWaitTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
