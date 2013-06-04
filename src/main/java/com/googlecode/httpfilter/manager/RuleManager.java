package com.googlecode.httpfilter.manager;

import com.googlecode.httpfilter.domain.RuleDO;

public interface RuleManager {

	/**
	 * ����CommunicationDO
	 * @param commt
	 * @return
	 */
	RuleDO createRule( RuleDO rule ) throws BizException;
	
	/**
	 * ͨ��id��ȡcomtDO
	 * @param id
	 * @return
	 */
	RuleDO getRuleById( long id ) throws BizException;
	
}
