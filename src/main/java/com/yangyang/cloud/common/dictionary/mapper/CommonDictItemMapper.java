package com.yangyang.cloud.common.dictionary.mapper;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.dictionary.bean.CommonDictItem;
import com.yangyang.cloud.common.dictionary.vo.CommonDictItemVo;
import com.yangyang.cloud.common.dictionary.vo.CommonDictVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * description:数据字典项mapper层
 * @author zhangyongxue
 * @date 2018/11/23 20:17
 */
@Mapper
public interface CommonDictItemMapper {

    /**
     * description:根据bean查询字典项信息列表
     * @author zhangyongxue
     * @date 2018/11/23 20:32
     * @param commonDictItem
     * @return java.util.List<com.yangyang.cloud.common.dictionary.vo.CommonDictItemVo>
     */
    List<CommonDictItemVo> getCommonDictItemListByBean(CommonDictItem commonDictItem);

    /**
    * description:根据bean查询字典项信息列表
    * @param commonDictItem
     * @return java.util.List<com.yangyang.cloud.common.dictionary.vo.CommonDictVo>
     */
    List<CommonDictVo> getCommonDictListByBean(CommonDictItem commonDictItem);

}
