package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.MultiResultDO;
import com.googlecode.httpfilter.domain.SingleResultDO;
import com.googlecode.httpfilter.domain.ToBeCheckDO;

public interface ToBeCheckService {

	/**
	 * ����ToBeCheckDO
	 * @param toBeCheckDO
	 * @return
	 */
	SingleResultDO<ToBeCheckDO> createToBeCheckDO( ToBeCheckDO toBeCheckDO );
	
	/**
	 * ͨ��id��ȡToBeCheckDO
	 * @param id
	 * @return
	 */
	SingleResultDO<ToBeCheckDO> getToBeCheckeDOById( long id );
	
	/**
	 * ͨ��ids��ȡToBeCheckDO
	 * @param ids
	 * @return
	 */
	MultiResultDO<Long, ToBeCheckDO> getToBeCheckDOByIds( List<Long> ids );
	
	/**
	 * ͨ��ids��ȡToBeCheckDO
	 * @param ids
	 * @return
	 */
	SingleResultDO<List<ToBeCheckDO>> getAllToBeCheckDOByVersionId( long versionId );
}
