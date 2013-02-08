package com.googlecode.httpfilter.handler;

import org.apache.commons.lang.ArrayUtils;
import org.jsoup.nodes.Document;

import com.googlecode.httpfilter.HttpFilterExchange;
import com.googlecode.httpfilter.mime.MIME;
import com.googlecode.httpfilter.util.HttpUtils;

/**
 * firebug-lite��ǿ<br/>
 * ��Ҫ�ڵ�ַ��������http://www.baidu.com/?__httpfilter_firebug=true<br/>
 * @author vlinux
 *
 */
public class FirebugEmbedHttpResponseHander implements HttpResponseHandler {

	@Override
	public boolean isHandleResponse(HttpFilterExchange exchange) {
		final String mime = HttpUtils.getMIME(exchange.getResponseFields());
		return MIME.isHtml(mime)
				&& isEnableFirebug(exchange);
	}

	@Override
	public ResponseHandlerResult handleResponse(HttpFilterExchange exchange,
			DataBlock block) throws Exception {
		
		// ֻ��html�����Ҫֲ��
		if( null == block
				|| !(block instanceof HtmlBlock) ) {
			return new ResponseHandlerResult(block);
		}
		
		/*
		 * Include the following code at the top of the <head> of your page:
		 */
		final HtmlBlock htmlBlock = (HtmlBlock)block;
		final Document doc = htmlBlock.getDocument();
		doc.select("head").first().prepend("<script type='text/javascript' src='https://getfirebug.com/firebug-lite.js'></script>");
		doc.select("html").attr("debug", "true");
		return new ResponseHandlerResult(new HtmlBlock(doc.html(), htmlBlock.getCharset()));
	}
	
	/**
	 * �Ƿ�������firebug
	 * @param exchange
	 * @return
	 */
	private boolean isEnableFirebug(HttpFilterExchange exchange) {
		final String uri = exchange.getRequestURI();
		return  ArrayUtils.contains(HttpUtils.parseRequestParamters(uri).get("__httpfilter_firebug"), "true");
	}

}
