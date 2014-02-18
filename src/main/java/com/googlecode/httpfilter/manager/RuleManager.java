package com.googlecode.httpfilter.manager;

import java.util.List;

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
	
	
	/**
	 * ͨ��id��ȡcomtDO
	 * @param id
	 * @return
	 */
	List<RuleDO> getAllRules() throws BizException;
	
	/**
	 * ͨ��idɾ��comtDO
	 * @param id
	 * @return
	 */
	int removeRuleById( long id ) throws BizException;
	
}
