package com.xgf.winecome.entity;

import java.io.Serializable;

public class IntegralGoods implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	private String id;

	private String desc;

	private String iconUrl;

	private String name;

	private String integral;

}
