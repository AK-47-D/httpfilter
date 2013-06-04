package com.googlecode.httpfilter.dao;

import java.sql.SQLException;

import com.googlecode.httpfilter.domain.VersionDO;

public interface VersionDao {

	/**
	 * ����VersionDO
	 * @param version
	 * @return
	 */
	long createVersionDO( VersionDO version ) throws SQLException;
	
	/**
	 * ͨ��ID��ȡVersionDO
	 * @param id
	 * @return
	 */
	VersionDO getVersionDOById( long id ) throws SQLException;
}
