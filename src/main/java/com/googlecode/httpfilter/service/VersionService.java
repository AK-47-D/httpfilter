package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.MultiResultDO;
import com.googlecode.httpfilter.domain.SingleResultDO;
import com.googlecode.httpfilter.domain.VersionDO;

public interface VersionService {

	/**
	 * ���� VersionDO
	 * @param version
	 * @return
	 */
	SingleResultDO<VersionDO> createVersionDO( VersionDO version );
	
	/**
	 * ͨ��Id��ȡVersionDO
	 * @param id
	 * @return
	 */
	SingleResultDO<VersionDO> getVersionDOById( long id );
	
	/**
	 * ͨ��Ids��ȡVersionDO list
	 * @param ids
	 * @return
	 */
	MultiResultDO<Long, VersionDO> getVersionDOByIds( List<Long> ids );
}
