package com.yangyang.cloud.message.mapper;

import com.yangyang.cloud.message.bean.*;
import com.yangyang.cloud.message.dto.MessageDeleteDTO;
import com.yangyang.cloud.message.vo.UserMessageAddVo;
import com.yangyang.cloud.message.vo.UserMsgReceiverSettingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMessageMapper {

    /**
     * 根据userId获取用户接收消息的配置信息
     *
     * @return {@link List< UserMessageSettingEntity >}
     */
    List<UserMessageSettingEntity> getUserMessageSetting(String userId);

    /**
     * 修改用户接收消息的配置信息
     *
     * @return {@link UserMessageSettingEntity}
     */
    int editUserMessageSetting(List<UserMessageSettingEntity> list);

    /**
     * 根据id 获取该设置的可编辑状态
     *
     * @param id
     * @return
     */
    String getCanEditById(BigInteger id);

    /**
     * 修改用户接收消息的配置信息
     *
     * @return {@link UserMessageSettingEntity}
     */
    int addUserMessageSetting(List<UserMessageSettingEntity> list);

    /**
     * 根据条件获取用户消息接收人
     *
     * @param messageId  消息的id
     * @return {@link UserMessageReceiverSettingEntity}
     */
    UserMessageReceiverSettingEntity getMessageReceiver(@Param("messageId") BigInteger messageId);

    /**
     * 根据条件获取用户消息接收人
     *
     * @param messageId  消息的id
     * @param receiverId 接收人id
     * @return {@link UserMessageReceiverSettingEntity}
     */
    UserMessageReceiverSettingEntity getAllMessageReceiver(@Param("messageId") BigInteger messageId, @Param("receiverId") BigInteger receiverId);

    /**
     * 获取用户接收消息的默认配置信息
     *
     * @return {@link List<OperationMessageTypeEntity>}
     */
    List<OperationMessageTypeEntity> getDefaultMessageSetting();

    /**
     * 添加短信邮件认证信息
     *
     * @return {@link int}
     */
    int addVerifityMessage(CommonUserMessageEntity commonUserMessageEntity);


    /**
     * 获取所有消息
     *
     * @param queryMap
     * @return
     */
    List<OperationMessageEntity> queryAllMessageForPage(Map queryMap);

    /**
     * 获取用户侧不可见消息分类的消息
     *
     * @param queryMap
     * @return
     */
    List<OperationMessageEntity> queryOtherMessageForPage(Map queryMap);

    /**
     * description: Recipient management list
     *
     * @return List<UserMessageReceiverInfoEntity> list
     * @author renhaixiang
     * @date 2018/9/29 16:18
     **/
    List<UserMessageReceiverInfoEntity> getReceiversByCreateUserId(String createdUserId);

    /**
     * 添加短信邮件认证信息
     *
     * @return {@link int}
     */
    UserMessageReceiverEntity getUserReceiver(BigInteger id);

    /**
     * description: 接收人管理新增
     *
     * @param userMessageReceiverEntity 接收人管理
     * @author renhaixiang
     * @date 2018/9/29 17:06
     **/
    int addMessageReceiver(UserMessageReceiverEntity userMessageReceiverEntity);

    int addMessageReceiverM(UserMessageAddVo userMessageAddVo);

    /**
     * description: 当前账户下接收人数量统计
     *
     * @param userId 当前账户id
     * @return receiver count
     * @author renhaixiang
     * @date 2019/4/16 10:21
     **/
    int getReceiverCount(@Param("userId") String userId);

    List<BigInteger> getUserMeaasegeSettingIdByMainId(@Param("arr") String[] ids,@Param("userId") String userId);

    List<UserMessageSettingEntity> getUserMessageSettingByMainId(@Param("mainTypeId")BigInteger mainTypeId,@Param("userId") String userId);

    /**
     * 接收人修改
     *
     * @param userMessageReceiverEntity
     * @return
     */
    int updateMessageReceiver(UserMessageReceiverEntity userMessageReceiverEntity);

    /**
     * desc: the remove of receiver
     *
     * @param userId
     * @param id
     * @return
     */
    int removeMessageReceiver(@Param("userId") String userId, @Param("id")String id);

    /**
     * desc: Delete receiver settings
     *
     * @param receiverId String
     * @param userId
     * @author chenxinyi
     * @date 2018/11/1 15:45
     */
    int removeMessageReceiverSetting(@Param("userId") String userId,@Param("receiverId")String receiverId);

    /**
     * desc: add receiver settings
     *
     * @param list List<UserMessageReceiverSettingEntity>
     * @author chenxinyi
     * @date 2018/11/1 15:45
     */
    int addMessageReceiverSetting(List<UserMessageReceiverSettingEntity> list);

    /**
     * desc: add receiver settings
     *
     * @param list List<UserMessageReceiverSettingEntity>
     * @author chenxinyi
     * @date 2018/11/1 15:45
     */
    int updateMessageReceiverSetting(List<UserMessageReceiverSettingEntity> list);

    /**
     * 获取该用户下 指定消息类型的所有接收人
     *
     * @param messageSettingId 消息的id
     * @param userId           接收人id
     * @return {@link UserMessageReceiverInfoEntity}
     */
    List<UserMessageReceiverInfoEntity> getMessageReceiverInfo(@Param("messageSettingId") String messageSettingId, @Param("userId") String userId);


    /**
     * 删除信息
     *
     * @param ids
     * @return
     */
    int deleteMessage(@Param("ids") String[] ids, @Param("message") MessageDeleteDTO messageDeleteDTO);

    /**
     * 根据接收人id获取其所有信息id
     * @param userId
     * @return
     */
    String[] getMessageIdsByReceiverId(@Param("userId") String userId);

    /**
     * 判断接收人是否已存在
     *
     * @param userId     创建者id
     * @param settingId  message_setting_id
     * @param receiverId 接收人id
     * @return
     */
    String isHasReceiver(@Param("userId") String userId, @Param("settingId") String settingId, @Param("receiverId") String receiverId);

    /**
     * 根据id获取修改接收人的Email 和 mobile
     *
     * @param id
     * @return
     */
    UserMessageReceiverEntity getReceiverEmialAndMobileById(@Param("id") String id, @Param("userId") String userId);

    /**
     * 获取当前接收人关联的消息类型
     *
     * @param userId String
     * @param receiverId String
     * @return List<UserMessageSettingEntity>
     */
    List<UserMessageSettingEntity> getCurrentReceiverMessageSetting(@Param("userId") String userId, @Param("receiverId") String receiverId);

    /**
     * 获取信息
     * @param userMessageReceiverSettingEntity UserMessageReceiverSettingEntity
     * @return List<UserMessageReceiverSettingEntity>
     */
    List<UserMessageReceiverSettingEntity> getUserMessageReceiverSettingEntity(UserMessageReceiverSettingEntity userMessageReceiverSettingEntity);

    /**
     * 获取账号联系人
     * @param id
     */
    Integer getMessageReceiverSelf(@Param("id") String id);

    /**
     * 添加账号联系人
     * @Param UserMessageReceiverEntity
     */
    Integer addMessageReceiverSelf(UserMessageReceiverEntity userMessageReceiverEntity);

    /**
     * 获取settingId
     * @Param map
     */
    List<BigInteger> getMsgSettingId(Map querymap);

    int getCountByMsgSettingId(Map map);

    int  updateMsgReceiverSettingByType(Map map);

    Integer getTypeLevelById(BigInteger messageId);

    BigInteger getMessageReceiverId(BigInteger id);

    List<OperationMessageTypeEntity> getMessageTypesCount();

    Integer getMessageCount(@Param("messageTypeId")BigInteger messageTypeId,@Param("receiverId") String receiverId);

    List<BigInteger> getChildMessageTypeId(@Param("id") BigInteger id);

    /**
     * 获取子消息类型
     * @param id
     * @return
     */
    List<BigInteger> getInvalidChildMessageTypeId(@Param("id") BigInteger id);

    List<BigInteger> getInnerChildMessageTypeId(BigInteger id);

    List<BigInteger> getUserAllMainTypeId(String userId);

    Integer updateUserMsgSettingById(@Param("settingVo") UserMsgReceiverSettingVo userMsgReceiverSettingVo, @Param("updatedTime") Date updatedTime);

    /**
     *
     * @param userId
     * @return
     */
    List<String> getUserSelfNumber(@Param("userId") String userId);

    /**
     * 获取无效的子消息类型
     * @param id
     * @return
     */
    List<BigInteger> getInvalidChildIds(@Param("id") BigInteger id);

    /**
     * 获取账号联系人姓名和id
     * @param userId
     * @return
     */
    List<UserMessageReceiverInfoEntity> getMessageReceiverSelfById(@Param("userId") String userId);

    /**
     * 获取用户设置下所有联系人
     * @param s
     * @param userId
     * @return
     */
    List<UserMessageReceiverInfoEntity> getMessageReceiverInfoById(@Param("messageSettingId") String s,@Param("userId") String userId);

    /**
     * 通过联系人姓名获取消息接收人
     * @param userId
     * @param receiverName
     * @return
     */
    List<UserMessageReceiverInfoEntity> getReceiversByName(@Param("userId") String userId,@Param("receiverName") String receiverName);
}
