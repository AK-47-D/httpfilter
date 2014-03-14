package com.googlecode.httpfilter.dao;

import java.sql.SQLException;

import com.googlecode.httpfilter.domain.FilterDO;

public interface FilterDao {
	
	/**
	 * ����filterDO
	 * @param filter
	 * @return
	 */
	long createFilterDO(FilterDO filter) throws SQLException;
	/**
	 * ͨ��id��ȡFilterDO
	 * @param id
	 * @return
	 */
	FilterDO getFilterDOById(long id) throws SQLException;

}
