package com.googlecode.httpfilter.common.utils;

/**
 * ������Ϣ
 * 
 * @author vlinux
 * 
 */
public class Configer {

	// HttpServer�˿ں�
	private int httpServerPort;

	// HttpServer webapp·��
	private String httpServerWebapp;

	// HttpServer context·��
	private String httpServerContext;

	// Proxy�����ļ�·��
	private String proxyConfig;

	/**
	 * ����Proxy�����ļ�·��
	 * @return
	 */
	public String getProxyConfig() {
		return proxyConfig;
	}

	/**
	 * ����Proxy�����ļ�·��
	 * @param proxyConfig
	 */
	public void setProxyConfig(String proxyConfig) {
		this.proxyConfig = proxyConfig;
	}

	/**
	 * ��ȡHttpServer context·��
	 * 
	 * @return
	 */
	public String getHttpServerContext() {
		return httpServerContext;
	}

	/**
	 * ����HttpServer context
	 * 
	 * @param httpServerContext
	 */
	public void setHttpServerContext(String httpServerContext) {
		this.httpServerContext = httpServerContext;
	}

	/**
	 * ��ȡHttpServer webapp·��
	 * 
	 * @return
	 */
	public String getHttpServerWebapp() {
		return httpServerWebapp;
	}

	/**
	 * ����HttpServer webapp·��
	 * 
	 * @param httpServerWebapp
	 */
	public void setHttpServerWebapp(String httpServerWebapp) {
		this.httpServerWebapp = httpServerWebapp;
	}

	/**
	 * ��ȡHttpServer�˿�
	 * 
	 * @return
	 */
	public int getHttpServerPort() {
		return httpServerPort;
	}

	/**
	 * ����HttpServer�˿�
	 * 
	 * @param httpServerPort
	 */
	public void setHttpServerPort(int httpServerPort) {
		this.httpServerPort = httpServerPort;
	}

}
