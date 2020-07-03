package com.yangyang.cloud.message.mapper;

import java.math.BigInteger;
import java.util.List;

import com.yangyang.cloud.messagemanage.dto.UpdateMessageTypeNameDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yangyang.cloud.message.bean.OperationMessageTypeEntity;
import com.yangyang.cloud.message.vo.MessageTypeListVO;

/**
 * @author daiyan
 */
@Mapper
public interface OperationMessageTypeMapper {
	/**
	 * 获取有效的状态的消息分类集合
	 * @return List<OperationMessageTypeEntity>
	 */
	List<OperationMessageTypeEntity> getList();
	
	/**
	 * 获取有效的状态的消息分类集合，只取接口需要的字段
	 * @return List<MessageTypeListVO>
	 */
	List<MessageTypeListVO> getIdAndNames();
	
	/**
	 * 根据 id 获取某一分类或类型的对象
	 * @param id BigInteger
	 * @param status String 是否有效（Y/N）
	 * @param typeLevel int 0:分类（mainType）;1:类型(分类的子类)
	 * @return OperationMessageTypeEntity OperationMessageTypeEntity
	 */
	OperationMessageTypeEntity getById(@Param("id") BigInteger id,
			@Param("status") String status,
			@Param("typeLevel") int typeLevel);
	
	/**
	 * 根据 typeName 获取某一分类或类型的对象
	 * @param typeName String
	 * @return String
	 */
	String getByName(@Param("typeName") String typeName,@Param("id") String id);

	/**
	 * 根据 typeCode 获取某一分类或类型的对象
	 * @param typeCode String
	 * @return String
	 */
	String getByCode(@Param("typeCode") String typeCode,@Param("id") String id);
	
	/**
	 * add
	 * @param operationMessageType OperationMessageTypeEntity
	 */
	void add(OperationMessageTypeEntity operationMessageType);

	/**
	 * 根据主分类名称判断该名称是否存在
	 * @param mainTypeName String
	 * @return String
	 */
	String getByMainTypeName(@Param("mainTypeName") String mainTypeName,@Param("id") String id);

	void updateMessageTypeName(UpdateMessageTypeNameDTO updateMessageTypeNameDTO);
	void updateMessageTypeParentName(UpdateMessageTypeNameDTO updateMessageTypeNameDTO);

	OperationMessageTypeEntity getOperationMessageTypeEntityById(BigInteger messageTypeId);

	List<OperationMessageTypeEntity> getChildMessageByParentId(BigInteger parentId);
}
