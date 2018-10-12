package org.beans;

import java.util.List;
import java.util.Map;

/**
 * @Author: csz
 * @Date: 2018/10/12 14:26
 */

public interface LavaBo<D extends LavaDo, E extends LavaExample> {
    int insert(D var1);

    int delete(Long var1);

    int update(D var1);

    D selectByPrimaryKey(Long var1);

    boolean isValidDo(D var1);

    int deleteByExample(E example);

    List<D> selectByExample(E example);

    int countByExample(E example);

    DataResult<D> getPageByExample(E example);

    int updateByExample(D record, E example);

    int updateByExample(Map<String, Object> map);

    void insertBatchSelective(List<D> recordLst);

    int updateBatchByPrimaryKeySelective(List<D> records);

}
