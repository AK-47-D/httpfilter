package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.FilterDO;
import com.googlecode.httpfilter.domain.MultiResultDO;
import com.googlecode.httpfilter.domain.SingleResultDO;

public interface FilterService {

	/**
	 * �������洢FilterDO
	 * @param filterDO
	 * @return
	 */
	SingleResultDO<FilterDO> createFilterDO(FilterDO filterDO);
	
	/**
	 * ͨ��id��ȡFilterDO
	 * @param id
	 * @return
	 */
	SingleResultDO<FilterDO> getFilterById(long id);
	
	/**
	 * ͨ��id list��ȡ filterDO�б�
	 * @param ids
	 * @return
	 */
	MultiResultDO<Long,FilterDO> searchFiltersByIds( List<Long> ids );
}
