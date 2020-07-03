package com.yangyang.cloud.message.vo;

import lombok.Data;

@Data
public class BatchAddReceiverVo {
    private String[] settingIds;
    private String[] receiversIds;
}
