package com.googlecode.httpfilter.proxy.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handler������
 * @author vlinux
 *
 */
public class HandlerResult {

	private final Map<String, String> headerModifier;
	private final Set<String> headerRemover;
	
	public HandlerResult() {
		this.headerModifier = new HashMap<String, String>();
		this.headerRemover = new HashSet<String>();
	}
	
	/**
	 * ����Header�е�K-V
	 * @param name
	 * @param value
	 */
	public void setHeader(String name, String value) {
		headerModifier.put(name, value);
	}
	
	/**
	 * ɾ��Header��ָ��Key
	 * @param name
	 */
	public void removeHeader(String name) {
		headerRemover.add(name);
	}
	
	/**
	 * ��ȡHeader��Ҫ�޸ĵ�Key-Value
	 * @return
	 */
	public Map<String, String> getHeaderModifier() {
		return new HashMap<String, String>(headerModifier);
	}
	
	/**
	 * ��ȡHeader��Ҫɾ����Key
	 * @return
	 */
	public Set<String> getHeaderRemover() {
		return new HashSet<String>(headerRemover);
	}
	
}
