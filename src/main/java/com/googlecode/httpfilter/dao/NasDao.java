package com.googlecode.httpfilter.dao;

import java.sql.SQLException;
import java.util.List;

import com.googlecode.httpfilter.domain.NasDO;

/**
 * Nas DAO
 * @author vlinux
 *
 */
public interface NasDao {

	/**
	 * ����һ��Nas�洢
	 * @param Nas
	 * @return nasId
	 * @throws SQLException
	 */
	long createNas(NasDO Nas) throws SQLException;
	
	/**
	 * ����ID��ȡһ��Nas�洢
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	NasDO getNasById(long id) throws SQLException;
	
	/**
	 * ����BatchNo��ȡһ��Nas�洢
	 * @param batchNo
	 * @return
	 * @throws SQLException
	 */
	List<NasDO> listNasByBatchNo(String batchNo) throws SQLException;
	
}
