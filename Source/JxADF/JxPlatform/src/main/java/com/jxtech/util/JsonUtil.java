package com.jxtech.util;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JSON处理工具类
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.02
 * 
 */
public class JsonUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 返回第一个记录的值
     * 
     * @param list
     * @return
     */
    public static String getStringOfFirstValue(List<Map<String, Object>> list) {
        if (list == null) {
            return null;
        }
        if (list.size() < 1) {
            return null;
        }
        Map<String, Object> map = list.get(0);
        if (map != null && map.size() > 0) {
            Object value = map.values().iterator().next();
            if (value == null) {
                return null;
            }
            return String.valueOf(value);
        }
        return null;
    }

    /**
     * 返回值的数组
     * 
     * @param list
     * @param key
     * @return
     */
    public static String getArrayOfValue(List<Map<String, Object>> list, String sql) {
        if (list == null) {
            return null;
        }
        int size = list.size();
        if (size <= 0) {
            return null;
        }
        Map<String, Object> first = list.get(0);// 第一条记录
        int cs = first.size();// 有多少个字段
        List<Object> rs = new ArrayList<Object>();
        if (cs == 1) {
            String name = first.keySet().iterator().next();
            // 只显示值即可。
            for (int i = 0; i < size; i++) {
                rs.add(list.get(i).get(name));
            }
        } else if (cs > 1) {
            String[] columns = SqlParserUtil.getColumns(sql);
            for (int i = 0; i < size; i++) {
                Map<String, Object> dto = list.get(i);
                List<Object> lv = new ArrayList<Object>();
                if (columns != null) {
                    for (int m = 0; m < cs && m < columns.length; m++) {
                        lv.add(dto.get(columns[m]));
                    }
                } else {
                    for (Map.Entry<String, Object> enty : dto.entrySet()) {
                        lv.add(enty.getValue());
                    }
                }
                rs.add(lv);
            }
        }
        return toJson(rs);
    }

    public static String toJson(Object list) {
        if (list == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        StringWriter sw = new StringWriter();
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createJsonGenerator(sw);
            mapper.writeValue(gen, list);
            return sw.toString();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
        return null;
    }

    public static List<Map<String, Object>> fromJson(String json) {
        if (StrUtil.isNull(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json.trim(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            LOG.error(e.getMessage() + "\r\n" + json, e);
        }
        return null;
    }

    /**
     * 返回JSON格式
     * 
     * @param list
     * @param columns
     * @param head
     * @param jboname
     * @param count
     * @param pageSize
     * @param pageNum
     * @return
     * @throws JxException
     */
    public static String toJson(List<Map<String, Object>> list, String[] columns, boolean head, String jboname, int count, int pageSize, int pageNum) throws JxException {
        StringBuilder sb = new StringBuilder();
        if (list == null) {
            return sb.toString();
        }
        int size = list.size();
        sb.append("[");
        if (head) {
            sb.append("{\"pagesize\":\"");
            sb.append(pageSize);
            sb.append("\",\"pagenum\":\"");
            sb.append(pageNum);
            sb.append("\",\"count\":\"");
            sb.append(count);
            sb.append("\",\"jboname\":\"");
            sb.append(jboname);
            sb.append("\"}");
            if (size > 0) {
                sb.append(",");
            }
        }
        if (size > 0) {
            JboSetIFace js = JboUtil.getJboSet(jboname);
            for (int i = 0; i < size; i++) {
                sb.append("{");
                JboIFace jbo = js.getJbo(true);
                jbo.setData(list.get(i));
                for (int j = 0; j < columns.length; j++) {
                    String col1 = columns[j];
                    String col2 = columns[j];
                    int pos = columns[j].indexOf(':');
                    if (pos > 0 && pos < columns[j].length()) {
                        col1 = columns[j].substring(0, pos);
                        col2 = columns[j].substring(pos + 1);
                    }
                    sb.append("\"");
                    sb.append(col2);
                    sb.append("\":\"");
                    String val = jbo.getString(col1);
                    if (val != null) {
                        sb.append(StrUtil.toJson(val));
                    }
                    sb.append("\"");
                    if (j < columns.length - 1) {
                        sb.append(",");
                    }
                }
                sb.append("}");
                if (i < (size - 1)) {
                    sb.append(",");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static JsonConfig config = new JsonConfig();

    static {
        config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);// 忽略循环，避免死循环
        config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {// 处理Date日期转换
                    @Override
                    public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date d = (Date) arg1;
                        return sdf.format(d);
                    }

                    @Override
                    public Object processArrayValue(Object arg0, JsonConfig arg1) {
                        return null;
                    }
                });
    }

    /**
     * 直接转换为标准的JSON格式
     * 
     * @param list
     * @return
     */
    public static String getJsonOfList(List<Map<String, Object>> list) {
        if (list == null) {
            return null;
        }
        try {
            JSONArray jsonArray = JSONArray.fromObject(list);
            if (jsonArray == null) {
                LOG.info("转换JSON格式出错。");
                return null;
            }
            return jsonArray.toString();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    /**
     * 是否是json字符串
     * 
     * @param str
     * @return
     */
    public static boolean isJson(String str) {
        if (StrUtil.isNull(str)) {
            return false;
        }
        try {
            JSONObject.fromObject(str);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return false;
        }
    }

    /**
     * java object convert to json string
     */
    public static String pojo2json(Object obj) {
        return JSONObject.fromObject(obj, config).toString();// 可以用toString(1)来实现格式化，便于阅读
    }

    /**
     * array、map、Javabean convert to json string
     */
    public static String object2json(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSONSerializer.toJSON(obj).toString();
    }

    /**
     * json string convert to javaBean
     * 
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> T json2pojo(String jsonStr, Class<T> clazz) {
        JSONObject jsonObj = JSONObject.fromObject(jsonStr);
        T obj = (T) JSONObject.toBean(jsonObj, clazz);
        return obj;
    }

    /**
     * json string convert to map
     */
    public static Map<String, Object> json2map(String jsonStr) {
        JSONObject jsonObj = JSONObject.fromObject(jsonStr);
        Map<String, Object> result = (Map<String, Object>) JSONObject.toBean(jsonObj, Map.class);
        return result;
    }

    /**
     * json string convert to array
     */
    public static Object[] json2arrays(String jsonString) {
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(jsonString);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setArrayMode(JsonConfig.MODE_OBJECT_ARRAY);
        Object[] objArray = (Object[]) JSONSerializer.toJava(jsonArray, jsonConfig);
        return objArray;
    }

    /**
     * json string convert to list
     * 
     * @param <T>
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public static <T> List<T> json2list(String jsonString, Class<T> pojoClass) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        return JSONArray.toList(jsonArray, pojoClass);
    }
}
