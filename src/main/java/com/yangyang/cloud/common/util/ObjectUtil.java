package com.yangyang.cloud.common.util;

import java.lang.reflect.Field;

import com.yangyang.cloud.workordermanage.bean.OperationWorkorderProblemTypeEntity;

/**
 * 与对象通用内容相关的工具类
 * @author daiyan
 */
public class ObjectUtil {
	/**
	 * 检查对象的所有属性是否全为空
	 * 如果对象没有属性，返回 true
	 * @param object Object
	 * @return boolean
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static boolean allFieldAraNull(Object object) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (null != field.get(object)) {
				field.setAccessible(false);
				return false;
			}
			field.setAccessible(false);
		}
		return true;
	}
	
	/**
	 * just for test
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		OperationWorkorderProblemTypeEntity object = new OperationWorkorderProblemTypeEntity();
		System.out.println(allFieldAraNull(object));
	}
}
