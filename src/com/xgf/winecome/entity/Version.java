package com.xgf.winecome.entity;

import java.io.Serializable;

public class Version implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	/**
	 * 版本说明
	 */
	private String remark;

	/**
	 * 版本代号
	 */
	private String code;
	
	private String versionName;
	
	/**
	 * Y 强制升级
	 * N 非强制升级
	 */
	private String force;
	
	/**
	 * 升级版本路径
	 */
	private String url;
	
	/**
	 * 版本号
	 */
	private String version;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getForce() {
		return force;
	}

	public void setForce(String force) {
		this.force = force;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
