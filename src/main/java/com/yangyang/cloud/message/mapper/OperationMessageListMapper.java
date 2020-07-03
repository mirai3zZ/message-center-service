package com.yangyang.cloud.message.mapper;

import com.yangyang.cloud.message.bean.OperationMessageList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OperationMessageListMapper {
    /**
     * desc 根据条件获取 Operation_message_list 表中 status为SUCCESS ，receiver_id 为 userId,message_main_type 为 messageMaintype
     *      message_template_type 为 mail 的数据
     * @return List<OperationMessageList>
     */
    List<OperationMessageList> getOperationMessageList();
}
