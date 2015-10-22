package com.gt.bmf.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.gt.bmf.common.page.PageList;
import com.gt.bmf.common.page.Paginator;
import com.gt.bmf.dao.BmfBaseDao;
import com.gt.bmf.util.SqlRemoveUtils;

@SuppressWarnings("all")
public abstract class BmfBaseDaoImpl<M> implements BmfBaseDao<M> {

	private final Class<M> entityClass;
	private final String HQL_LIST_ALL;
	private final String HQL_COUNT_ALL;
	private final String HQL_LIST_COMPANY_ALL;

	public BmfBaseDaoImpl() {
		this.entityClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		HQL_LIST_ALL = "from " + this.entityClass.getSimpleName() + " order by id desc";
		HQL_COUNT_ALL = " select count(*) from " + this.entityClass.getSimpleName();
		HQL_LIST_COMPANY_ALL = "from " + this.entityClass.getSimpleName() + " e where e.companyId = ?"; // 入侵
	}

	@Autowired
	@Qualifier("bmfSessionFactory")
	private SessionFactory sessionFactory;

	public Session getSession() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return session;
	}

	@Override
	public M get(Serializable id) {
		return (M) getSession().get(this.entityClass, id);
	}

	@Override
	public M load(Serializable id) {
		return (M) getSession().load(this.entityClass, id);
	}

	@Override
	public Serializable save(M model) {
		return (Serializable) getSession().save(model);
	}

	@Override
	public void saveOrUpdate(M model) {
		getSession().saveOrUpdate(model);
	}

	@Override
	public void saveOrUpdateAll(Collection<?> list) {
		if (list != null) {
			int temp = 0;
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				if (temp % 20 == 0) {
					getSession().flush();
					getSession().clear();
				}
				getSession().saveOrUpdate(it.next());
				temp++;
			}
		}
	}

	@Override
	public void update(M model) {
		getSession().update(model);
	}

	@Override
	public void merge(M model) {
		getSession().merge(model);
	}

	@Override
	public void deleteObject(M model) {
		getSession().delete(model);
	}

	@Override
	public boolean delete(Serializable id) {
		M model = get(id);
		if (model != null) {
			getSession().delete(model);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public List<M> findAll() {
		return list(HQL_LIST_ALL);
	}

	/**
	 * 入侵
	 */
	@Override
	public List<M> findAll(Long companyId) {
		if (companyId != null) {
			return list(HQL_LIST_COMPANY_ALL, companyId);
		} else {
			return list(HQL_LIST_ALL);
		}
	}

	@Override
	public PageList<M> findPageData(int pageNum, int pageSize, Long companyId) {
		return findPageData(HQL_LIST_COMPANY_ALL, pageNum, pageSize, companyId);
	}

	@Override
	public List<M> find(String hql) {
		return list(hql);
	}

	@Override
	public List<M> find(String hql, Object... values) {
		return list(hql, values);
	}

	@Override
	public Integer count(String hql, Object... values) {
		Long total = aggregate(hql, values);
		return total.intValue();
	}

	@Override
	public Integer countAll() {
		return count(HQL_COUNT_ALL);
	}

	@Override
	public M findUnique(String hql, Object... values) {
		return aggregate(hql, values);
	}

	@Override
	public List<M> findByHQLLimit(String hql, int offset, int limit, Object... values) {
		return list(hql, offset, limit, values);
	}

	@Override
	public M findUniqueByHQLLimit(String hql, int offset, int limit, Object... values) {
		return aggregate(hql, offset, limit, values);
	}

	@Override
	public List<M> findByHQLLimit(String hql, int offset, int limit) {
		return list(hql, offset, limit, null);
	}

	@Override
	public PageList<M> findPageData(String hql, int pageNum, int pageSize, Object... values) {
		Assert.isTrue(pageSize > 0, "PageSize > 0 must be true");
		final String countHql = "SELECT count(*) " + SqlRemoveUtils.removeOrders(SqlRemoveUtils.removeSelect(SqlRemoveUtils.removeFetchKeyword((hql))));
		Integer totalCount = this.count(countHql, values);
		Paginator paginator = new Paginator(pageNum, pageSize, totalCount);
		PageList<M> pageList = new PageList<M>(paginator);
		if (totalCount != null && totalCount.intValue() > 0) {
            System.out.println("FtrProduct1 pageNum["+pageNum+"] pageSize["+pageSize+"] ");
            System.out.println("FtrProduct2 paginator.getOffset()["+paginator.getOffset()+"] totalCount["+totalCount+"]");
			List<M> list = this.list(hql, paginator.getOffset(), pageSize, values);
			pageList.addAll(list);
		}
		return pageList;
	}

	@Override
	public PageList<M> findPageData(int pageNum, int pageSize) {
		return findPageData(HQL_LIST_ALL, pageNum, pageSize);
	}

	@Override
	public List<M> findBySQL(String sql) {
		return listBySql(sql);
	}

	@Override
	public List<M> findBySQL(String sql, Object... values) {
		return listBySql(sql, values);
	}

	@Override
	public List<M> findBySQLLimit(String sql, int offset, int limit) {
		return this.listBySql(sql, offset, limit, null);
	}

	@Override
	public PageList<M> findPageDataBySQL(String sql, int pageNum, int pageSize, Object... values) {
		Assert.isTrue(pageSize > 0, "PageSize > 0 must be true");
		final String countHql = "SELECT count(*) " + SqlRemoveUtils.removeOrders(SqlRemoveUtils.removeSelect(SqlRemoveUtils.removeFetchKeyword((sql))));
		Integer totalCount = this.count(countHql, values);
		Paginator paginator = new Paginator(pageNum, pageSize, totalCount);
		PageList<M> pageList = new PageList<M>(paginator);
		if (totalCount != null && totalCount.intValue() > 0) {
			List<M> list = this.list(sql, paginator.getOffset(), pageSize, values);
			pageList.addAll(list);
		}
		return pageList;
	}

	public List<M> findBySQLLimit(String sql, int offset, int limit, Object... values) {
		return listBySql(sql, offset, limit, values);
	}

	@Override
	public M findUniqueBySQL(String sql) {
		return aggregateBySQL(sql);
	}

	@Override
	public M findUniqueBySQL(String sql, Object... values) {
		return aggregateBySQL(sql, values);
	}

	@Override
	public List<Object[]> listByRelationSQL(String sql, List<Entry<String, Class<?>>> entityList, List<Entry<String, Type>> scalarList, Object... values) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (entityList != null) {
			for (Entry<String, Class<?>> entity : entityList) {
				query.addEntity(entity.getKey(), entity.getValue());
			}
		}

		if (scalarList != null) {
			for (Entry<String, Type> entity : scalarList) {
				query.addScalar(entity.getKey(), entity.getValue());
			}
		}
		setParameters(query, values);
		return query.list();
	}

	@Override
	public Integer executeBySQL(String sql, Object... objects) {
		SQLQuery query = getSession().createSQLQuery(sql);
		setParameters(query, objects);
		return query.executeUpdate();
	}

	@Override
	public Integer executeByHQL(String hql, Object... objects) {
		Query query = getSession().createQuery(hql);
		setParameters(query, objects);
		return query.executeUpdate();
	}

	@Override
	public List<?> findOther(String hql) {
		return list(hql);
	}

	@Override
	public List<?> findOther(String hql, Object... values) {
		return list(hql, values);
	}

	@Override
	public List<?> findOtherByHQLLimit(String hql, int offset, int limit, Object... values) {
		return list(hql, offset, limit, values);
	}

	@Override
	public Object findOtherUnique(String hql, Object... values) {
		return aggregate(hql, values);
	}

	@Override
	public List<?> findOtherBySQL(String sql) {
		return listBySql(sql);
	}

	@Override
	public List<?> findOtherBySQL(String sql, Object... values) {
		return listBySql(sql, values);
	}

	@Override
	public Object findOtherUniqueBySQL(String sql) {
		return aggregateBySQL(sql);
	}

	@Override
	public Object findOtherUniqueBySQL(String sql, Object... values) {
		return aggregateBySQL(sql, values);
	}

	/**
	 * 根据查询条件返回唯一一条记录
	 */
	protected <T> T aggregate(final String hql, final Map<String, Collection<?>> map, final Object... paramlist) {
		Query query = getSession().createQuery(hql);
		if (paramlist != null) {
			setParameters(query, paramlist);
			for (Entry<String, Collection<?>> e : map.entrySet()) {
				query.setParameterList(e.getKey(), e.getValue());
			}
		}
		return (T) query.uniqueResult();
	}

	protected <T> T aggregate(final String hql, final Object... paramlist) {
		Query query = getSession().createQuery(hql);
		setParameters(query, paramlist);
		return (T) query.uniqueResult();
	}

	protected <T> T aggregateBySQL(final String sql, final Object... paramlist) {
		SQLQuery query = getSession().createSQLQuery(sql);
		setParameters(query, paramlist);
		query.addEntity(this.entityClass);
		return (T) query.uniqueResult();
	}

	protected <T> T aggregate(final String hql, final int offset, final int limit, final Object... paramlist) {
		Query query = getSession().createQuery(hql);
		setParameters(query, paramlist);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		return (T) query.uniqueResult();
	}

	/**
	 * 根据查询条件返回N条记录
	 */
	protected <T> List<T> list(final String hql, final Object... paramlist) {
		Query query = getSession().createQuery(hql);
		setParameters(query, paramlist);
		return query.list();
	}

	protected <T> List<T> list(final String hql, final int offset, final int limit, final Object... paramlist) {
		Query query = getSession().createQuery(hql);
		setParameters(query, paramlist);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		return query.list();
	}

	protected <T> List<T> listBySql(final String sql, final Object... paramlist) {
		SQLQuery query = getSession().createSQLQuery(sql);
		setParameters(query, paramlist);
		query.addEntity(this.entityClass);
		return query.list();
	}

	protected <T> List<T> listBySql(final String sql, final int offset, final int limit, final Object... paramlist) {
		SQLQuery query = getSession().createSQLQuery(sql);
		setParameters(query, paramlist);
		query.addEntity(this.entityClass);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		return query.list();
	}

	/**
	 * 设置查询参数
	 */
	protected void setParameters(Query query, Object[] paramlist) {
		if (paramlist != null) {
			for (int i = 0, z = paramlist.length; i < z; i++) {
				if (paramlist[i] instanceof Date) {
					query.setTimestamp(i, (Date) paramlist[i]);
				} else {
					query.setParameter(i, paramlist[i]);
				}
			}
		}
	}

}
