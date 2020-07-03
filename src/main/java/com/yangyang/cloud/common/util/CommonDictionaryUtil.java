package com.yangyang.cloud.common.util;

import com.yangyang.cloud.common.dictionary.bean.CommonDictItem;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * description:公共的数据字典配置工具类
 * @author zhangyongxue
 * @date 2018/11/23 09:49
 */
@Component
public class CommonDictionaryUtil {

    /**
     * 字典项所属区域
     */
    private static final String REGION_CODE = "region-code";
    /**
     * 字典项组ID
     */
    private static final String GROUP_ID = "group-id";
    /**
     * 字典项配置文件
     */
    private static final String PROPERTIES_FILE_NAME = "config/common_dictionary.properties";

    /**
     * 保存字典项配置文件内容
     */
    private static Map<String, String> propertiesMap = null;

    /**
     * description: 获取字典的所属字典区域及所属字典组信息
     * @author zhangyongxue
     * @date 2018/11/23 10:23
     * @param sign 标识
     * @return java.lang.String
     */
    public static CommonDictItem getCommonDictInfo(String sign) {

        //首次加载字典配置文件
        if(null == propertiesMap){
            propertiesMap = PropertiesLoaderUtil.getPropertyMap(PROPERTIES_FILE_NAME);
        }

        //拼接完整的所属字典区域以及所属字典组的key值
        String regionCode = sign + "." + REGION_CODE;
        String groupId = sign + "." + GROUP_ID;

        CommonDictItem commonDictItem = new CommonDictItem();

        //获取对应的value值
        commonDictItem.setRegionCode(propertiesMap.get(regionCode));
        commonDictItem.setGroupId(propertiesMap.get(groupId));

        return commonDictItem;
    }
}
