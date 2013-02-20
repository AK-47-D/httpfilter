package com.googlecode.httpfilter.manager;

import java.util.List;

import com.googlecode.httpfilter.domain.ConnectionDO;

public interface ConnectionManager {

	/**
	 * ����һ��ConnectionDO
	 * @param cont
	 * @return
	 * @throws BizException
	 */
	ConnectionDO createConnection(ConnectionDO cont) throws BizException;
	
	/**
	 * ͨ��id��ȡConnectionDO
	 * @param id
	 * @return
	 * @throws BizException
	 */
	ConnectionDO getConnectionById(long id) throws BizException;
	
	/**
	 * ����traceId��minId��ȡ
	 * @param traceId
	 * @param minId
	 * @return
	 */
	List<ConnectionDO> getConnectionByComtId( long comtId, long minId ) throws BizException;
	
}
