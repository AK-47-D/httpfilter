package com.googlecode.httpfilter.proxy.handler;

import com.googlecode.httpfilter.proxy.HttpFilterExchange;

/**
 * HTTPӦ����
 * @author vlinux
 *
 */
public interface HttpResponseHandler {

	/**
	 * �ж��Ƿ��ܴ���HTTPӦ��
	 * @param exchange
	 * @return
	 */
	boolean isHandleResponse(final HttpFilterExchange exchange);
	
	/**
	 * ����HTTPӦ��
	 * @param exchange
	 * @param block
	 * @return
	 * @throws Exception
	 */
	ResponseHandlerResult handleResponse(
			final HttpFilterExchange exchange,
			final DataBlock block) throws Exception;
	
}
