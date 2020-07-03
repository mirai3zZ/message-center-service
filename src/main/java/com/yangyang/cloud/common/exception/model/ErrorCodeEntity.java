package com.yangyang.cloud.common.exception.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * model entity for table 'common_error_code'
 *
 * @author sunguangtao 2018-10-20
 */
@Data
@Slf4j
public class ErrorCodeEntity {
    private Long id;
    private String projectCode;
    private String projectName;
    private String moduleCode;
    private String moduleName;
    private String errorCode;
    private String errorMessage;
    private Date createdTime;
}
