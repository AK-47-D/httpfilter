package com.googlecode.httpfilter.proxy.handler;

import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.httpfilter.proxy.HttpFilterExchange;
import com.googlecode.httpfilter.proxy.mime.MIME;
import com.googlecode.httpfilter.util.HttpUtils;

/**
 * �ı�Ӧ������
 * @author vlinux
 *
 */
public class TextHttpResponseHandler implements HttpResponseHandler {

	private static final Logger logger = LoggerFactory.getLogger("httpfilter-proxy");
	
	@Override
	public boolean isHandleResponse(HttpFilterExchange exchange) {

//		// ��Ҫ�������ݽ��н�ѹ
//		if(StringUtils.isNotEmpty(exchange.getResponseFields().getStringField("Content-Encoding"))){
//			return false;
//		};
		
		// �����ǰ��mime����Լ��mimesָ����Χ�ڣ����ܵ���Text����
		final String mime = HttpUtils.getMIME(exchange.getResponseFields());
		if( !MIME.isText(mime) ) {
			return false;
		}
		
		return true;
	}

	@Override
	public ResponseHandlerResult handleResponse(HttpFilterExchange exchange,
			DataBlock block) throws Exception {
		
		final Charset charset = HttpUtils.getCharset(exchange.getResponseFields(), HttpUtils.DEFAULT_CHARSET);
		final TextBlock textBlock = new TextBlock(block.getDatas(), charset);
		final String mime = HttpUtils.getMIME(exchange.getResponseFields());
		
		final TextBlock returnBlock;
		if( MIME.isHtml(mime) ) {
			returnBlock = convertToHtmlBlock(textBlock, block);
		} else if( MIME.isCss(mime) ) {
			returnBlock = convertToCssBlock(textBlock);
		} else if( MIME.isJson(mime) ) {
			returnBlock = convertToJsonBlock(textBlock);
		} else if( MIME.isXml(mime) ) {
			returnBlock = convertToXmlBlock(textBlock);
		} else {
			returnBlock = textBlock;
		}
		logger.debug("\n[DEBUG] : debug for \"{}\"\n  URL : {}\n  MSG : block cover to={}\n", new Object[]{
			"text block convert.",
			exchange.getRequestURL(),
			returnBlock.getClass().getSimpleName()
		});
		final ResponseHandlerResult result = new ResponseHandlerResult(returnBlock);
		return result;
	}
	
	private HtmlBlock convertToHtmlBlock(final TextBlock textBlock, final DataBlock block) {
		final HtmlBlock html = new HtmlBlock(textBlock.getText(), textBlock.getCharset());
		final Document doc = html.getDocument();
		
		Charset htmlCharset = textBlock.getCharset();
		
		/*
		 * ���� <meta http-equiv="content-type" content="text/html; charset=gbk"/>
		 */
		final Element metaCtEle = doc.select("meta[HTTP-EQUIV=content-type]").first();
		if( null != metaCtEle ) {
			htmlCharset = HttpUtils.getCharset(metaCtEle.attr("content"), htmlCharset);
		}
		
		/*
		 * ���� <meta charset="gbk"/>
		 */
		final Element metaChEle = doc.select("meta[charset]").first();
		if( null != metaChEle ) {
			try {
				htmlCharset = Charset.forName(metaChEle.attr("charset"));
			} catch(Exception e) {
				// ingore
			}
		}
		
		/*
		 * ���Response��Header�е��ַ������HTML-META��ָ���Ĳ�һ��
		 * ����HTML-META�е��ַ����ر���
		 */
		return textBlock.getCharset().equals(htmlCharset)
				// ����ԭ�е��ı���
				? html
				// HTML�ر���
				: new HtmlBlock(new String(block.getDatas(), htmlCharset), htmlCharset);
	}
	
	private CssBlock convertToCssBlock(final TextBlock textBlock) {
		return new CssBlock(textBlock.getText(), textBlock.getCharset());
	}
	
	private XmlBlock convertToXmlBlock(final TextBlock textBlock) {
		return new XmlBlock(textBlock.getText(), textBlock.getCharset());
	}
	
	private JsonBlock convertToJsonBlock(final TextBlock textBlock) {
		return new JsonBlock(textBlock.getText(), textBlock.getCharset());
	}
	
}
