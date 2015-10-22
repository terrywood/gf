package com.gt.bmf.service.impl;

import com.gt.bmf.common.page.PageList;
import com.gt.bmf.dao.BmfBaseDao;
import com.gt.bmf.service.BmfBaseService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public abstract class BmfBaseServiceImpl<M> implements BmfBaseService<M> {

	protected BmfBaseDao<M> bmfBaseDao;

	public abstract void setBmfBaseDao(BmfBaseDao<M> bmfBaseDao);

	@Override
	public int countAll() {
		return bmfBaseDao.countAll();
	}

	@Override
	public void delete(Serializable id) {
		bmfBaseDao.delete(id);
	}

	@Override
	public void deleteObject(M model) {
		bmfBaseDao.deleteObject(model);
	}

	@Override
	public M get(Serializable id) {
		return bmfBaseDao.get(id);
	}

	@Override
	public List<M> findAll() {
		return bmfBaseDao.findAll();
	}

	@Override
	public void merge(M model) {
		bmfBaseDao.merge(model);
	}

	@Override
	public M save(M model) {
		bmfBaseDao.save(model);
		return model;
	}

	@Override
	public void saveOrUpdate(M model) {
		bmfBaseDao.saveOrUpdate(model);
	}

	@Override
	public void saveOrUpdateAll(Collection<?> list) {
		bmfBaseDao.saveOrUpdateAll(list);
	}

	@Override
	public void update(M model) {
		bmfBaseDao.update(model);
	}

	@Override
	public PageList<M> findPageData(int pageNum, int pageSize) {
		return bmfBaseDao.findPageData(pageNum, pageSize);
	}

}
