package com.googlecode.httpfilter.dao.ibatis;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Ibatis֧����
 * @author vlinux
 *
 */
public abstract class AbstractSqlMapClientDaoSupport extends
		SqlMapClientDaoSupport {

	@Resource(name = "httpfilterSqlMapClient")
	private SqlMapClient sqlMapClient;

	/**
	  * �ڷ����ϼ���ע��@PostConstruct����������ͻ���Bean��ʼ��֮��Spring����ִ��
	  * ��ע��Bean��ʼ��������ʵ����Bean���� װ��Bean�����ԣ�����ע�룩����
	  * ����һ�����͵�Ӧ�ó����ǣ�������Ҫ��Bean��ע��һ���丸���ж�������ԣ�
	  * �������޷���д��������Ի����Ե� setter����ʱ
	  */
	@PostConstruct
	public final void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

}
