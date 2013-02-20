package com.googlecode.httpfilter.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.googlecode.httpfilter.constant.ErrorCodeConstants;

/**
 * ���ؽ����������
 * @author jiangyi.ctd
 *
 */
public class ResultUtils {

	/**
	 * �����İ���Ϣע��
	 * @author jiangyi.ctd
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface ErrorMessage {

		/**
		 * �����Ӧ�İ���Ϣ
		 * @return
		 */
		String errorMessage() default StringUtils.EMPTY;
		
	}
	
	private static final Map<String, String> errorMsgMap = new HashMap<String, String>();

	static{
		Field[] fields = ErrorCodeConstants.class.getFields();
		if(fields != null){
			for(Field field: fields){
				String errorCode;
				try {
					errorCode = (String) field.get(null);
				} catch (Exception e) {
					//��д���ֶζ��岻���쳣
					continue;
				}
				String errorMsg = null;
				//���ֶ�ע���ϻ�ȡ������Ϣ
				if(field.isAnnotationPresent(ErrorMessage.class)){
					errorMsg = field.getAnnotation(ErrorMessage.class).errorMessage();
				}else{
					errorMsg = errorCode;
				}
				errorMsgMap.put(errorCode, errorMsg);
			}
		}
	}

	/**
	 * ���ݴ�����Ͳ�����ȡ�����İ�
	 * @param errorCode
	 * @param args
	 * @return
	 */
	public static String getErrorMsg(String errorCode, Object... args){
		//����������Ӧ�İ�δ���壬��ֱ�ӷ��ر���
		if(StringUtils.isEmpty(errorCode) || !errorMsgMap.containsKey(errorCode)){
			return errorCode;
		}
		return String.format(errorMsgMap.get(errorCode), args);
	}
	
}
