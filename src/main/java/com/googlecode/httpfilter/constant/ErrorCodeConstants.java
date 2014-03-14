package com.googlecode.httpfilter.constant;

import com.googlecode.httpfilter.util.ResultUtils.ErrorMessage;

public class ErrorCodeConstants {

	@ErrorMessage(errorMessage="ͨ��batchNo��ȡNAS�ļ�ʧ��")
	public static final String NAS_QUERY_FAILED_BY_BATCHNO = "NAS_QUERY_FAILED_BY_BATCHNO";
	
	@ErrorMessage(errorMessage="��ѯ������(id=%s)ʧ�ܣ�")
	public static final String GET_FILTER_BY_ID_ERROR = "GET_FILTER_BY_ID_ERROR";
	
	@ErrorMessage(errorMessage="��ѯ����(id=%s)ʧ�ܣ�")
	public static final String GET_CONNECTION_BY_ID_ERROR = "GET_CONNECTION_BY_ID_ERROR";
	
	@ErrorMessage(errorMessage="���Ӳ���ѯ����(traceId=%s)ʧ�ܣ�")
	public static final String FETCH_CONNECTION_BY_ID_ERROR = "FETCH_CONNECTION_BY_ID_ERROR";
	
	@ErrorMessage(errorMessage="ͨ��traceId��ѯ����(traceId=%s)ʧ�ܣ�")
	public static final String GET_CONNECTION_BY_TRACE_ID_ERROR = "GET_CONNECTION_BY_TRACE_ID_ERROR";
	
	@ErrorMessage(errorMessage="����ConnectionDoʧ�ܣ�")
	public static final String CREATE_CONNECTION_ERROR = "CREATE_CONNECTION_ERROR";
	
	@ErrorMessage(errorMessage="ͨ��traceId��ѯ�Ự(traceId=%s)ʧ�ܣ�")
	public static final String GET_COMMUNICATION_BY_TRACE_ID_ERROR = "GET_COMMUNICATION_BY_TRACE_ID_ERROR";
	
	@ErrorMessage(errorMessage="��ѯ�Ự(id=%s)ʧ�ܣ�")
	public static final String GET_COMMUNICATION_BY_ID_ERROR = "GET_COMMUNICATION_BY_ID_ERROR";
	
	@ErrorMessage(errorMessage="�����Ựʧ�ܣ�")
	public static final String CREATE_COMMUNICATION_ERROR = "CREATE_COMMUNICATION_ERROR";
	
	@ErrorMessage(errorMessage="��ѯ����(id=%s)ʧ�ܣ�")
	public static final String GET_RULE_BY_ID_ERROR = "GET_RULE_BY_ID_ERROR";
	
	@ErrorMessage(errorMessage="��ѯ��У��Ĺ���(id=%s)ʧ�ܣ�")
	public static final String GET_TOBECHECK_BY_ID_ERROR = "GET_TOBECHECK_BY_ID_ERROR";
	
	@ErrorMessage(errorMessage="��ѯ���а汾(id=%s)ʧ�ܣ�")
	public static final String GET_VERSION_BY_ID_ERROR = "GET_VERSION_BY_ID_ERROR";
	
	@ErrorMessage(errorMessage="У���������޹ؼ���Ϊ��(keyWords=%s)������")
	public static final String MAIN_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR = "MAIN_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR";
	
	@ErrorMessage(errorMessage="��У���������޹ؼ���Ϊ��(keyWords=%s)������")
	public static final String CHECK_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR = "CHECK_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR";
	
	@ErrorMessage(errorMessage="�����������޹ؼ���Ϊ��(keyWords=%s)������")
	public static final String CHECK_AND_MAIN_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR = "CHECK_AND_MAIN_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR";
	
	@ErrorMessage(errorMessage="��У�����������������(param=%s)����Ӧ��ֵ��ͬ��")
	public static final String CHECK_PARAM_VALUE_NOE_EQUAL = "CHECK_PARAM_VALUE_NOE_EQUAL";
	
	@ErrorMessage(errorMessage="��У��������ȱ�ٲ�����(param=%s)")
	public static final String CHECK_NOT_CONTAIN_PARAM = "CHECK_NOT_CONTAIN_PARAM";
	
	@ErrorMessage(errorMessage="��У��response���ڴ���response���ݲ���������ؼ���Ϊ��(keyWords=%s)")
	public static final String CHECK_RESPONSE_CONTENT_NOT_SAME = "CHECK_RESPONSE_CONTENT_NOT_SAME";
}
