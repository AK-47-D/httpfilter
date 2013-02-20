package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.CommunicationDO;
import com.googlecode.httpfilter.domain.SingleResultDO;

public interface CommunicationService {
	/**
	 * ����traceId�����Ự������CommunicationDO
	 * @param traceId
	 * @return
	 */
	public SingleResultDO<CommunicationDO> createCommunication( CommunicationDO commtDO );

	/**
	 * ����traceId��ȡCommunicationDO list
	 * @param traceId
	 * @return
	 */
	public SingleResultDO<List<CommunicationDO>> getCommunication( String traceId );
	/**
	 * ����Id��ȡCommunicationDO
	 * @param id
	 * @return
	 */
	public SingleResultDO<CommunicationDO> getCommunication( long id );
	
	/**
	 * ��ѯcommunication�����û�д���һ���µ�
	 * @param traceId
	 * @return
	 */
	SingleResultDO<List<CommunicationDO>> fetchComtByTraceId(String traceId);
	
}
