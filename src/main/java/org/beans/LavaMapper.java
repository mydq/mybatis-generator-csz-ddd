package org.beans;

/**
 * @Author: csz
 * @Date: 2018/10/12 14:28
 */

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LavaMapper<T, E> {
    /**
     * 动态字段,写入数据库记录
     * @param var1
     * @return
     */
    int insertSelective(T var1);

    /**
     * 根据主键删除数据库的记录
     * @param var1
     * @return
     */
    int deleteByPrimaryKey(T var1);

    /**
     * 根据条件，删除数据库的记录
     * @param var1
     * @return
     */
    int deleteByExample(E var1);

    /**
     * 根据指定的条件获取数据库记录数
     * @param var1
     * @return
     */
    int countByExample(E var1);

    /**
     * 根据指定的条件查询符合条件的数据库记录
     * @param var1
     * @return
     */
    List<T> selectByExample(E var1);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录
     * @param var1
     * @param var2
     * @return
     */
    int updateByExampleSelective(@Param("record") T var1, @Param("example") E var2);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录
     * @param var1
     * @return
     */
    int updateByExampleSelective(Map<String, Object> var1);

    /**
     * 根据指定的条件来更新符合条件的数据库记录
     * @param var1
     * @param var2
     * @return
     */
    int updateByExample(@Param("record") T var1, @Param("example") E var2);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     * @param var1
     * @return
     */
    int updateByPrimaryKeySelective(T var1);

    /**
     * 根据指定主键获取一条数据库记录
     * @param var1
     * @return
     */
    T selectByPrimaryKey(Long var1);

    /**批量插入
     * @param recordLst
     */
    void insertBatch(List<T> recordLst);

    /**
     * 根据主键，批量更新
     * @param records
     * @return
     */
    int updateBatchByPrimaryKeySelective(List<T> records);
}
