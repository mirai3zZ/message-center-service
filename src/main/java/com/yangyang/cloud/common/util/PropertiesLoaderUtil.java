package com.yangyang.cloud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * description:自定义配置文件加载器
 * @author zhangyongxue
 * @date 2018/11/23 14:37
 */
@Slf4j
public class PropertiesLoaderUtil {

    /**
     * description:根据配置文件名称加载配置文件
     * @author zhangyongxue
     * @date 2018/11/23 14:51
     * @param propertyFileName 配置文件名称
     * @return java.util.Map<java.lang.String , java.lang.String>
     */
    private static Map<String, String> loadAllProperties(String propertyFileName) {

        Map<String, String> propertiesMap = null;
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(propertyFileName);
            propertiesMap = processProperties(properties);

        } catch (IOException e) {
            e.printStackTrace();
            log.info("Configuration file:{} reading failed", propertyFileName);
        }
        return propertiesMap;
    }

    /**
     * description:解析配置文件信息
     * @author zhangyongxue
     * @date 2018/11/23 14:51
     * @param props 配置
     * @return java.util.Map<java.lang.String , java.lang.String>
     */
    private static Map<String, String> processProperties(Properties props) {

        Map<String, String> propertiesMap = new HashMap<>();

        for (Object key : props.keySet()) {
            String keyStr = key.toString();

            //PropertiesLoaderUtils的默认编码是ISO-8859-1,在这里转码一下
            propertiesMap.put(keyStr, new String(
                    props.getProperty(keyStr).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
        }
        return propertiesMap;
    }

    /**
     * description: 根據配置文件名获取配置文件key-value格式的map
     * @author zhangyongxue
     * @date 2018/11/23 15:15
     * @param fileName 配置文件名
     * @return java.util.Map<java.lang.String , java.lang.String>
     */
    public static Map<String, String> getPropertyMap(String fileName) {
        return loadAllProperties(fileName);
    }
}