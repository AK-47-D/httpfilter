package com.googlecode.httpfilter.proxy.handler;

/**
 * ���ݿ�
 * @author vlinux
 *
 */
public class DataBlock {

	// ���ݴ洢��
	private final byte[] datas;
	
	public DataBlock(byte[] datas) {
		this.datas = datas;
	}

	/**
	 * ��ȡ���ݿ��е�����
	 * @return
	 */
	public byte[] getDatas() {
		return datas.clone();
	}
	
}
