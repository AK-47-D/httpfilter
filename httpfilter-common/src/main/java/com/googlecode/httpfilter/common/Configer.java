package com.googlecode.httpfilter.common;

/**
 * ������Ϣ
 * 
 * @author vlinux
 * 
 */
public class Configer {

	private final static Configer configer = new Configer();
	
	/*
	 * firebug-lite��·��
	 */
	private String firebugPath;
	
	public static Configer getDefault() {
		return configer;
	}

	/**
	 * ����firebug·��
	 * @param firebugPath
	 */
	public void setFirebugPath(String firebugPath) {
		this.firebugPath = firebugPath;
	}
	
	/**
	 * ��ȡfirebug·��
	 * @return
	 */
	public String getFirebugPath() {
		return firebugPath;
	}

}
