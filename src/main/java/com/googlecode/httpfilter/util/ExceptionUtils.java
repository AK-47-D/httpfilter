package com.googlecode.httpfilter.util;

import org.apache.commons.lang.StringUtils;
import org.h2.jdbc.JdbcSQLException;

public class ExceptionUtils {

	/**
	 * �жϵ�ǰ�쳣�Ƿ���H2��Ψһ�����쳣
	 * @param t
	 * @return
	 */
	public static boolean isH2UniqueIndexException(Throwable t) {
		
		if( null == t ) {
			return false;
		}
		
		if( !(t instanceof JdbcSQLException ) ) {
			return false;
		}
		
		return StringUtils.containsIgnoreCase(t.getMessage(), "Unique index or primary key violation");
		
	}
	
}
