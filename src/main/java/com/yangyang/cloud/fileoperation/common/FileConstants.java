package com.yangyang.cloud.fileoperation.common;

import com.yangyang.cloud.OperationServiceApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

/**
 *
 */
public class FileConstants {

    public static final String FILE_OPERATION_MODULE_CODE = "666";
    public static final String FILE_OPERATION_MODULE = "文件处理模块";

    public static final String FILE_OPERATION_FILE_NOT_EXIST_CODE = "004";
    public static final String FILE_OPERATION_FILE_NOT_EXIST = "文件不存在";

    public static final String FILE_OPERATION_FILE_UPLOAD_FAIL_CODE = "005";
    public static final String FILE_OPERATION_FILE_UPLOAD_FAIL = "文件上传失败";
    public static final String FILE_OPERATION_FILE_UPLOAD_FAIL_EXT = "文件上传格式不支持";

    public static final String FILE_OPERATION_FILE_DEL_FAIL_CODE = "005";
    public static final String FILE_OPERATION_FILE_DEL_FAIL = "文件删除失败";
    public static final String FILE_OPERATION_FILE_DEL_SUCCESS = "文件删除成功";
    public static final String FILE_OPERATION_FILE_TYPE_ISERROR = "008";
    public static final String FILE_OPERATION_FILE_DTYPE_ISERROR_MESSAGE = "文件格式不正确";
    public static final String FILE_OPERATION_FILE_NO_SUPPORT_FORMAT = "009";
    public static final String FILE_OPERATION_FILE_UPLOAD_THUMP_FAIL = "压缩图片上传失败";

    private static HashSet<String> operationExtSet   = new HashSet<>();
    //operation
    public static HashSet<String> getOperationExpSet() {
        operationExtSet.add("jpg");
        operationExtSet.add("bmp");
        operationExtSet.add("png");
        operationExtSet.add("gif");
        operationExtSet.add("txt");
        operationExtSet.add("rar");
        operationExtSet.add("zip");
        operationExtSet.add("doc");
        operationExtSet.add("docx");
        operationExtSet.add("conf");
        operationExtSet.add("eml");
        operationExtSet.add("pdf");
        operationExtSet.add("xlsx");
        operationExtSet.add("xls");
        return operationExtSet;
    }
    public static HashSet<String> getImageExpSet() {
        operationExtSet.add("jpg");
//        operationExtSet.add("bmp");
//        operationExtSet.add("png");
//        operationExtSet.add("gif");

        return operationExtSet;
    }

    public static String getFileTempRoot() {
        File tempFlie = new File(getRootPath());
        if (!tempFlie.exists()) {
            tempFlie = new File("");
        }
        //如果上传目录为/upload/,则可以如下获取
        File upload = new File(tempFlie.getAbsolutePath(), "uplaod/temp");
        if (!upload.exists()) {
            upload.mkdirs();
            System.out.println(upload.getAbsolutePath());
        }
        return upload.getAbsolutePath();
    }

    //获取文件的根目录
    public static String getRootPath() {
        try {
            return new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ApplicationHome(OperationServiceApplication.class).getSource().getParentFile().getPath();
    }
}
