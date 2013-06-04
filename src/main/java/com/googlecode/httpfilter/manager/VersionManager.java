package com.googlecode.httpfilter.manager;

import com.googlecode.httpfilter.domain.VersionDO;

public interface VersionManager {

	/**
	 * ����versionDo
	 * @param version
	 * @return
	 */
	VersionDO createVersionDO( VersionDO version ) throws BizException;
	/**
	 * ͨ��Id��ȡversionDO
	 * @param id
	 * @return
	 */
	VersionDO getVersionDO( long id ) throws BizException;
}
