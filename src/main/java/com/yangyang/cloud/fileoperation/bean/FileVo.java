package com.yangyang.cloud.fileoperation.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * desc:
 *
 * @param
 * @author chengym
 * @return
 * @date 2018/12/05 18:38
 */
@Data
public class FileVo {
    @NotNull
    private String fileUrl;
}
