package com.example.zhbj47.utils;

import com.google.gson.Gson;

public class GsonUtils {
	
	/**
	 * 把json数据封装成Bean
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T json2Bean(String json,Class<T> clazz){
		Gson gson=new Gson();
		return gson.fromJson(json, clazz);
		
	}
}
