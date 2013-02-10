package com.googlecode.httpfilter.manager;

import java.util.List;

import com.googlecode.httpfilter.domain.NasDO;

/**
 * Nas Manager
 * @author vlinux
 *
 */
public interface NasManager {

	/**
	 * ����Nas�洢
	 * @param nas
	 * @return ��������Nas
	 * @throws BizException
	 */
	NasDO createNas(NasDO nas) throws BizException;
	
	/**
	 * ����ID��ȡNas�洢
	 * @param id
	 * @return Nas�洢
	 * @throws BizException
	 */
	NasDO getNasById(long id) throws BizException;
	
	/**
	 * �������Ų�ѯһ��Nas�洢
	 * @param batchNo
	 * @return
	 * @throws BizException
	 */
	List<NasDO> listNasByBatchNo(String batchNo) throws BizException;
	
	/**
	 * ����Nas�洢����
	 * @return
	 */
	String generateBatchNo();
	
}
