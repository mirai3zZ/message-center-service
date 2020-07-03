package com.yangyang.cloud.common.dictionary.controller;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.dictionary.bean.CommonDictItem;
import com.yangyang.cloud.common.dictionary.service.CommonDictItemService;
import com.yangyang.cloud.common.dictionary.vo.CommonDictItemVo;
import com.yangyang.cloud.quota.bean.CommonQuotaBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description:公共数据字典控制层
 * @author zhangyongxue
 * @date 2018/11/23 16:09
 */
@RequestMapping("/operation/dict")
@RestController
public class CommonDictItemController {

    @Autowired
    private CommonDictItemService commonDictItemService;

    /**
     * description:获取数据字典列表
     * @author zhangyongxue
     * @date 2018/11/23 16:15
     * @param sign  数据字典
     * @return com.yangyang.cloud.common.ResponseBean<CommonDictItem>
     */
    @GetMapping("/{sign}")
    @ResponseBody
    public ResponseBean<List<CommonDictItemVo>> getCommonDictItemList(@PathVariable("sign") String sign){
       return commonDictItemService.getCommonDictItemList(sign);
    }
}
