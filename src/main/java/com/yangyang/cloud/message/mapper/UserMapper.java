package com.yangyang.cloud.message.mapper;
//
//import com.yangyang.cloud.message.bean.PicpUserEntity;
import com.yangyang.cloud.message.vo.UserMsgCreatedUserIdVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

//
///**
// * Author: mengfanlong
// * E-mail: morphy8@163.com
// * Date:   2016/11/25 17:19
// * ---------------------------
// * Desc:   用户信息 数据访问mapper
// */
@Mapper
public interface UserMapper {
//
//    /**
//     * 根据email获取用户所有的信息
//     *
//     * @param email 邮箱
//     * @return {@link com.yangyang.cloud.message.bean.PicpUserEntity}
//     */
//    PicpUserEntity getUserInfoByEmail(String email);
    List<String> getAllCreatedUserId();

    List<UserMsgCreatedUserIdVo> getCreatedUserId();

    List<String> getMsgReceiverSettingId(String createdUserId);

    int updateUserMsgReceiverSetting(Map queryMap);

    int updateReceiverCreatedTime(Map map);
}
