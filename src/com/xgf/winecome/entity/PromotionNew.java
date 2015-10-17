package com.xgf.winecome.entity;

import java.io.Serializable;

public class PromotionNew implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String promotionId;
	
	private String startTime;

	private String detailImg;
	
	private String title;

	private String thumbnail;
	
	private String desc;

	private String status;
	
	private String endTime;
	
	private String displayPlace;

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getDetailImg() {
		return detailImg;
	}

	public void setDetailImg(String detailImg) {
		this.detailImg = detailImg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDisplayPlace() {
		return displayPlace;
	}

	public void setDisplayPlace(String displayPlace) {
		this.displayPlace = displayPlace;
	}

}
