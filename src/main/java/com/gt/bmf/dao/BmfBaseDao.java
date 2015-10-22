package com.gt.bmf.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import org.hibernate.type.Type;

import com.gt.bmf.common.page.PageList;

public interface BmfBaseDao<M> {

	public M get(Serializable id);

	public M load(Serializable id);

	public Serializable save(M model);

	public void saveOrUpdate(M model);

	public void saveOrUpdateAll(Collection<?> list);

	public void update(M model);

	public void merge(M model);

	public void deleteObject(M model);

	public boolean delete(Serializable id);

	public void flush();

	public void clear();

	public List<M> findAll();

	public List<M> findAll(Long companyId);

	public List<M> find(String hql);

	public List<M> find(String hql, Object... values);

	public Integer count(String hql, Object... values);

	public Integer countAll();

	public M findUnique(String hql, Object... values);

	public List<M> findByHQLLimit(String hql, int offset, int limit, Object... values);

	public M findUniqueByHQLLimit(String hql, int offset, int limit, Object... values);

	public List<M> findByHQLLimit(String hql, int offset, int limit);
	
	public PageList<M> findPageData(int pageNum, int pageSize);
	
	public PageList<M> findPageData(int pageNum, int pageSize, Long companyId);

	public PageList<M> findPageData(String hql, int pageNum, int pageSize, Object... values);

	public List<M> findBySQL(String sql);

	public List<M> findBySQL(String sql, Object... values);

	public List<M> findBySQLLimit(String sql, int offset, int limit);

	public List<M> findBySQLLimit(String sql, int offset, int limit, Object... values);

	public PageList<M> findPageDataBySQL(String sql, int pageNum, int pageSize, Object... values);

	public M findUniqueBySQL(String sql);

	public M findUniqueBySQL(String sql, Object... values);

	public List<Object[]> listByRelationSQL(String sql, List<Entry<String, Class<?>>> entityList, List<Entry<String, Type>> scalarList, Object... values);

	public Integer executeBySQL(String sql, Object... objects);

	public Integer executeByHQL(String hql, Object... objects);

	public List<?> findOther(String hql);

	public List<?> findOther(String hql, Object... values);

	public List<?> findOtherByHQLLimit(String hql, int offset, int limit, Object... values);

	public Object findOtherUnique(String hql, Object... values);

	public List<?> findOtherBySQL(String sql);

	public List<?> findOtherBySQL(String sql, Object... values);

	public Object findOtherUniqueBySQL(String sql);

	public Object findOtherUniqueBySQL(String sql, Object... values);

}
