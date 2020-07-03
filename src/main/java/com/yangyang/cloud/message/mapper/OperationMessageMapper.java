package com.yangyang.cloud.message.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yangyang.cloud.message.bean.OperationMessageEntity;

/**
 * @author daiyan
 */
@Mapper
public interface OperationMessageMapper {
	/**
	 * get OperationMessageEntity List
	 * @param deleteFlag String
	 * @param readStatus String
	 * @param receiverId String
	 * @param messageTemplateType String
	 * @return List<OperationMessageEntity>
	 */
	public List<OperationMessageEntity> getList(@Param("deleteFlag") String deleteFlag,
			@Param("readStatus") String readStatus,
			@Param("receiverId") String receiverId,
			@Param("messageTemplateType") String messageTemplateType);

	/**
	 * 查询邮件群发失败原因
	 * @param senderEmail
	 * @return
	 */
	String getEmailFailedReason(@Param("senderEmail") String senderEmail);

	/**
	 * 查询发送完成的邮件
	 * @param senderEmail
	 * @return
	 */
	int getSuccessEmail(@Param("senderEmail") String senderEmail);
}
