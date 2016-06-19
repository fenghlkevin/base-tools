package com.kevin.iesutdio.tools.clazz;

import java.util.List;

public class ClassUtil {
	public static Object createObject(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
	}

	@SuppressWarnings("unchecked")
	public static Class createClass(String className) throws ClassNotFoundException{
		return Thread.currentThread().getContextClassLoader().loadClass(className);
	}
}
