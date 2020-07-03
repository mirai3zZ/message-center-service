package com.yangyang.cloud.common.base;

import java.util.List;


public interface BaseMapper<T> {
	
	public void add(T t);
	
	public void update(T t);
	
	public void updateBySelective(T t); 	
	
	public void delete(Object id);

	public int queryByCount(T t);
	
	public List<T> queryByListForpage(T t);

	public T queryById(Object id);

	public void batchAdd(List list);
	
	public void batchDelete(List<T> list);
	
	public void deleteByCondtion(T t);

}
