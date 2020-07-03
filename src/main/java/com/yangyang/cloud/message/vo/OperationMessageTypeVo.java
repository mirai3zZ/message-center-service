package com.yangyang.cloud.message.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class OperationMessageTypeVo {

    private String id;
    private String typeName;
    private String defaultMailSetting;
    private String defaultEmailSetting;
    private String defaultSmsSetting;
    private String canEdit;
    private String parentId;
    private String typeLevel;

    public OperationMessageTypeVo(String id) {
        this.id = id;
    }
}
