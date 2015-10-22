package com.gt.bmf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.gt.bmf.dao.BmfAppTokenDao;
import com.gt.bmf.pojo.BmfAppToken;

@Repository("bmfAppTokenDao")
public class BmfAppTokenDaoImpl extends BmfBaseDaoImpl<BmfAppToken> implements BmfAppTokenDao {

	@Override
	public BmfAppToken checkToken(String platform, String token) {
		String hql = "select o from BmfAppToken o where 1=1";

		List<Object> paramList = new ArrayList<Object>();

		if (StringUtils.isNotEmpty(platform)) {
			hql += " and o.platform = ? ";
			paramList.add(platform);
		}

		if (StringUtils.isNotEmpty(token)) {
			hql += " and o.token = ? ";
			paramList.add(token);
		}
		return findUnique(hql, paramList.toArray());
	}

	@Override
	public List<BmfAppToken> findListData(String platform, int offset, int limit) {
		if (StringUtils.isNotEmpty(platform)) {
			String hql = "select o from BmfAppToken o where o.platform = ? order by o.id asc";
			return findByHQLLimit(hql, offset, limit, platform);
		} else {
			return null;
		}
	}
}
