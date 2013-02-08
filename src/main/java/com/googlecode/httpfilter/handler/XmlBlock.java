package com.googlecode.httpfilter.handler;

import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;

/**
 * XML��
 * @author vlinux
 *
 */
public class XmlBlock extends TextBlock {

	/*
	 * xml�ĵ�
	 */
	private final Document document;
	
	public XmlBlock(String text, Charset charset) {
		super(text, charset);
		this.document = Jsoup.parse(text, "", new Parser(new XmlTreeBuilder()));
	}

	/**
	 * ��ȡ��ǰxml�ĵ�
	 * @return
	 */
	public Document getDocument() {
		return document.clone();
	}
	
}
