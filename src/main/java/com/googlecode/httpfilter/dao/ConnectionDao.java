package com.googlecode.httpfilter.dao;

import java.sql.SQLException;
import java.util.List;

import com.googlecode.httpfilter.domain.ConnectionDO;

public interface ConnectionDao {

	/**
	 * ����ConnectionDO
	 * @param cont
	 * @return
	 * @throws SQLException
	 */
	long createConnection(ConnectionDO cont) throws SQLException;
	
	/**
	 * ����id��ѯConnectionDO
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	ConnectionDO getConnectionById(long id) throws SQLException;
	
	/**
	 * ����traceId��minId��ȡConnectionDO
	 * @param traceId
	 * @param minId
	 * @return
	 * @throws SQLException
	 */
	List<ConnectionDO> getConnectionByComtId( long comtId, long minId ) throws SQLException;
}
