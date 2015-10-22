package com.gt.bmf.dao;

import java.util.List;

import com.gt.bmf.pojo.BmfAppToken;

public interface BmfAppTokenDao extends BmfBaseDao<BmfAppToken> {

	public List<BmfAppToken> findListData(String platform, int offset, int limit);

	public BmfAppToken checkToken(String platform, String token);
}
