package com.googlecode.httpfilter.domain;

/**
 * ���ؽ������
 * @author vlinux
 *
 */
public class BaseResultDO extends BaseDO {

	private static final long serialVersionUID = 1978227428619439353L;

	private boolean success = true; // Ĭ�ϳɹ�

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
