package com.yangyang.cloud.common.exception.service;

import com.yangyang.cloud.common.exception.dao.ErrorCodeMapper;
import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service layer for error code
 *
 * @author sunguangtao 2018-10-22
 */
@Service
@Slf4j
public class ErrorCodeService {
    private final ErrorCodeMapper errorCodeMapper;
    private static final String PROJECT_CODE = "802";
    private static final String MODUlE_CODE_WORKORDER = "003";

    @Autowired
    public ErrorCodeService(ErrorCodeMapper errorCodeMapper) {
        this.errorCodeMapper = errorCodeMapper;
    }

    //@Cacheable(value = "errorCodeCache", key = "#projectCode+#moduleCode+#errorCode")
    public ErrorCodeEntity getErrorCodeEntity(String projectCode, String moduleCode, String errorCode) {
        log.debug("Cache miss.");
        return errorCodeMapper.getErrorCodeEntity(projectCode, moduleCode, errorCode);
    }
    
    /**
     * 获得工单模块的 errorCode
     * @param errorCode 错误码
     * @return ErrorCodeEntity
     */
    public ErrorCodeEntity getWorkOrderError(String errorCode) {
    	return errorCodeMapper.getErrorCodeEntity(PROJECT_CODE, MODUlE_CODE_WORKORDER, errorCode);
    }
}
