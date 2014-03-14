package com.googlecode.httpfilter.dao;

import java.sql.SQLException;
import java.util.List;

import com.googlecode.httpfilter.domain.CommunicationDO;

public interface CommunicationDao {

	/**
	 * �����洢CommunicationDO
	 * @param Nas
	 * @return
	 * @throws SQLException
	 */
	long createComt(CommunicationDO comtDO) throws SQLException;
	
	/**
	 * ͨ��ID��ȡComtDO
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	CommunicationDO getComtById(long id) throws SQLException;
	
	/**
	 * ͨ��trace_id��ȡComtDO
	 * @param traceId
	 * @return
	 * @throws SQLException
	 */
	List<CommunicationDO> getComtByTraceId(String traceId) throws SQLException;
}
