package com.gt.bmf.service;

import java.util.List;

import com.gt.bmf.pojo.BmfAppToken;

public interface BmfAppTokenService extends BmfBaseService<BmfAppToken> {

	public void send(List<BmfAppToken> appTokenList, String message, String platform);

	public BmfAppToken checkToken(String platform, String token);

	public List<BmfAppToken> findListData(String platform, int offset, int limit);
}
