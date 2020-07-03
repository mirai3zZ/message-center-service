package com.yangyang.cloud.common.dictionary.service;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.common.dictionary.bean.CommonDictItem;
import com.yangyang.cloud.common.dictionary.mapper.CommonDictItemMapper;
import com.yangyang.cloud.common.dictionary.vo.CommonDictItemVo;
import com.yangyang.cloud.common.util.CommonDictionaryUtil;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import com.yangyang.cloud.workorder.common.WorkOrderConstants;
import com.yangyang.cloud.workorder.common.WorkOrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description:数据字典service层
 * @author zhangyongxue
 * @date 2018/11/23 16:17
 */
@Slf4j
@Service
public class CommonDictItemService {

    /**数据字典dao层*/
    @Autowired
    private CommonDictItemMapper commonDictItemMapper;

    @Autowired
    private WorkOrderUtils workOrderUtils;

    /**
     * description:获取数据字典列表
     * @author zhangyongxue
     * @date 2018/11/23 16:20
     * @param sign
     * @return com.yangyang.cloud.common.ResponseBean<com.yangyang.cloud.common.dictionary.vo.CommonDictItemVo>
     */
    public ResponseBean<List<CommonDictItemVo>> getCommonDictItemList(String sign) {
        ResponseBean<List<CommonDictItemVo>> responseBean = new ResponseBean<>();

        //1.从配置文件中获取字典项的相关信息
        CommonDictItem commonDictItem = getCommonDictItem(sign);

        User user = SecurityContextUtil.getLoginUser();
        log.info("user id [{}] name [{}] get dictionary list[regionCode:{} and groupId:{}]",
                user.getId(), user.getName(), commonDictItem.getRegionCode(), commonDictItem.getGroupId());

        //2.对查询条件进行验证

        //验证字典项所属区域是否为空
        if(StringUtils.isEmpty(commonDictItem.getRegionCode())){
            //获取错误码信息:002:字典项所属区域为空
            return (ResponseBean<List<CommonDictItemVo>>)workOrderUtils.getErrorCodeResponse(responseBean, "002");
        }

        //验证字典项所属组ID是否为空
        if(StringUtils.isEmpty(commonDictItem.getGroupId())){
            //获取错误码信息:003:字典项组ID为空
            return (ResponseBean<List<CommonDictItemVo>>)workOrderUtils.getErrorCodeResponse(responseBean, "003");
        }

        //3.根据bean查询字典项信息并返回列表
        List<CommonDictItemVo> commonDictItemList = commonDictItemMapper.getCommonDictItemListByBean(commonDictItem);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        if (0 == commonDictItemList.size()){
            responseBean.setMessage(WorkOrderConstants.NO_RECORD);
        }else{
            responseBean.setMessage(WorkOrderConstants.SELECT_SUCCESS_MSG);
        }
        responseBean.setResult(commonDictItemList);
        return responseBean;
    }

    /**
     * description: 获取字典项对象
     * @author zhangyongxue
     * @date 2018/11/23 16:30
     * @param sign   标识
     * @return com.yangyang.cloud.common.dictionary.bean.CommonDictItem
     */
    private CommonDictItem getCommonDictItem(String sign){

        CommonDictItem commonDictItem = CommonDictionaryUtil.getCommonDictInfo(sign);
        //字典项是否生效;1 有效 0无效
        commonDictItem.setEnabled(WorkOrderConstants.DIGITAL_NUMBER_ONE);
        return commonDictItem;
    }
}
