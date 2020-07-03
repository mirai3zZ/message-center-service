package com.yangyang.cloud.fileoperation.mapper;

import com.yangyang.cloud.fileoperation.bean.FileBean;
import com.yangyang.cloud.fileoperation.bean.OperationAttachmentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface OperationAttachmentInfoMapper {

    int saveOperationAttachment(OperationAttachmentInfo operation);

    List<OperationAttachmentInfo> selectAll(OperationAttachmentInfo operationAttachmentInfo);

    int update(OperationAttachmentInfo operationAttachmentInfo);

    void updateAttachmentList(@Param("attachmentIds") List<BigInteger> attachmentIds,@Param("moduleId") String moduleId);

    int deleteByPrimaryKey(BigInteger id);

    List<FileBean> selectAllFileBean(@Param("moduleCode") String moduleCode, @Param("moduleId") String moduleId);

    int deleteByFileUrl(@Param("fileUrl") String realurl);

    Integer getAttachmentByUrl(String realurl);

    void  deleteByFileAttribute(Map map);

    void delectFileByModuleId(String moduleId);
}
