package com.gt.bmf.service;

import com.gt.bmf.common.page.PageList;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BmfBaseService<M> {

	public M save(M model);

	public void saveOrUpdate(M model);

	public void saveOrUpdateAll(Collection<?> list);

	public void update(M model);

	public void merge(M model);

	public void delete(Serializable id);

	public void deleteObject(M model);

	public M get(Serializable id);

	public int countAll();

	public List<M> findAll();

	public PageList<M> findPageData(int pageNum, int pageSize);

}
