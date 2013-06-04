package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.MultiResultDO;
import com.googlecode.httpfilter.domain.RuleDO;
import com.googlecode.httpfilter.domain.SingleResultDO;

public interface RuleService {

	/**
	 * �����ȶԹ���
	 * @param ruleDO
	 * @return
	 */
	SingleResultDO<RuleDO> createRuleDO(RuleDO ruleDO);
	
	/**
	 * ͨ��id��ȡRuleDO
	 * @param id
	 * @return
	 */
	SingleResultDO<RuleDO> getRuleById(long id);
	
	/**
	 * ͨ��id list��ȡ RuleDO�б�
	 * @param ids
	 * @return
	 */
	MultiResultDO<Long,RuleDO> searchRulesByIds( List<Long> ids );
}
