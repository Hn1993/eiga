package com.eiga.an.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 *
 *	SharedPreferencesUtils工具类
 *
 */
public class SharedPreferencesUtils {
	/**
	 *
	 */
	private static final String FILE_NAME = "shared_data";

	/**
	 *
	 * 	存入数据
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void putShared(Context context, String key, Object object) {

		String type = object.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();

		if ("String".equals(type)) {
			editor.putString(key, (String) object);
		} else if ("Integer".equals(type)) {
			editor.putInt(key, (Integer) object);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) object);
		} else if ("Float".equals(type)) {
			editor.putFloat(key, (Float) object);
		} else if ("Long".equals(type)) {
			editor.putLong(key, (Long) object);
		}

		editor.commit();
	}

	/**
	 * 取出数据
	 * 
	 * @param context
	 * @param key
	 * @param
	 * @return
	 */
	public static Object getShared(Context context, String key, Object defaultObject) {
		String type = defaultObject.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		//return sp.getString(key, "");
		if ("String".equals(type)) {
			return sp.getString(key, (String) defaultObject);
		} else if ("Integer".equals(type)) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if ("Boolean".equals(type)) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if ("Float".equals(type)) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if ("Long".equals(type)) {
			return sp.getLong(key, (Long) defaultObject);
		}
		return null;
	}

	public static void putDoubleShare(Context context, String key, double d){

	}


	/**
	 *
	 * 
	 * @param context
	 * @param key
	 * @param
	 *
	 * @return
	 */
	public static Object getObject(Context context, String key,
                                   Class<?> classOfT) {
		Gson gson = new GsonBuilder().create();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		String json = sp.getString(key, "");
		try {

			Object o = gson.fromJson(json, classOfT);
			return o;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * ���ָ������
	 * 
	 * @param context
	 * @param key
	 */
	public static void clearSp(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, null);
		edit.commit();
	}
	/**
	 * �洢����
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void putObject(Context context , String key, Object object){
		Gson gson=new GsonBuilder().create();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, gson.toJson(object));
		edit.commit();
	}


}
