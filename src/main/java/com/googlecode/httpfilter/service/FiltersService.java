package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.FilterDO;
import com.googlecode.httpfilter.domain.FiltersDO;
import com.googlecode.httpfilter.domain.SingleResultDO;

public interface FiltersService {

	SingleResultDO<FiltersDO> createFilters( FiltersDO filters );
	
	SingleResultDO<FiltersDO> getFiltersById( long id );
	
	/**
	 * ���������ϣ�filterIds�ֶδ���˹�������ID���ö��Ÿ���
	 * @param filters
	 * @return
	 */
	List<FilterDO> filersDOToFliterList( FiltersDO filters );
	
	FiltersDO filterListToFiltersDO( List<FilterDO> filterList );
}
