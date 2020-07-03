package com.yangyang.cloud.message.service;
//
//import com.yangyang.cloud.common.DefaultRestTemplate;
//import com.yangyang.cloud.message.bean.PicpUserEntity;
import com.inspur.bss.commonsdk.utils.IdWorker;
import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.TimeUtil;
import com.yangyang.cloud.common.bean.UserInfoBean;
import com.yangyang.cloud.message.bean.UserMessageReceiverEntity;
import com.yangyang.cloud.message.common.MessageConstants;
import com.yangyang.cloud.message.mapper.UserMapper;
import com.yangyang.cloud.message.mapper.UserMessageMapper;
import com.yangyang.cloud.message.vo.UserMsgCreatedUserIdVo;
import com.yangyang.cloud.messagemanage.service.MessageManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Author: mengfanlong
// * E-mail: meng.fanlong@inspur.com
// * Date:   2018/9/12 23:51
// * -------------------------------
// * Desc:   用户数据处理
// */
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageManageService messageManageService;

    @Autowired
    private UserMessageMapper userMessageMapper;
//
//    @Autowired
//    @Qualifier("restTemplate")
//    private RestTemplate restTemplate;
//    @Autowired
//    @Qualifier("defaultRestTemplate")
//    DefaultRestTemplate defaultRestTemplate;
//
//    public PicpUserEntity getUser(String email) {
//        //调用示例
////       PicpUserEntity userFromOperationService = restTemplate.getForObject("http://operation-service-chengym/get?email=562634404@qq.com", PicpUserEntity.class);
//        //调用示例 never used
////        PicpUserEntity userFromMybatis = userMapper.getUserInfoByEmail(email);
//        Map<String, String> map = new HashMap<>(8);
//        map.put ("module" ,"user") ;
//        map.put("userId", "123");
//        map.put("alias", "12333") ;
//        map.put("createdTime", "2018-10-27 00:00:00");
//        map.put( "mobile", "18888358065") ;
//        defaultRestTemplate.exchange("http://user-service/user", HttpMethod.POST, map, String.class,new Object[]{});
//        PicpUserEntity userFromMybatis = new PicpUserEntity();
//        userFromMybatis.setEmail(email);
//        return userFromMybatis;
//    }

    public ResponseBean updateUserMsgReceiver(){
        ResponseBean responseBean = new ResponseBean();
        List<String> allCreatedUserIds = userMapper.getAllCreatedUserId();
        //将接收人列表中存在用户全部增加一条账号联系人信息
        for(String createdUserId : allCreatedUserIds){

            Integer count = userMessageMapper.getMessageReceiverSelf(createdUserId);
            //判断消息接收人列表是否有用户本人
            if(count==0){
                UserInfoBean userInfoBean = new UserInfoBean();
                try {
                    userInfoBean = messageManageService.getUserInfoById(createdUserId);
                }catch (Exception e){
                    log.info("获取不到用户信息的id{}",e);
                    continue;
                }
                UserMessageReceiverEntity userMessageReceiverEntity = new UserMessageReceiverEntity();
                userMessageReceiverEntity.setId(BigInteger.valueOf(IdWorker.getNextId()));
                Date date = TimeUtil.string2DateTime("2018-01-01 00:00:01");
                userMessageReceiverEntity.setCreatedTime(date);
                userMessageReceiverEntity.setCreatedUserName(userInfoBean.getUserName());
                userMessageReceiverEntity.setCreatedUserId(createdUserId);
                userMessageReceiverEntity.setReceiverName(MessageConstants.RECEIVER_SELF);
                if(userInfoBean.getEmail()!=null){
                    userMessageReceiverEntity.setReceiverEmail(userInfoBean.getEmail());
                }else{
                    userMessageReceiverEntity.setReceiverEmail(MessageConstants.UNKOWN);
                }
                if(userInfoBean.getMobile()!=null){
                    userMessageReceiverEntity.setReceiverMobile(userInfoBean.getMobile());
                }else{
                    userMessageReceiverEntity.setReceiverMobile(MessageConstants.UNKOWN);
                }
                userMessageReceiverEntity.setEmailCheckPass(MessageConstants.STATUS_YES);
                userMessageReceiverEntity.setMobileCheckPass(MessageConstants.STATUS_YES);
                userMessageReceiverEntity.setStatus(MessageConstants.STATUS_YES);
                userMessageReceiverEntity.setIsUserSelf(MessageConstants.STATUS_YES);
                int success = userMessageMapper.addMessageReceiverSelf(userMessageReceiverEntity);
                if(success==1){
                    log.info("{}该用户账号联系人在接收人列表初始化完成",createdUserId);
                }
            }
        }
        List<UserMsgCreatedUserIdVo> userMsgCreatedUserIdVoList = userMapper.getCreatedUserId();
        List<String> failureList = new ArrayList<>();
        log.info("查询出来的账号联系人一共有{}条",userMsgCreatedUserIdVoList.size());
        //更新created_time
        for(UserMsgCreatedUserIdVo userMsgCreatedUserIdVo:userMsgCreatedUserIdVoList){
            Map map =new HashMap();
            Date date = TimeUtil.string2DateTime("2018-01-01 00:00:01");
            map.put("createdTime",date);
            map.put("receiverId",userMsgCreatedUserIdVo.getId());
            int success = userMapper.updateReceiverCreatedTime(map);
            if(success==0){
                log.info("接收人列表的创建时间更新失败，receiverId:{}",userMsgCreatedUserIdVo.getId());
                failureList.add(userMsgCreatedUserIdVo.getId());
            }
        }
        //更新数据为0的receiverId
        for(UserMsgCreatedUserIdVo userMsgCreatedUserIdVo : userMsgCreatedUserIdVoList){
            List<String> ids = userMapper.getMsgReceiverSettingId(userMsgCreatedUserIdVo.getCreatedUserId());
            for(String id : ids){
                Map<String,String> queryMap = new HashMap<>(2);
                queryMap.put("receiverId",userMsgCreatedUserIdVo.getId());
                queryMap.put("id",id);
                int success = userMapper.updateUserMsgReceiverSetting(queryMap);
                if(success==0){
                    log.info("这个receiverId:{}的数据更新失败了,具体的user-receiver-setting-id是:{}",userMsgCreatedUserIdVo.getId(),id);
                    failureList.add(userMsgCreatedUserIdVo.getId());
                }
            }
        }
       if(failureList.size()>0){
           log.info("部分数据更新失败",failureList.toArray());
           responseBean.setCode("1");
           responseBean.setMessage("部分数据更新失败");
       }else {
           responseBean.setCode("0");
           responseBean.setMessage("数据更新完成");
       }
       return responseBean;
    }
}
