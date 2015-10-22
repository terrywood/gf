package com.gt.bmf.vo;

import com.gt.bmf.BmfConstants;

public class SelectItemVo {
	private String code;
	private String value;
	private String selected = BmfConstants.GLOBAL_NO;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}

}
