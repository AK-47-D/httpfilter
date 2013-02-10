package com.googlecode.httpfilter.proxy.handler;


/**
 * Ӧ������
 * @author vlinux
 *
 */
public class ResponseHandlerResult extends HandlerResult {

	private final DataBlock block;
	
	public ResponseHandlerResult(final DataBlock block) {
		this.block = block;
	}

	/**
	 * ��ȡ���ݿ�
	 * @return
	 */
	public DataBlock getBlock() {
		return block;
	}

}
