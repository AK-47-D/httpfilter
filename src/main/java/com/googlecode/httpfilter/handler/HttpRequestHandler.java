package com.googlecode.httpfilter.handler;

import com.googlecode.httpfilter.HttpFilterExchange;

/**
 * ��������
 * @author vlinux
 *
 */
public interface HttpRequestHandler {

	/**
	 * �ж��Ƿ��ܴ���HTTPӦ��
	 * @param exchange
	 * @return
	 */
	boolean isHandleRequest(final HttpFilterExchange exchange);
	
	/**
	 * �������������������
	 * @param exchange
	 * @return
	 * @throws Exception
	 */
	RequestHandlerResult handleRequest(final HttpFilterExchange exchange) throws Exception;
	
}
