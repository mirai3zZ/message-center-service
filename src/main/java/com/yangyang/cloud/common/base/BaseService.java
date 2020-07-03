package com.yangyang.cloud.common.base;

import com.thoughtworks.xstream.core.SequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseService<T> {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected SequenceGenerator sequenceGenerator;

    public abstract BaseMapper<T> getMapper();

    /**
     * 取得Mapper路径名
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getMapperClassPath(String sqlid) {
        Class<?>[] classes = getMapper().getClass().getInterfaces();
        String mapper = classes[0].getName() + "." + sqlid;
        return mapper;
    }

    /**
     * 添加
     *
     * @param t
     * @return
     * @see [类、类#方法、类#成员]
     */
    public void add(T t) {
        getMapper().add(t);
    }

    /**
     * 更新(空值不更新)
     *
     * @param dtos
     * @see [类、类#方法、类#成员]
     */
    public void BatchUpdateBySelective(List<T> dtos) {
        for (T dto : dtos) {
            getMapper().updateBySelective(dto);
        }
    }

    /**
     * 删除数据
     *
     * @param ids
     * @see [类、类#方法、类#成员]
     */
    public void delete(Object... ids) {
        if (ids == null || ids.length < 1) {
            return;
        }
        for (Object id : ids) {
            getMapper().delete(id);
        }
    }



}
