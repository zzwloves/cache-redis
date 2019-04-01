package io.vicp.wloves.cache.redis.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * Json工具转换类
 * @author zhengwei.zhu
 * @date 2018年4月23日 上午11:25:22
 * @version <b>1.0.0</b>
 */
public class JsonUtil {

		private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
		private static ObjectMapper mapper = null;
		private static ObjectMapper mapperWithType = null;

		static {
			mapper = new ObjectMapper();
			// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			// 允许key没有使用双引号的json
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			// 零时区
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mapper.setDateFormat(format);

			// 驼峰转换
//			mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
			// 多态转换
			//mapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
			//配置不写value为null的key
	        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
			// 输出格式化
			mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
			mapper.setSerializationInclusion(Include.NON_NULL);
			
			mapperWithType = new ObjectMapper();
			mapperWithType.enableDefaultTyping(DefaultTyping.NON_FINAL);
		}

		/**
		 * 将对象转换成JSON字符串
		 * 
		 * @param obj
		 *            目标对象
		 * @return 字符串，转换失败时返回null
		 */
		public static String toJson(Object obj) {
			if (obj == null) {
				return null;
			}
			try {
				return mapper.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				logger.error("write to json string error:" + obj, e);
				return null;
			} 
	    }

		/**
		 * 将单个键值对转换成JSON字符串，用于返回只有一个键值对json时的便捷方法
		 * 
		 * @param obj
		 *            目标对象
		 * @return 字符串，转换失败时返回null
		 */
		public static String toJson(Object key, Object value) {
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put(key, value);
			try {
				return mapper.writeValueAsString(map);
			} catch (JsonProcessingException e) {
				logger.error("write to json string error:" + map, e);
				return null;
			}
	    }

		/**
		 * 反序列化POJO或简单Collection如List<String>.
		 * 
		 * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
		 * 
		 * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
		 * 
		 * @see #fromJson(String, JavaType)
		 */
		public static <T> T fromJson(String jsonString, Class<T> clazz) {
			if (StringUtil.isEmpty(jsonString)) {
				return null;
			}
			try {
				return mapper.readValue(jsonString, clazz);
			} catch (IOException e) {
				logger.error("parse json string error:" + jsonString, e);
				return null;
			}
		}
		

		/**
		 * 反序列化复杂Collection如List<Bean>
		 * 
		 * @param jsonString
		 * @param collectionType
		 *            集合类型
		 * @param elementType
		 *            集合内元素类型
		 * @return 转换失败时返回 null
		 */
		public static <L extends Collection<E>, E> L fromJson(String jsonString,
				Class<L> collectionClass, Class<E> elementClass) {
			if (StringUtil.isEmpty(jsonString)) {
				return null;
			}
			try {
				CollectionType type = mapper.getTypeFactory()
						.constructCollectionType(collectionClass, elementClass);
				return mapper.readValue(jsonString, type);
			} catch (Exception e) {
				logger.error("parse json string error:" + jsonString, e);
				return null;
			}
		}

		/**
		 * 反序列化复杂类型<br/>
		 * 针对复杂类型反序列化时，自动转化为LinkedHashMap问题
		 * @param jsonString
		 * @param typeRef 自定义解析器类型
		 * @return
		 */
		public static  <T> T fromJson(String jsonString, TypeReference<T> typeRef) {
			if (StringUtil.isEmpty(jsonString)) {
				return null;
			}
	        try {
	            return mapper.readValue(jsonString, typeRef);
	        } catch (Exception e) {
	        	logger.error("parse json string error:" + jsonString, e);
				return null;
	        } 
	    }
		
		/**
		 * 反序列化复杂类型<br/>
		 * @param jsonString
		 * @param typeRef 自定义解析器类型
		 * @return
		 * @throws IOException 
		 * @throws JsonMappingException 
		 * @throws JsonParseException 
		 */
		public static  <T> T fromJson_(String jsonString, TypeReference<T> typeRef) throws JsonParseException, JsonMappingException, IOException {

			return mapper.readValue(jsonString, typeRef);
	    }
		
		/**
		 * 反序列化POJO或简单Collection如List<String>.
		 * 
		 * 如果JSON输入流为null或流没有内容, 返回Null. 
		 * 
		 * @param is
		 * @param clazz 自定义解析器类型
		 * @return
		 */
		public static <T> T fromInputStream(InputStream is, Class<T> clazz) {
			try {
				if (Objects.isNull(is) || is.available() == 0) {
					return null;
				}
				return mapper.readValue(is, clazz);
			} catch (IOException e) {
				logger.error("parse json string error:", e);
				return null;
			}
		}
		
		/**
		 * 反序列化复杂类型<br/>
		 * 针对复杂类型反序列化时，自动转化为LinkedHashMap问题
		 * @param is
		 * @param typeRef 自定义解析器类型
		 * @return
		 */
		public static <T> T fromInputStream(InputStream is, TypeReference<T> typeRef) {
			try {
				if (Objects.isNull(is) || is.available() == 0) {
					return null;
				}
				return mapper.readValue(is, typeRef);
			} catch (IOException e) {
				logger.error("parse json inputStream error:", e);
				return null;
			}
		}
		
		public static <K,M extends Map<K, L>,L extends Collection<E>, E> M fromJson(String jsonString,
				Class<M> mapClass, Class<L> collectionClass, Class<E> elementClass) {
			if (StringUtil.isEmpty(jsonString)) {
				return null;
			}
	        try {
	            return mapper.readValue(jsonString,  new TypeReference<M>() {});
	        } catch (Exception e) {
	        	logger.error("parse json string error:" + jsonString, e);
				return null;
	        }
		}
		
		
		/**
		 * 当JSON里只含有Bean的部分属性，更新一个已存在Bean，只覆盖该部分的属性.
		 */
		public static void update(String jsonString, Object object) {
			try {
				mapper.readerForUpdating(object).readValue(jsonString);
			} catch (JsonProcessingException e) {
				logger.error("update json string:" + jsonString + " to object:"
						+ object + " error.", e);
			} catch (IOException e) {
				logger.error("update json string:" + jsonString + " to object:"
						+ object + " error.", e);
			}
		}
		
		/**
		 * 转换JSON对象为ajax的方式<br />
		 * ex.{"success":"false","data":{"name":"名称已存在"}}
		 * 
		 * @param obj
		 *            错误信息，若用map，Key若为表单项的name，则会自动在对应表单项显示错误信息
		 */
		public static String toFormJson(Object obj, boolean success) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", success);
			map.put("data", obj);
			try {
				return mapper.writeValueAsString(map);
			} catch (JsonProcessingException e) {
				logger.error("write to json string error:" + obj, e);
				return null;
			}
	    }

		/**
		 * 输出JSONP格式数据
		 */
		public static String toJsonP(String functionName, Object object) {
			return toJson(new JSONPObject(functionName, object));
		}
		
		/**
		 * 将Object转换成Json，维持多态类型.
		 * 该Json不能用于rest请求
		 * @return
		 */
		public static String toJsonAsPolymorphism(Object obj){
			try {
				return mapperWithType.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
			}
	        return null;
		}
		/**
		 * 将Json转换成Object，维持多态类型.
		 * 该转换不能用于rest响应
		 * @return
		 */
		public static <T> T fromJsonAsPolymorphism(String json,Class<T> clazz){
			try {
				return mapperWithType.readValue(json, clazz);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			return null;
		}
	
}
