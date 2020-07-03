package com.yangyang.cloud.fileoperation.controller;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.constants.CommonConstants;
import com.yangyang.cloud.common.exception.ApiException;
import com.yangyang.cloud.fileoperation.bean.FileVo;
import com.yangyang.cloud.fileoperation.common.FileConstants;
import com.yangyang.cloud.fileoperation.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 文件处理
 * @author chengym
 * @date 2018/12/04
 */
@RestController
@Slf4j
@RequestMapping("/operation/file")
public class FileController {
    @Autowired
    FileService fileService;

    @CrossOrigin
    @PostMapping(value = "/upload",consumes= MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseBean singleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            return  fileService.saveFile(file);
        } catch (Exception e) {
            log.error("upload file failed", e);
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_NOT_EXIST_CODE,FileConstants.FILE_OPERATION_FILE_NOT_EXIST);
        }
    }
    @DeleteMapping("/delete")
    public ResponseBean deleteFile(@RequestBody FileVo fileVo){
        return fileService.delFile(fileVo.getFileUrl());
    }
    @CrossOrigin
    @PostMapping("/upload/thumbs")
    public ResponseBean singleFileUploadWithThumb(@RequestParam("file") MultipartFile file) {
        try {
            return fileService.saveFileWithThumb(file);
        } catch (Exception e) {
            log.error("upload file failed", e);
            throw new ApiException(FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_NOT_EXIST_CODE, FileConstants.FILE_OPERATION_FILE_NOT_EXIST);
        }
    }

    /**
     * 批量删除文件信息 备案用
     * @param list
     * @return
     */
    @DeleteMapping("/deleteall")
    public ResponseBean deleteallFile(@RequestBody List<String> list){
        ResponseBean responseBean = new ResponseBean();
        if (list != null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                responseBean = fileService.delFile(list.get(i));
            }
        }else{
            responseBean.setCode(FileConstants.FILE_OPERATION_FILE_DEL_FAIL_CODE);
            responseBean.setMessage(FileConstants.FILE_OPERATION_FILE_DEL_SUCCESS);
        }
        return responseBean;
    }

}