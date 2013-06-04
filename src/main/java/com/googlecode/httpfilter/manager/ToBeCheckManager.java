package com.googlecode.httpfilter.manager;

import com.googlecode.httpfilter.domain.ToBeCheckDO;

public interface ToBeCheckManager {

	/**
	 * ���� ToBeCheckDO
	 * @param toBeCheckDO
	 * @return
	 */
	ToBeCheckDO createToBeCheckDO( ToBeCheckDO toBeCheckDO ) throws BizException;
	
	/**
	 * ��ѯToBeCheckDO
	 * @param id
	 * @return
	 */
	ToBeCheckDO getToBeCheckDOById( long id ) throws BizException;
	
}
