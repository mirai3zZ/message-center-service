package com.yangyang.cloud.fileoperation.service;

import com.inspur.bss.commonsdk.utils.IdWorker;
import com.yangyang.cloud.beian.bean.BeianSponsorEntity;
import com.yangyang.cloud.beian.bean.OperationAttachmentEntity;
import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.fileoperation.bean.FileBean;
import com.yangyang.cloud.fileoperation.bean.OperationAttachmentInfo;
import com.yangyang.cloud.fileoperation.common.FileConstants;
import com.yangyang.cloud.fileoperation.mapper.OperationAttachmentInfoMapper;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import com.yangyang.cloud.workorder.dto.WorkOrderMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OperationAttachmentInfoService {
    //模块code
    //运营-工单-沟通记录模块
    public static final String OPERATION_WORKORDER_MESSAGE = "operation_workorder_message";

    //文件删除异常
    public static final String OPERATION_DELETE_ERROR_MESSAGE = "文件同步删除失败";



    @Autowired
    private OperationAttachmentInfoMapper operationAttachmentInfoMapper;

    /**
     * 拼接 附件入库的 数据
     *
     * @param modelCode
     * @param fileBean
     * @return
     */
    public OperationAttachmentInfo groupOperation(String modelCode, FileBean fileBean) {
        OperationAttachmentInfo operationAttachmentInfo = new OperationAttachmentInfo();
        String url = fileBean.getUrl();
        String originalName = fileBean.getName();
        String ext = originalName.substring(originalName.lastIndexOf(".") + 1);
        String[] fileUrl = url.split("/");
        String fileName = fileUrl[fileUrl.length - 1];
        operationAttachmentInfo.setId(fileBean.getId());
        operationAttachmentInfo.setFileName(fileName);/** 附件保存的名称*/
        operationAttachmentInfo.setOriginalName(originalName);/** 原始附件名称*/
        operationAttachmentInfo.setFileSize(fileBean.getSize());/** 附件大小字节数（byte）*/
        operationAttachmentInfo.setFuleUrl(url);/** url路径*/
        operationAttachmentInfo.setFileSuffix(ext);/** 附件后缀 doc,jpg'*/
        operationAttachmentInfo.setModuleId(fileBean.getModuleId());
        operationAttachmentInfo.setModuleCode(modelCode);
        operationAttachmentInfo.setFileAttribute(fileBean.getFileAttribute());
        operationAttachmentInfo.setFileAttribute2(fileBean.getFileAttribute2());
        return operationAttachmentInfo;
    }

    /**
     * 新增文件数据
     *
     * @param operation
     * @return
     */
    public ResponseBean saveOperationAttachment(OperationAttachmentInfo operation) {
        User user = SecurityContextUtil.getLoginUser();
        ResponseBean responseBean = new ResponseBean();
        operation.setId(BigInteger.valueOf(IdWorker.getNextId()));
        operation.setCreatedTime(new Date());
        if(user!=null){
            operation.setCreatedUserId(user.getId());
            operation.setCreatedUserName(user.getName());
        }
        operationAttachmentInfoMapper.saveOperationAttachment(operation);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        return responseBean;
    }

    /**
     * 查询文件
     *
     * @param
     * @return
     */
    public List<FileBean> selectAllFileBean(String modelCode, String modelId) {
        List<FileBean> list = operationAttachmentInfoMapper.selectAllFileBean(modelCode, modelId);
        return list;
    }

    /**
     * 查询文件
     *
     * @param operationAttachmentInfo
     * @return
     */
    public List<OperationAttachmentInfo> selectAll(OperationAttachmentInfo operationAttachmentInfo) {
        List<OperationAttachmentInfo> list = operationAttachmentInfoMapper.selectAll(operationAttachmentInfo);
        return list;
    }

    /**
     * 修改文件信息
     *
     * @param operationAttachmentInfo
     * @return
     */
    public ResponseBean update(OperationAttachmentInfo operationAttachmentInfo) {
        ResponseBean responseBean = new ResponseBean();
        operationAttachmentInfoMapper.update(operationAttachmentInfo);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        return responseBean;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public ResponseBean deleteByPrimaryKey(Integer id) {
        ResponseBean responseBean = new ResponseBean();
        operationAttachmentInfoMapper.deleteByPrimaryKey(BigInteger.valueOf(id));
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        return responseBean;
    }

    /**
     * 删除文件信息
     * @param realurl
     * @return
     */
    public int deleteByFileUrl(String realurl) {
        return  operationAttachmentInfoMapper.deleteByFileUrl(realurl);
    }

    /**
     * 查询finish 表，判断服务器文件能否删除
     * @param realurl
     * @return
     */
    public Integer getAttachmentByUrl(String realurl){
        return operationAttachmentInfoMapper.getAttachmentByUrl(realurl);
    }

    /**
     * 根据图片类型删除图片
     * @param map
     */
    public void deleteByFileAttribute(Map map){
        operationAttachmentInfoMapper.deleteByFileAttribute(map);
    }

    /**
     * 根据moduleId删除图片信息
     * @param moduleId
     */
    public void delectFileByModuleId(String moduleId){
        operationAttachmentInfoMapper.delectFileByModuleId(moduleId);
    }
}
