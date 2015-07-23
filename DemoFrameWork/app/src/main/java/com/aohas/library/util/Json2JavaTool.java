package com.aohas.library.util;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON自动装配工具类, 实体类里有 list 或 数组 时，要加注解 @FromJsonArray
 * 注意：代码混淆时，影响此类的使用，解决办法，
 * 	1.在混淆配置文件中设置保留实体类不混淆。（-keep class com.package.* {*;}）
 * 	2.在混淆配置文件中设置保留实体类无参构造方法不混淆，字段使用 @JsonKey 。（-keepclasseswithmembers class com.package.** {   public <init>(); }）
 * 	3.在混淆配置文件中设置保留实体类无参构造方法及字段不混淆。
 * @author {YueJinbiao}
 * 
 */
public class Json2JavaTool {

	private static final String TAG = "Json2JavaTool";
	
	private static boolean LOG_ENABLE = false;

	/**
	 * 集合或数组变量注释，表示要把一个 JSONArray 装配成一个 list 或者 数组; 基本数据类型的变量必须用封装类声明类型,比如
	 * list<Integer> ,Integer[], (clazz=Integer.class);
	 * 
	 * @author {YueJinbiao}
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FromJsonArray {
		Class clazz();
	}

	/**
	 * 变量注释，用于指定json数据中相应 key 值，不用此注释时，使用变量名，代码混淆时必须要用这个注释。
	 * 
	 * @author {YueJinbiao}
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface JsonKey {
		String key();
	}

	/**
	 * 如实体类需要在装配完成以后作一些业务处理，可一实现这个监听器， 在 onAssembleSuccess() 方法里添加逻辑。
	 * 
	 * @author {YueJinbiao}
	 */
	public interface OnAssembleSuccessListener {
		public void onAssembleSuccess();
	}

	/**
	 * JSONObject 组装成 java 对象
	 * 
	 * @param clazz
	 *            解析类型的 Class
	 * @param json
	 * @return
	 */
	public static <T> T toJavaObject(Class<T> clazz, JSONObject json) {
		if(clazz == String.class){
			return (T) json.toString();
		}
		Object result = null;
		try {
			result = clazz.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		if(result == null){
			Log.e(TAG, "类" + clazz.getSimpleName() + "未能实例化！！");
			return null;
		}
		Field[] fields = clazz.getFields();
		try {
			for (Field field : fields) {
				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}

				String fieldName = getJsonKey(field);

				if (json.isNull(fieldName)) {
					if(LOG_ENABLE){
						Log.i(TAG, "类" + clazz.getSimpleName() + "的字段" + fieldName + "(json数据空)!");
					}
					continue;
				} else {
					if(LOG_ENABLE){
						Log.i(TAG,	"Try to assemble field@[" + clazz.getSimpleName() + "." + fieldName + "]");
					}
				}
				field.setAccessible(true);
				Class fieldClazz = field.getType();
				if (fieldClazz.equals(int.class)) {
					field.setInt(result, json.optInt(fieldName));
				} else if (fieldClazz.equals(long.class)) {
					field.setLong(result, json.optLong(fieldName));
				} else if (fieldClazz.equals(boolean.class)) {
					field.setBoolean(result, json.optBoolean(fieldName));
				} else if (fieldClazz.equals(float.class)) {
					field.setFloat(result, (float) json.optDouble(fieldName));
				} else if (fieldClazz.equals(double.class)) {
					field.setDouble(result, json.optDouble(fieldName));
				} else if (fieldClazz.equals(String.class)) {
					field.set(result, json.optString(fieldName));
				} else if (List.class.isAssignableFrom(fieldClazz)) {
					List results = getListValues(field, json);
					if (results != null) {
						field.set(result, results);
					}
				} else if (fieldClazz.isArray()) {
					Object results = getArrayValues(field, json);
					if (results != null) {
						field.set(result, results);
					}
				} else if (!fieldClazz.isInterface()) {
					JSONObject jObj = json.getJSONObject(fieldName);
					if (jObj != null) {
						Object value = toJavaObject(fieldClazz, jObj);
						field.set(result, value);
					}
				}
				if(LOG_ENABLE){
					Log.i(TAG, "Assemble field@[" + clazz.getSimpleName() + "." + fieldName + "]#result=" + result);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (OnAssembleSuccessListener.class.isAssignableFrom(clazz)) {
			((OnAssembleSuccessListener) result).onAssembleSuccess();
		}
		if(LOG_ENABLE){
			Log.i(TAG, "Assemble class@[" + clazz.getSimpleName() + "]#result="	+ result);
		}
		return (T) result;
	}

	/**
	 * JSONArray 组装成 java List 集合
	 * 
	 * @param clazz
	 *            List存储类型的 Class
	 * @param jsonArray
	 * @return
	 */
	public static <T> List<T> toJavaList(Class<T> insideClazz,
			JSONArray jsonArray) {
		List<T> results = new ArrayList<T>();
		try {
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				Object object = jsonArray.get(i);
				if (object instanceof JSONObject) {
					Object obj = toJavaObject(insideClazz, (JSONObject) object);
					if (obj != null) {
						results.add((T) obj);
					}
				} else {
					results.add((T) object);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * JSONArray 组装成 java 数组
	 * 
	 * @param clazz
	 *            List存储类型的 Class
	 * @param jsonArray
	 * @return
	 */
	public static <T> T[] toJavaArray(Class<T> insideClazz, JSONArray jsonArray) {
		int length = jsonArray.length();
		Object results = Array.newInstance(insideClazz, length);
		for (int i = 0; i < length; i++) {
			try {
				Object object = jsonArray.get(i);
				if (object instanceof JSONObject) {
					Object obj = toJavaObject(insideClazz, (JSONObject) object);
					if (obj != null) {
						Array.set(results, i, insideClazz.cast(obj));
					}
				} else {
					Array.set(results, i, insideClazz.cast(object));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		if(LOG_ENABLE){
			Log.i(TAG, "Assemble list@[" + insideClazz.getSimpleName()	+ "]#result=" + results);
		}
		return (T[]) results;

	}

	private static List getListValues(Field field, JSONObject json) {
		List value = null;
		FromJsonArray j2lAnno = field.getAnnotation(FromJsonArray.class);
		if (j2lAnno != null) {
			Class insideClazz = j2lAnno.clazz();
			if (insideClazz != null) {
				try {
					JSONArray jsonArray = json.getJSONArray(getJsonKey(field));
					if (jsonArray != null) {
						value = toJavaList(insideClazz, jsonArray);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	private static Object[] getArrayValues(Field field, JSONObject json) {
		Object[] value = null;
		FromJsonArray j2lAnno = field.getAnnotation(FromJsonArray.class);
		if (j2lAnno != null) {
			Class insideClazz = j2lAnno.clazz();
			if (insideClazz != null) {
				try {
					JSONArray jsonArray = json.getJSONArray(getJsonKey(field));
					if (jsonArray != null) {
						value = toJavaArray(insideClazz, jsonArray);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	private static String getJsonKey(Field field) {
		JsonKey jsonKey = field.getAnnotation(JsonKey.class);
		String key = null;
		if (jsonKey == null) {
			key = field.getName();
		} else {
			key = jsonKey.key();
		}
		return key;
	}
	
	public static void setLogEnable(boolean enable){
		LOG_ENABLE = enable;
	}
}