package com.gt.bmf.vo;

import java.util.List;

public class BmfPushMessageVo {

	private List<String> tokenList;

	private String message;

	public BmfPushMessageVo() {
	}

	public BmfPushMessageVo(List<String> tokenList, String message) {
		this.tokenList = tokenList;
		this.message = message;
	}

	public List<String> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<String> tokenList) {
		this.tokenList = tokenList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}