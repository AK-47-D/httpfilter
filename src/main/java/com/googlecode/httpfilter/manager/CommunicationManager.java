package com.googlecode.httpfilter.manager;

import java.util.List;

import com.googlecode.httpfilter.domain.CommunicationDO;

public interface CommunicationManager {
	
	/**
	 * ����CommunicationDO
	 * @param commt
	 * @return
	 */
	CommunicationDO createCommunication( CommunicationDO commt ) throws BizException;
	
	/**
	 * ͨ��id��ȡcomtDO
	 * @param id
	 * @return
	 */
	CommunicationDO getComtById(long id) throws BizException;
	
	/**
	 * ͨ��traceId��ȡcomtDO
	 * @param traceId
	 * @return
	 * @throws BizException
	 */
	List<CommunicationDO> getComtByTraceId(String traceId) throws BizException;
}
