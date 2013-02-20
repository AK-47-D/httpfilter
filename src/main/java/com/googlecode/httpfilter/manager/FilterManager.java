package com.googlecode.httpfilter.manager;

import com.googlecode.httpfilter.domain.FilterDO;

public interface FilterManager {
	
	/**
	 * ����FilterDO
	 * @param filterDO
	 * @return
	 */
	FilterDO createFilterDO(FilterDO filterDO) throws BizException;
	
	/**
	 * ͨ��id��ѯFilterDO
	 * @param id
	 * @return
	 */
	FilterDO getFilterDOById( long id ) throws BizException;

}
