package com.googlecode.httpfilter.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.client.CachedExchange;
import org.eclipse.jetty.util.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.httpfilter.HttpFilterExchange;

/**
 * ��ѹ������
 * @author vlinux
 *
 */
public class UnCompressHttpResponseHandler implements HttpResponseHandler {

	private static final Logger logger = LoggerFactory.getLogger("httpfilter");
	
	@Override
	public boolean isHandleResponse(HttpFilterExchange exchange) {
		final String ce = exchange.getResponseFields().getStringField("Content-Encoding");
		return StringUtils.isNotEmpty(ce);
	}

	@Override
	public ResponseHandlerResult handleResponse(final HttpFilterExchange exchange, final DataBlock block)
			throws Exception {
		final String ct = getCompressType(exchange);
		logger.debug("uri={};Content-encoding={};compressType={};block-size={};resp-status={}", new Object[]{
				exchange.getRequestURI(), exchange.getResponseFields().getStringField("Content-Encoding"), ct, block.getDatas().length, exchange.getResponseStatus()});
		
		final ResponseHandlerResult result;
		final DataBlock returnDataBlock;
		if( StringUtils.equalsIgnoreCase("gzip", ct) ) {
			returnDataBlock = unCompressGZIP(block);
		} else if(StringUtils.equalsIgnoreCase("deflate", ct)){
			returnDataBlock = unCompressInflater(block);
		} else {
			returnDataBlock = block;
			
		}
		result = new ResponseHandlerResult(returnDataBlock);
		removeResponseHeader(result);
		return result;
	}

	
	/**
	 * ��ȡѹ����ʽ
	 * @param exchange
	 * @return
	 */
	private String getCompressType(final CachedExchange exchange) {
		return exchange.getResponseFields().getStringField("Content-Encoding");
	}

	/**
	 * ����gzip��ѹ����ʽ
	 * @throws IOException 
	 */
	private DataBlock unCompressGZIP(DataBlock block) throws IOException {
		ByteArrayOutputStream baos = null;
		GZIPInputStream gzipIs = null;
		try {
			gzipIs = new GZIPInputStream(new ByteArrayInputStream(block.getDatas()));
			baos = new ByteArrayOutputStream();
			IO.copy(gzipIs, baos);
			return new DataBlock(baos.toByteArray());
		} finally {
			IO.close(gzipIs);
			IO.close(baos);
		}
	}
	
	/**
	 * ����Inflater��ѹ����ʽ
	 * @param block
	 * @return
	 * @throws IOException
	 */
	private DataBlock unCompressInflater(DataBlock block) throws IOException {
		ByteArrayOutputStream baos = null;
		InflaterInputStream inflaterIs = null;
		try {
			inflaterIs = new InflaterInputStream(new ByteArrayInputStream(block.getDatas()));
			baos = new ByteArrayOutputStream();
			IO.copy(inflaterIs, baos);
			return new DataBlock(baos.toByteArray());
		} finally {
			IO.close(inflaterIs);
			IO.close(baos);
		}
	}
	
	
	/**
	 * Ĩ��Ӧ��ͷ���й��ڱ����ʽ���ֵı��
	 * @param result
	 */
	private void removeResponseHeader(final ResponseHandlerResult result) {
		result.removeHeader("Content-Encoding");
		result.removeHeader("Content-Length");
	}

}
