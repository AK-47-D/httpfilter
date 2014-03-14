package com.googlecode.httpfilter.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * �������ض���
 * @author vlinux
 *
 * @param <T>
 */
public class MultiResultDO<K,V> extends BaseResultDO {

	private static final long serialVersionUID = -2000834975633519075L;

	/*
	 * ����ģ��
	 */
	private Map<K,V> models = new HashMap<K,V>();
	
	/*
	 * ������Ϣ
	 * Map<KEY,ErrMsg>
	 */
	private Map<K, ErrMsg> errMsgs = new HashMap<K, ErrMsg>();

	public Map<K, V> getModels() {
		return models;
	}

	public void setModels(Map<K, V> models) {
		this.models = models;
	}

	public Map<K, ErrMsg> getErrMsgs() {
		return errMsgs;
	}

	public void setErrMsgs(Map<K, ErrMsg> errMsgs) {
		this.errMsgs = errMsgs;
	}
	
	public void putError(K key, String errorCode, Object... args) {
		ErrMsg errMsg = errMsgs.get(key);
		if( null == errMsg ) {
			errMsg = new ErrMsg();
			errMsgs.put(key, errMsg);
		}
		errMsg.putError(errorCode, args);
	}
	
}
