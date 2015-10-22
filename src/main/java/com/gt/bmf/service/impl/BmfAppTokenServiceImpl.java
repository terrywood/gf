package com.gt.bmf.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gt.bmf.BmfConstants;
import com.gt.bmf.dao.BmfAppTokenDao;
import com.gt.bmf.dao.BmfBaseDao;
import com.gt.bmf.pojo.BmfAppToken;
import com.gt.bmf.service.BmfAppTokenService;

@Service("bmfAppTokenService")
public class BmfAppTokenServiceImpl extends BmfBaseServiceImpl<BmfAppToken> implements BmfAppTokenService {

	private static Logger logger = Logger.getLogger("BmfPushMessageServiceImpl");

	private BmfAppTokenDao bmfAppTokenDao;



	@Autowired
	@Qualifier("bmfAppTokenDao")
	@Override
	public void setBmfBaseDao(BmfBaseDao<BmfAppToken> bmfBaseDao) {
		logger.info("BmfAppTokenService is ready");
		this.bmfBaseDao = bmfBaseDao;
		this.bmfAppTokenDao = (BmfAppTokenDao) bmfBaseDao;
	}

    @Override
    public void send(List<BmfAppToken> appTokenList, String message, String platform) {

    }

    @Override
	public BmfAppToken checkToken(String platform, String token) {
		return bmfAppTokenDao.checkToken(platform, token);
	}



	@Override
	public void update(BmfAppToken e) {
		bmfAppTokenDao.update(e);
	}

	@Override
	public void saveOrUpdate(BmfAppToken e) {
		bmfAppTokenDao.saveOrUpdate(e);
	}

	@Override
	public List<BmfAppToken> findListData(String platform, int offset, int limit) {
		return bmfAppTokenDao.findListData(platform, offset, limit);
	}

}
