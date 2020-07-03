package com.yangyang.cloud.common.util;


import com.inspur.bss.commonsdk.utils.IdWorker;
import com.yangyang.cloud.common.redis.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 基于单机的redis实现,若后续redis实现集群还需优化 TODO
 *
 * @author : zhanghuazong
 * @date : 2019/7/4 14:37
 */
@Service
public class IdGenerator {
    @Autowired
    private RedisUtil redisUtil;

    private String workorderBillIdKey = "operation-service:workorder_id";

    private String lastDate = "";

    /**
     * 生成工单标号,工单规则为YYYYMMDD+6位流水号
     *
     * @return
     */
    public long buildIdByRedis() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
            LocalDateTime now = LocalDateTime.now();
            String prefix = now.format(formatter);
            if (StringUtils.equals(lastDate, prefix)) {
                return getNextId(prefix);
            } else {
                //重置
                lastDate = prefix;
                redisUtil.set(workorderBillIdKey, 0);
                return getNextId(prefix);
            }
        } catch (Exception e) {
            return IdWorker.getNextId();
        }

    }

    public long getNextId(String prefix) {
        Long value = redisUtil.incr(workorderBillIdKey, 1);
        String sequenceId;
        String suffix = String.valueOf(value);
        //Id最长20位
        if (suffix.length() < 6) {
            sequenceId = prefix + String.format("%06d", value);
        } else if (suffix.length() > 12) {
            return IdWorker.getNextId();
        } else {
            sequenceId = prefix + suffix;
        }
        return Long.valueOf(sequenceId);
    }

}
