package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.ConnectionDO;
import com.googlecode.httpfilter.domain.SingleResultDO;

public interface ConnectionService {

	/**
	 * �����Ự
	 * @param conntDO
	 * @return
	 */
	public SingleResultDO<ConnectionDO> createConnectionDO( ConnectionDO conntDO );
	
	/**
	 * ����ComtId��minContID��СConnectionId��ȡ��������
	 * @param traceId
	 * @return
	 */
	 SingleResultDO<List<ConnectionDO>> getConnectionByComtId(long comtId, long minContID);
	
	/**
	 * ����ComtId��ȡ��������
	 * @param traceId
	 * @return
	 */
	SingleResultDO<List<ConnectionDO>> getConnectionByComtId(long comtId);

	/**
	 * ����traceId��minContID��СConnectionId��ȡ��������
	 * @param traceId
	 * @return
	 */
	 SingleResultDO<List<ConnectionDO>> getConnectionByTraceId(String traceId, long minContID);
	
	/**
	 * ����traceId��ȡ��������
	 * @param traceId
	 * @return
	 */
	SingleResultDO<List<ConnectionDO>> getConnectionByTraceId(String traceId);
	
	/**
	 * ����Id��ȡ��������
	 * @param Id
	 * @return
	 */
	public SingleResultDO<ConnectionDO> getConnectionById(long id);
	
	
	
}
