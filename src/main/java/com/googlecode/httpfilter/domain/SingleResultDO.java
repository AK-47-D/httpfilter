package com.googlecode.httpfilter.domain;

/**
 * ���ص������
 * @author vlinux
 *
 * @param <T>
 */
public class SingleResultDO<T> extends BaseResultDO {

	private static final long serialVersionUID = 7987375914110334082L;

	private T model; //��������ģ��
	
	public SingleResultDO() {
		//
	}
	
	public SingleResultDO(final T model) {
		this.model = model;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}
	
}
