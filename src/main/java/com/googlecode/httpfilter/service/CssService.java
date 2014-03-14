package com.googlecode.httpfilter.service;

import java.util.List;

import com.googlecode.httpfilter.domain.CssDO;
import com.googlecode.httpfilter.domain.SingleResultDO;

public interface CssService {

	/**
	 * ����CSSDO
	 * @param cssDO
	 * @return
	 */
	SingleResultDO<CssDO> createCssDO( CssDO cssDO );
	
	/**
	 * ͨ��id��ȡCSSDO
	 * @param id
	 * @return
	 */
	SingleResultDO<CssDO> getCssDOById(long id);
	
	/**
	 * ͨ������Id��ȡCSSDO �б�
	 * @param itemId
	 * @return
	 */
	SingleResultDO<List<CssDO>> getCssDOByItemId(long itemId); 
}
