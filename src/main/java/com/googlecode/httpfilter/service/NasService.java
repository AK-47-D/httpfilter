package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.MultiResultDO;
import com.googlecode.httpfilter.domain.NasDO;
import com.googlecode.httpfilter.domain.SingleResultDO;

/**
 * Nas�����
 * @author vlinux
 *
 */
public interface NasService {

	/**
	 * ����һ��NAS�洢
	 * @param batchNas
	 * @return �洢����
	 */
	SingleResultDO<String/*BatchNo*/> createBatchNas(List<NasDO> batchNas);
	
	/**
	 * �г�ͳһ�����µ�NAS�洢
	 * @param batchNo
	 * @return
	 */
	MultiResultDO<NasDO> listNasByBatchNo(String batchNo);
	
}
