package com.yangyang.cloud.fileoperation.service;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.constants.CommonConstants;
import com.yangyang.cloud.common.exception.ApiException;
import com.yangyang.cloud.fileoperation.bean.FileBean;
import com.yangyang.cloud.fileoperation.common.FileConstants;
import com.yangyang.cloud.fileoperation.fastdfs.FastDFSClient;
import com.yangyang.cloud.fileoperation.fastdfs.FastDFSFile;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * desc:
 *
 * @author chengym
 * @date 2018/12/04 9:04
 */
@Service
@Slf4j
public class FileService {
    @Autowired
    private OperationAttachmentInfoService operationAttachmentInfoService;

    /**
     * upload file to server
     *
     * @param multipartFile file
     * @return url
     */
    public ResponseBean saveFile(MultipartFile multipartFile) {
        ResponseBean responseBean = new ResponseBean();
        if (multipartFile == null) {
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_NOT_EXIST_CODE, FileConstants.FILE_OPERATION_FILE_NOT_EXIST);
        }
        String fileName = multipartFile.getOriginalFilename();
        log.info("start to upload file {}", fileName);
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        //判断是否在允许的扩展名里
        if (!fileExtInArray(ext)) {
            responseBean.setCode(FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE);
            responseBean.setMessage(FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_EXT);
            return responseBean;
        }
        FileBean fileBean;
        try {
            //ext 后缀 fileName 文件名
            fileBean = uploadFileForFileBean(multipartFile, fileName, ext);
            fileBean.setSuffix(ext);
        } catch (IOException e) {
            log.error(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE + "upload file Exception!", e);
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL);
        }
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setResult(fileBean);
        return responseBean;
    }

    /**
     * @param ext
     * @return
     */
    private Boolean fileExtInArray(String ext) {
        if (!StringUtils.isEmpty(ext)) {
            if (FileConstants.getOperationExpSet().contains(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * upload file to the server
     *
     * @param multipartFile file
     * @param fileName      file name
     * @param ext           the type of the file
     * @return url get from the server
     * @throws IOException
     */
    private String uploadFileForpath(MultipartFile multipartFile, String fileName, String ext) throws IOException {
        String[] fileAbsolutePath = {};
        InputStream inputStream = multipartFile.getInputStream();
        byte[] fileBuff = null;
        if (inputStream != null) {
            int len1 = inputStream.available();
            fileBuff = new byte[len1];
            inputStream.read(fileBuff);
        }
        FastDFSFile file = new FastDFSFile(fileName, fileBuff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file);
        } catch (Exception e) {
            log.error("upload file Exception!", e);
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL);
        }
        if (fileAbsolutePath == null) {
            log.error("upload file failed,please upload again!");
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL);
        }
        return FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
    }

    /**
     * upload file to the server
     *
     * @param multipartFile file
     * @param fileName      file name
     * @param ext           the type of the file
     * @return url get from the server
     * @throws IOException
     */
    private FileBean uploadFileForFileBean(MultipartFile multipartFile, String fileName, String ext) throws IOException {
        String[] fileAbsolutePath = {};
        InputStream inputStream = multipartFile.getInputStream();
        byte[] fileBuff = null;
        int len1 = 0;
        if (inputStream != null) {
            len1 = inputStream.available();
            fileBuff = new byte[len1];
            inputStream.read(fileBuff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, fileBuff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file);
        } catch (Exception e) {
            log.error("upload file Exception!", e);
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL);
        }
        if (fileAbsolutePath == null) {
            log.error("upload file failed,please upload again!");
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL);
        }
        //String name, String url,int size
        return new FileBean(fileName, FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1], len1);
    }

    /**
     * delete file from fastdfs server
     *
     * @param fileUrl
     * @return
     */
    public ResponseBean delFile(String fileUrl) {
        ResponseBean responseBean = new ResponseBean();
        String realurl = "";
        try {
            realurl = new String(Base64.getDecoder().decode(fileUrl), "UTF-8");
            log.info("start to delete file by fileUrl {}", realurl);
        } catch (UnsupportedEncodingException e) {
            log.info("error decode url from String ,error is {}", e);
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_DEL_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_DEL_FAIL);
        }
        //删除服务器文件
        //判断 finish表存在图片信息，不删除服务器文件
        Integer attachmentEntity = operationAttachmentInfoService.getAttachmentByUrl(realurl);
        int delFlag = 0;
        if (attachmentEntity == 0 ){
            log.info("delete FastDFSDelFile ",realurl);
            delFlag = callFastDFSDelFile(realurl);
        }
        //删除数据库数据
        delFlag = operationAttachmentInfoService.deleteByFileUrl(realurl);
        log.info("delete fileUrl number",delFlag);
//        if (delFlag != 0) {
//            responseBean.setCode(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_NOT_EXIST_CODE);
//            responseBean.setMessage(FileConstants.FILE_OPERATION_FILE_NOT_EXIST);
//        } else {
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage(FileConstants.FILE_OPERATION_FILE_DEL_SUCCESS);
//        }
        return responseBean;
    }

    /**
     * call fastdfs server from server
     *
     * @param realurl file name
     * @return
     */
    private int callFastDFSDelFile(String realurl) {
        String fileGroup = realurl.split("/")[3];
        String fileStorage = (realurl.split(fileGroup)[1]).replaceFirst("/", "");
        log.info("fileGroup {},fileStorage {}", fileGroup, fileStorage);
        try {
            int delFlag = FastDFSClient.deleteFile(fileGroup, fileStorage);
            return delFlag;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(CommonConstants.OPERATION_PROJECT_CODE + "." + FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_DEL_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_DEL_FAIL);
        }
    }

    public ResponseBean saveFileWithThumb(MultipartFile multipartFile) {
        ResponseBean responseBean = new ResponseBean();
        Map<String, FileBean> resultMap = new HashMap<>();
        if (multipartFile == null) {
            throw new ApiException(FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_NOT_EXIST_CODE, FileConstants.FILE_OPERATION_FILE_NOT_EXIST);
        }
        String fileName = multipartFile.getOriginalFilename();
        //身份证图片识别
        log.info("start to upload file {}", fileName);
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String path = null;
        FileBean fileBean;
        String temmFileThumb = UUID.randomUUID().toString() + fileName;

        try {
            if (FileConstants.getImageExpSet().contains(ext)) {
                Thumbnails.of(multipartFile.getInputStream()).size(320, 640).toFile(FileConstants.getFileTempRoot() + System.getProperty("file.separator") + temmFileThumb);
                String fileNames = FileConstants.getFileTempRoot() + System.getProperty("file.separator") + temmFileThumb;
                //IdCardImageFaceVo idCardImage = getIdCardImage(fileNames, imageType);

                File tempFlieThumb = new File(fileNames);
                String s = temmFileThumb.toString();
                FileBean fileBean1 = uploadFileForFileBean(tempFlieThumb, fileName, ext);
                resultMap.put("thumbnail", fileBean1);
                tempFlieThumb.delete();
                //ext 后缀 fileName 文件名
                fileBean = uploadFileForFileBean(multipartFile, fileName, ext);
                resultMap.put("original", fileBean);
            } else {
                responseBean.setCode(FileConstants.FILE_OPERATION_FILE_TYPE_ISERROR);
                responseBean.setMessage(FileConstants.FILE_OPERATION_FILE_DTYPE_ISERROR_MESSAGE);

            }
        } catch (UnsupportedFormatException e) {
            log.error(FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE + "upload file Exception!", e);
            throw new ApiException(FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL);
        } catch (IOException e) {
            log.error(FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_NO_SUPPORT_FORMAT + "upload file Exception!", e);
            throw new ApiException(FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_NO_SUPPORT_FORMAT, FileConstants.FILE_OPERATION_FILE_UPLOAD_THUMP_FAIL);
        }
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        responseBean.setResult(resultMap);
        return responseBean;
    }

    /**
     * @param fileThumb
     * @param fileName
     * @param ext
     * @return
     * @throws IOException
     */
    private FileBean uploadFileForFileBean(File fileThumb, String fileName, String ext) throws IOException {
        String path = fileThumb.getPath();
        String[] fileAbsolutePath = {};
        InputStream inputStream = new FileInputStream(fileThumb);
        byte[] file_buff = null;
        int len1 = 0;
        if (inputStream != null) {
            len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file);
        } catch (Exception e) {
            log.error("upload file Exception!", e);
            throw new ApiException(FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL);
        }
        if (fileAbsolutePath == null) {
            log.error("upload file failed,please upload again!");
            throw new ApiException(FileConstants.FILE_OPERATION_MODULE_CODE + FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL_CODE, FileConstants.FILE_OPERATION_FILE_UPLOAD_FAIL);
        }
        //String name, String url,int size
        return new FileBean(fileName, FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1], len1);
    }


}
