package com.yangyang.cloud.fileoperation.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * desc: file attribute for return
 *
 * @param
 * @author chengym
 * @return
 * @date 2018/12/05 16:29
 */
@Data
public class FileBean {
    private BigInteger id;
    private String name;
    private String url;
    private int size;
    private String moduleId;
    private String fileAttribute;
    private String fileAttribute2;
    /**
     * 附件后缀
     */
    private String suffix;

    FileBean(){

    }
    public FileBean(String name, String url){
        this.name = name;
        this.url = url;
    }
    public FileBean(String name, String url,int size){
        this.name = name;
        this.url = url;
        this.size = size;
    }

    public FileBean(String name, String url,int size,String moduleId){
        this.name = name;
        this.url = url;
        this.size = size;
        this.moduleId = moduleId;
    }
    public FileBean(String name, String url,int size,String moduleId,String fileAttribute,String fileAttribute2){
        this.name = name;
        this.url = url;
        this.size = size;
        this.moduleId = moduleId;
        this.fileAttribute = fileAttribute;
        this.fileAttribute2 = fileAttribute2;
    }

    public FileBean(BigInteger id,String name, String url,int size,String moduleId,String fileAttribute,String fileAttribute2){
        this.id = id;
        this.name = name;
        this.url = url;
        this.size = size;
        this.moduleId = moduleId;
        this.fileAttribute = fileAttribute;
        this.fileAttribute2 = fileAttribute2;
    }

}
