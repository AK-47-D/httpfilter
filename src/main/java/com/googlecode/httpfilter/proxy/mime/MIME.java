package com.googlecode.httpfilter.proxy.mime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.ArrayUtils;

/**
 * HTTP MIME ���Ͷ���
 * @author vlinux
 *
 */
public class MIME {

	private static final String[] _HTML = new String[]{
		"text/html",
	};
	
	private static final String[] _XML = new String[]{
		"text/xml",
		"application/xhtml+xml",
		"application/xml",
	};
	
	private static final String[] _CSS = new String[]{
		"text/css",
	};
	
	private static final String[] _JSON = new String[]{
		"application/json",
	};
	
	private static final String[] _TEXT = new String[]{
		"text/plain",
		"text/javascript",
		"application/x-javascript",
	};
	
	/**
	 * �г���֧�ֵ� mime
	 * @return
	 */
	public static Collection<String> listSupports() {
		final Collection<String> mimes = new ArrayList<String>();
		mimes.addAll(Arrays.asList(_HTML));
		mimes.addAll(Arrays.asList(_XML));
		mimes.addAll(Arrays.asList(_CSS));
		mimes.addAll(Arrays.asList(_JSON));
		mimes.addAll(Arrays.asList(_TEXT));
		return mimes;
	}
	
	/**
	 * �жϵ�ǰmime�Ƿ���֧�ַ�Χ
	 * @param mime
	 * @return
	 */
	public static boolean isSupport(final String mime) {
		return listSupports().contains(mime);
	}
	
	public static boolean isHtml(final String mime) {
		return ArrayUtils.contains(_HTML, mime);
	}
	
	public static boolean isCss(final String mime) {
		return ArrayUtils.contains(_CSS, mime);
	}

	public static boolean isJson(final String mime) {
		return ArrayUtils.contains(_JSON, mime);
	}

	public static boolean isXml(final String mime) {
		return ArrayUtils.contains(_XML, mime);
	}

	public static boolean isText(final String mime) {
		// text �Ƚ����⣬��Ϊhtml/css/json/xml������text
		return ArrayUtils.contains(_TEXT, mime)
				|| isHtml(mime)
				|| isCss(mime)
				|| isJson(mime)
				|| isXml(mime)
		;
	}
	
}
