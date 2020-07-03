package com.yangyang.cloud.notice.dto;

import com.yangyang.cloud.common.paginate.Page;
import com.yangyang.cloud.notice.bean.NoticeBean;
import lombok.Data;

import java.util.List;

/**
 * description:公告分类数据
 *
 * @author: LiuYang01
 * @date: 2019/9/13 14:26
 */
@Data
public class NoticeTypeDTO {
    private String noticeTypeId;
    private String noticeTypeName;
    private Page notices;
}
