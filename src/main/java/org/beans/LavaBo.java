package org.beans;

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
}
