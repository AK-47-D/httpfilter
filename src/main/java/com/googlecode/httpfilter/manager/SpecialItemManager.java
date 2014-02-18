package com.googlecode.httpfilter.manager;

import java.util.List;

import com.googlecode.httpfilter.domain.ItemDO;

public interface SpecialItemManager {

	/**
	 * ����csv�ļ������г�����������Ʒ��Ϣ
	 * @param specialCSV �ļ���,��ʽ:xxx.csv
	 * @return
	 */
//	List<ItemDO> listForSpecials(String specialCSV);
	
	/**
	 * ����csv�ļ������г�����������Ʒ��Ϣ
	 * @param specialCSV �ļ���,��ʽ:xxx.csv
	 * @return
	 */
	List<ItemDO> listForSpecialsWithStyle(int style);
	/**
	 * ��ȡ����url
	 * @return
	 */
	String queryDataUrl();
}
